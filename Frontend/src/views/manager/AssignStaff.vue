<template>
  <div class="assign-staff h-full flex flex-col p-4 sm:p-6 md:p-8 bg-gray-100 h-screen overflow-hidden">
    <p class="text-xl font-bold text-gray-800 mb-3">Assign Staff to Flights</p>

    <div class="mb-3 bg-white p-6 rounded-xl shadow-lg border border-gray-200">
      <label class="block text-sm font-medium text-gray-700 mb-2">Select Flight</label>
      <select v-model="selectedFlightId" @change="loadFlightDetails" class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 text-lg appearance-none cursor-pointer transition duration-150 ease-in-out">
        <option value="">-- Choose a Flight --</option>
        <option v-for="flight in flights" :key="flight.flightId" :value="flight.flightId">
          {{ flight.flightNumber }} - {{ flight.departLocation }} → {{ flight.arrivalLocation }} ({{ formatDate(flight.departTime) }})
        </option>
      </select>
    </div>


    <!-- Loading States generated error state -->
    <div v-if="loadingFlights" class="flex-grow flex items-center justify-center text-indigo-600">
      <i class="fas fa-spinner fa-spin text-4xl mr-3"></i> Loading flights...
    </div>
    <div v-else-if="flightError" class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-8 rounded-xl shadow-md">
      <h3 class="text-red-600 font-bold mb-2 text-xl">Error Loading Flights</h3>
      <p class="text-red-500">{{ flightError }}</p>
    </div>

    <!-- Employee Selection Grid -->
    <div v-else-if="selectedFlightId" class="flex-grow overflow-hidden flex flex-col">
      <div class="mb-3 flex flex-col sm:flex-row justify-between items-start sm:items-center p-4 bg-white rounded-xl shadow-md border-b">
        <div class="mb-2 sm:mb-0">
          <h2 class="text-xl font-bold text-gray-800">Available Employees</h2>
          <p class="text-sm text-gray-600">Click to select employees, then click 'Assign Selected'. Assigned employees are pre-selected.</p>
          <p class="text-sm text-orange-600">Assigned employees are not cumulative.</p>
        </div>
        <div class="flex flex-col sm:flex-row items-stretch sm:items-center space-y-3 sm:space-y-0 sm:space-x-4 w-full sm:w-auto">
          <input v-model="searchQuery" type="text" placeholder="Search employees..." class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 w-full sm:w-48">
          <button @click="assignSelected" :disabled="loadingAssignment || selectedEmployeeIds.length === 0" class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition disabled:bg-gray-400 disabled:cursor-not-allowed text-center">
            <i :class="loadingAssignment ? 'fas fa-spinner fa-spin' : 'fas fa-check'"></i>
            {{ loadingAssignment ? 'Assigning...' : `Assign Selected (${selectedEmployeeIds.length})` }}
          </button>
        </div>
      </div>

      <!-- generated loading frame -->
      <div v-if="loadingEmployees" class="flex-grow flex items-center justify-center text-indigo-600">
        <i class="fas fa-spinner fa-spin text-4xl mr-3"></i> Loading employees...
      </div>

      <!-- Employee Grid with overflow-y-auto -->
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4 overflow-y-auto flex-grow p-1">
        <div v-for="emp in filteredEmployees" :key="emp.id"
             :class="['bg-white rounded-xl shadow-md p-5 border-2 transition hover:shadow-lg cursor-pointer',
                      selectedEmployeeIds.includes(emp.id) ? 'border-indigo-500 bg-indigo-50 shadow-indigo-200' : 'border-gray-200 hover:border-gray-400']"
             @click="toggleEmployee(emp.id)">
          <div class="flex items-start justify-between">
            <div class="flex-grow">
              <h3 class="text-lg font-extrabold text-gray-800">{{ emp.firstName }} {{ emp.lastName }}</h3>
              <p class="text-sm text-gray-600 truncate mb-2">{{ emp.email }}</p>
              <span :class="getRoleBadge(emp.title)" class="inline-block px-3 py-1 text-xs rounded-full font-bold mt-1">
                {{ emp.title }}
              </span>
            </div>
          </div>
          <div class="mt-3 text-xs text-gray-500 border-t pt-2 flex justify-between items-center">
            <span>ID: {{ emp.id }}</span>
            <input type="checkbox" :checked="selectedEmployeeIds.includes(emp.id)" class="w-4 h-4 text-indigo-600 rounded focus:ring-2 focus:ring-indigo-500 ml-2 pointer-events-none">
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="flex items-center justify-center text-gray-400">
      <div class="text-center p-10 bg-white rounded-xl shadow-md border border-gray-200">
        <i class="fas fa-plane-departure text-7xl mb-4 text-indigo-400"></i>
        <p class="text-lg font-semibold">Select a flight from the dropdown to assign employees</p>
      </div>
    </div>

    <!-- Flight Details Modal (Context Box) -->
    <div v-if="showFlightDetailsModal && flightDetails" class="fixed inset-0 bg-black bg-opacity-40 overflow-y-auto h-full w-full z-50 flex justify-center items-center p-4">
      <div class="bg-white rounded-xl shadow-2xl w-full max-w-4xl p-8 transform transition-all duration-300 scale-100 animate-fade-in-down">
        <div class="flex justify-between items-center mb-6 border-b pb-3">
          <h2 class="text-2xl font-extrabold text-indigo-700">Flight Details: {{ flightDetails.flightNumber }}</h2>
          <button @click="showFlightDetailsModal = false" class="text-gray-400 hover:text-gray-600 transition p-2">
            <i class="fas fa-times text-xl"></i>
          </button>
        </div>

        <div v-if="loadingDetails" class="p-8 text-center text-indigo-600">
          <i class="fas fa-spinner fa-spin text-3xl"></i> Loading details...
        </div>

        <div v-else-if="detailError" class="p-4 bg-red-50 text-red-600 border border-red-200 rounded-lg">
          <i class="fas fa-exclamation-triangle mr-2"></i> {{ detailError }}
        </div>

        <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-8 text-gray-700">
          <!-- Column 1: Core Info -->
          <div>
            <h3 class="text-xl font-bold mb-3 text-gray-800">Route & Schedule</h3>
            <div class="space-y-2">
              <p><span class="font-semibold text-gray-900">Route:</span> <i class="fas fa-map-marker-alt text-indigo-500 mr-1"></i> {{ flightDetails.departLocation }} <i class="fas fa-arrow-right text-sm mx-1 text-indigo-500"></i> {{ flightDetails.arrivalLocation }}</p>
              <p><span class="font-semibold text-gray-900">Departure:</span> <i class="fas fa-clock text-indigo-500 mr-1"></i> {{ formatDate(flightDetails.departTime) }}</p>
              <p><span class="font-semibold text-gray-900">Arrival:</span> <i class="fas fa-plane-arrival text-indigo-500 mr-1"></i> {{ formatDate(flightDetails.arrivalTime) }}</p>
              <p><span class="font-semibold text-gray-900">Duration:</span> <i class="fas fa-hourglass-half text-indigo-500 mr-1"></i> {{ flightDetails.flightTime }} minutes</p>
              <p><span class="font-semibold text-gray-900">Status:</span> <span :class="getStatusBadge(flightDetails.status)" class="font-bold">{{ flightDetails.status || 'Scheduled' }}</span></p>
              <p :class="{'text-red-600': flightDetails.delayHours > 0}"><span class="font-semibold text-gray-900">Delay:</span> {{ flightDetails.delayHours ? flightDetails.delayHours + ' hours' : 'None' }}</p>
            </div>
          </div>

          <!-- Column 2: Staff & Capacity -->
          <div>
            <h3 class="text-xl font-bold mb-3 text-gray-800">Staffing & Capacity</h3>
            <div class="space-y-2">
              <p><span class="font-semibold text-gray-900">Capacity:</span> <i class="fas fa-users text-indigo-500 mr-1"></i> {{ flightDetails.capacity }} seats</p>
              <p><span class="font-semibold text-gray-900">Seats Remaining:</span> <span class="font-bold" :class="{'text-green-600': flightDetails.seatsRemaining > 0, 'text-red-600': flightDetails.seatsRemaining === 0}">{{ flightDetails.seatsRemaining }}</span></p>
            </div>

            <p class="mt-6 text-lg font-bold text-gray-900 border-t pt-3">Current Staff Assigned:</p>
            <p class="mt-2">
              <span class="font-semibold text-blue-700"><i class="fas fa-user-tie mr-1"></i> Pilots ({{ flightDetails.pilots ? flightDetails.pilots.length : 0 }}):</span>
              <span class="text-sm block ml-5 italic">{{ flightDetails.pilots && flightDetails.pilots.length > 0 ? flightDetails.pilots.join(', ') : 'No Pilots Assigned' }}</span>
            </p>
            <p class="mt-2">
              <span class="font-semibold text-green-700"><i class="fas fa-person-booth mr-1"></i> Attendants ({{ flightDetails.flightattendants ? flightDetails.flightattendants.length : 0 }}):</span>
              <span class="text-sm block ml-5 italic">{{ flightDetails.flightattendants && flightDetails.flightattendants.length > 0 ? flightDetails.flightattendants.join(', ') : 'No Attendants Assigned' }}</span>
            </p>
          </div>
        </div>

        <div class="mt-8 text-right border-t pt-4">
          <button @click="showFlightDetailsModal = false" class="px-8 py-3 bg-indigo-600 text-white font-semibold rounded-lg hover:bg-indigo-700 transition shadow-md hover:shadow-lg">
            Close & Continue Assignment
          </button>
        </div>
      </div>
    </div>

    <!-- Messages -->
    <div v-if="successMessage" class="fixed bottom-4 right-4 bg-green-600 text-white px-6 py-3 rounded-lg shadow-xl animate-fade-in-right">
      <i class="fas fa-check-circle mr-2"></i> {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="fixed bottom-4 right-4 bg-red-600 text-white px-6 py-3 rounded-lg shadow-xl animate-fade-in-right">
      <i class="fas fa-exclamation-circle mr-2"></i> {{ errorMessage }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'AssignStaff',
  data() {
    return {
      flights: [],
      employees: [],
      selectedFlightId: '',
      selectedEmployeeIds: [],
      searchQuery: '',

      // State for main flights/employees
      loadingFlights: true,
      loadingEmployees: false,
      flightError: null,

      // State for Flight Details Modal
      flightDetails: null, // Holds the details for the selected flight
      showFlightDetailsModal: false,
      loadingDetails: false,
      detailError: null,

      // Messaging
      successMessage: '',
      errorMessage: '',

      loadingAssignment: false, // New state for assignment button

      // API Base URL
      apiUrl: 'http://localhost:8080/api/flights',
      empUrl: 'http://localhost:8080/api/employees',
      manUrl: 'http://localhost:8080/api/managers',
    }
  },
  computed: {
    filteredEmployees() {
      if (!this.employees) return [];
      const query = this.searchQuery.toLowerCase();

      return this.employees.filter(emp =>
          emp.id.toString().includes(query) ||
          emp.firstName.toLowerCase().includes(query) ||
          emp.lastName.toLowerCase().includes(query) ||
          emp.email.toLowerCase().includes(query) ||
          emp.title.toLowerCase().includes(query)
      );
    },
  },
  mounted() {
    this.fetchFlights();
    this.fetchEmployees();
  },
  methods: {
    async fetchFlights() {
      this.loadingFlights = true;
      this.flightError = null;
      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const backendData = await response.json();

        // Assuming backend returns an array of flight objects
        this.flights = backendData.map(flight => ({
          flightId: flight.flightId || flight.id,
          flightNumber: flight.flightNumber,
          departLocation: flight.departLocation,
          arrivalLocation: flight.arrivalLocation,
          departTime: flight.departTime,
        }));

      } catch (e) {
        console.error('Failed to fetch flights:', e);
        this.flightError = e.message || 'The backend service is unavailable or returned an error.';
      } finally {
        this.loadingFlights = false;
      }
    },

    async fetchEmployees() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(this.empUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const data = await response.json();
        this.employees = data.map(emp => ({
          ...emp,
          // Fallback mocking logic for title if not provided by API
          title: emp.title || (emp.id % 3 === 0 ? 'Manager' : (emp.id % 2 === 0 ? 'Flight Attendant' : 'Pilot'))
        }));
      } catch (e) {
        console.error('Fetch Employees Error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },

    async loadFlightDetails() {
      this.selectedEmployeeIds = []; // Clear selection on new flight select
      this.searchQuery = ''; // Clear search
      this.flightDetails = null; // Clear old details

      if (this.selectedFlightId) {
        // Fetch the full details for the selected flight
        await this.fetchFlightDetails(this.selectedFlightId);
        // Only show the modal if details were successfully fetched
        if (this.flightDetails) {
          this.showFlightDetailsModal = true;
        }
      } else {
        this.showFlightDetailsModal = false;
      }
    },

    async fetchFlightDetails(flightId) {
      const detailUrl = `${this.apiUrl}/${flightId}`;
      this.loadingDetails = true;
      this.detailError = null;
      this.flightDetails = null; // Clear details while fetching

      try {
        let details;

        const response = await fetch(detailUrl); // Real API call for non-mocked IDs
        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }
        details = await response.json();

        this.flightDetails = details;

        // Automatically pre-select employees currently assigned to the flight
        this.selectedEmployeeIds = [
          ...(details.pilots || []),
          ...(details.flightattendants || [])
        ].filter(id => id !== null); // Filter out potential nulls

      } catch (e) {
        console.error(`Failed to fetch flight ${flightId} details:`, e);
        this.detailError = e.message || 'Failed to load flight details.';
        this.showError(`Error loading details for flight ${flightId}.`);
      } finally {
        this.loadingDetails = false;
      }
    },

    // Generated
    toggleEmployee(empId) {
      // Ensure the ID is treated as a number for comparison/array methods
      const id = Number(empId);
      const index = this.selectedEmployeeIds.indexOf(id);

      if (index > -1) {
        this.selectedEmployeeIds.splice(index, 1);
      } else {
        this.selectedEmployeeIds.push(id);
      }
    },

    async assignSelected() {
      if (this.selectedEmployeeIds.length === 0) {
        this.showError('Please select at least one employee');
        return;
      }

      this.loadingAssignment = true;
      this.errorMessage = '';

      const flightId = Number(this.selectedFlightId);
      const assignmentUrl = `${this.manUrl}/flights/${flightId}/assign`;

      try {
        const response = await fetch(assignmentUrl, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          // Send the array of employee IDs
          body: JSON.stringify(this.selectedEmployeeIds),
        });

        if (!response.ok) {
          // Attempt to read error message from body if available
          const errorText = await response.text();
          let message = `Failed to assign staff. Server responded with status ${response.status}.`;
          if (errorText) {
            try {
              const errorJson = JSON.parse(errorText);
              message = errorJson.message || message;
            } catch {
              message = `${message} Details: ${errorText.substring(0, 100)}...`;
            }
          }
          throw new Error(message);
        }

        const updatedFlightDetails = await response.json();

        this.flightDetails = updatedFlightDetails;

        // Generated
        // Re-sync selected IDs to match the returned server state
        this.selectedEmployeeIds = [
          ...(updatedFlightDetails.pilots || []),
          ...(updatedFlightDetails.flightattendants || [])
        ].filter(id => id !== null);

        this.showSuccess(`Successfully assigned staff to flight ${this.flightDetails?.flightNumber || this.selectedFlightId}. Details updated.`);

        // Ensure modal is active (it should be, but just in case)
        if (this.flightDetails) {
          this.showFlightDetailsModal = true;
        }

      } catch (e) {
        console.error('Assignment error:', e);
        this.showError(e.message || 'Failed to assign employees. Check server status and API URL.');
      } finally {
        this.loadingAssignment = false;
      }
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      try {
        const date = new Date(dateString);
        return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
      } catch (e) {
        return dateString; // Return original string if parsing fails
      }
    },

    getRoleBadge(title) {
      const badges = {
        'Pilot': 'bg-blue-100 text-blue-800',
        'FlightAttendant': 'bg-green-100 text-green-800',
        'Manager': 'bg-yellow-100 text-yellow-800'
      };
      return badges[title] || 'bg-gray-100 text-gray-800';
    },

    getStatusBadge(status) {
      if (!status) return 'bg-gray-100 text-gray-800';
      const lowerStatus = status.toLowerCase();
      if (lowerStatus.includes('cancelled')) return 'bg-red-100 text-red-800';
      if (lowerStatus.includes('delayed')) return 'bg-yellow-100 text-yellow-800';
      if (lowerStatus.includes('ontime')) return 'bg-green-100 text-green-800';
      return 'bg-gray-100 text-gray-800';
    },

    showSuccess(message) {
      this.successMessage = message;
      setTimeout(() => this.successMessage = '', 3000);
    },

    showError(message) {
      this.errorMessage = message;
      setTimeout(() => this.errorMessage = '', 5000);
    }
  }
}
</script>

<style>
/* Generated */
/* Simple CSS for animations */
  @keyframes fadeInRight {
    from { opacity: 0; transform: translateX(20px); }
    to { opacity: 1; transform: translateX(0); }
  }

  @keyframes fadeInDown {
    from { opacity: 0; transform: translateY(-20px); }
    to { opacity: 1; transform: translateY(0); }
  }

  .animate-fade-in-right {
    animation: fadeInRight 0.3s ease-out forwards;
  }

  .animate-fade-in-down {
    animation: fadeInDown 0.3s ease-out forwards;
  }
</style>