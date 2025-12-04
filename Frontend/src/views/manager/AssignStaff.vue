<template>
  <div class="assign-staff h-full flex flex-col">
    <h1 class="text-3xl font-bold text-gray-800 mb-6">Assign Staff to Flights</h1>

    <!-- Flight Selection -->
    <div class="mb-6 bg-white p-6 rounded-lg shadow-md">
      <label class="block text-sm font-medium text-gray-700 mb-2">Select Flight</label>
      <select v-model="selectedFlightId" @change="loadFlightDetails" class="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 text-lg">
        <option value="">-- Choose a Flight --</option>
        <option v-for="flight in flights" :key="flight.flightId" :value="flight.flightId">
          {{ flight.flightNumber }} - {{ flight.departLocation }} → {{ flight.arrivalLocation }} ({{ formatDate(flight.departTime) }})
        </option>
      </select>
    </div>

    <!-- Loading States -->
    <div v-if="loadingFlights" class="flex-grow flex items-center justify-center text-indigo-600">
      Loading flights...
    </div>
    <div v-else-if="flightError" class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-6 rounded-lg">
      <h3 class="text-red-600 font-bold mb-2">Error Loading Flights</h3>
      <p class="text-red-500">{{ flightError }}</p>
    </div>

    <!-- Employee Selection Grid -->
    <div v-else-if="selectedFlightId" class="flex-grow overflow-hidden flex flex-col">
      <div class="mb-4 flex justify-between items-center">
        <div>
          <h2 class="text-xl font-bold text-gray-800">Available Employees</h2>
          <p class="text-sm text-gray-600">Select employees to assign to this flight</p>
        </div>
        <div class="flex items-center space-x-4">
          <input v-model="searchQuery" type="text" placeholder="Search employees..." class="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
          <button @click="assignSelected" :disabled="selectedEmployeeIds.length === 0" class="px-6 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition disabled:bg-gray-400 disabled:cursor-not-allowed">
            <i class="fas fa-check mr-2"></i>Assign Selected ({{ selectedEmployeeIds.length }})
          </button>
        </div>
      </div>

      <div v-if="loadingEmployees" class="flex-grow flex items-center justify-center text-indigo-600">
        Loading employees...
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 overflow-y-auto flex-grow">
        <div v-for="emp in filteredEmployees" :key="emp.id" 
             :class="['bg-white rounded-lg shadow-md p-4 border-2 transition cursor-pointer', 
                      selectedEmployeeIds.includes(emp.id) ? 'border-green-500 bg-green-50' : 'border-gray-200 hover:border-indigo-300']"
             @click="toggleEmployee(emp.id)">
          <div class="flex items-start justify-between">
            <div class="flex-grow">
              <h3 class="text-lg font-bold text-gray-800">{{ emp.firstName }} {{ emp.lastName }}</h3>
              <p class="text-sm text-gray-600">{{ emp.email }}</p>
              <span :class="getRoleBadge(emp.title)" class="inline-block px-2 py-1 text-xs rounded-full font-semibold mt-2">
                {{ emp.title }}
              </span>
            </div>
            <div class="ml-2">
              <input type="checkbox" :checked="selectedEmployeeIds.includes(emp.id)" class="w-5 h-5 text-indigo-600 rounded focus:ring-2 focus:ring-indigo-500">
            </div>
          </div>
          <div class="mt-2 text-xs text-gray-500">
            ID: {{ emp.id }}
          </div>
        </div>
      </div>
    </div>

    <!-- Empty State -->
    <div v-else class="flex-grow flex items-center justify-center text-gray-400">
      <div class="text-center">
        <i class="fas fa-plane-departure text-6xl mb-4"></i>
        <p class="text-lg">Select a flight to assign employees</p>
      </div>
    </div>

    <!-- Messages -->
    <div v-if="successMessage" class="fixed bottom-4 right-4 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg">
      {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="fixed bottom-4 right-4 bg-red-500 text-white px-6 py-3 rounded-lg shadow-lg">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { managerAPI } from '@/api/service'

export default {
  name: 'AssignStaff',
  data() {
    return {
      flights: [],
      employees: [],
      selectedFlightId: '',
      selectedEmployeeIds: [],
      searchQuery: '',
      loadingFlights: true,
      loadingEmployees: false,
      flightError: null,
      successMessage: '',
      errorMessage: ''
    }
  },
  computed: {
    filteredEmployees() {
      if (!this.searchQuery) return this.employees;
      const query = this.searchQuery.toLowerCase();
      return this.employees.filter(emp =>
        emp.firstName.toLowerCase().includes(query) ||
        emp.lastName.toLowerCase().includes(query) ||
        emp.email.toLowerCase().includes(query) ||
        emp.title.toLowerCase().includes(query)
      );
    }
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
        this.flights = await managerAPI.getAllFlights();
      } catch (e) {
        this.flightError = e.response?.data?.message || e.message || 'Failed to load flights';
      } finally {
        this.loadingFlights = false;
      }
    },

    async fetchEmployees() {
      this.loadingEmployees = true;
      try {
        this.employees = await managerAPI.getAllEmployees();
        // Filter to only pilots and flight attendants
        this.employees = this.employees.filter(emp => 
          emp.title === 'Pilot' || emp.title === 'Flight Attendant'
        );
      } catch (e) {
        console.error('Failed to load employees:', e);
      } finally {
        this.loadingEmployees = false;
      }
    },

    loadFlightDetails() {
      this.selectedEmployeeIds = [];
      this.searchQuery = '';
    },

    toggleEmployee(empId) {
      const index = this.selectedEmployeeIds.indexOf(empId);
      if (index > -1) {
        this.selectedEmployeeIds.splice(index, 1);
      } else {
        this.selectedEmployeeIds.push(empId);
      }
    },

    async assignSelected() {
      if (this.selectedEmployeeIds.length === 0) {
        this.showError('Please select at least one employee');
        return;
      }

      try {
        await managerAPI.assignFlight(this.selectedFlightId, this.selectedEmployeeIds);
        const flight = this.flights.find(f => f.flightId == this.selectedFlightId);
        this.showSuccess(`Successfully assigned ${this.selectedEmployeeIds.length} employee(s) to flight ${flight?.flightNumber}`);
        this.selectedEmployeeIds = [];
      } catch (e) {
        this.showError(e.response?.data?.message || 'Failed to assign employees');
      }
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    },

    getRoleBadge(title) {
      const badges = {
        'Pilot': 'bg-blue-100 text-blue-800',
        'Flight Attendant': 'bg-green-100 text-green-800'
      };
      return badges[title] || 'bg-gray-100 text-gray-800';
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