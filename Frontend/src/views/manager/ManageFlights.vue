<template>
  <div class="manage-flights h-full flex flex-col">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Manage Flights</h1>
      <button @click="openCreateModal" class="px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
        <i class="fas fa-plus mr-2"></i>Create Flight
      </button>
    </div>

    <!-- Loading/Error States -->
    <div v-if="loading" class="flex-grow flex items-center justify-center text-indigo-600">
      Loading flights...
    </div>
    <div v-else-if="error" class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-6 rounded-lg">
      <h3 class="text-red-600 font-bold mb-2">Error Loading Flights</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchFlights" class="mt-4 px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700">
        Try Again
      </button>
    </div>

    <!-- Flights Grid -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 overflow-y-auto flex-grow">
      <div v-for="flight in flights" :key="flight.flightId" class="bg-white rounded-lg shadow-md p-6 border border-gray-200 hover:shadow-lg transition">
        <div class="flex justify-between items-start mb-4">
          <div>
            <h3 class="text-xl font-bold text-gray-800">{{ flight.flightNumber }}</h3>
            <p class="text-sm text-gray-500">{{ formatDate(flight.departTime) }}</p>
          </div>
          <span :class="getStatusBadge(flight.status)" class="px-3 py-1 text-xs rounded-full font-semibold">
            {{ formatStatus(flight.status) }}
          </span>
        </div>

        <div class="space-y-2 mb-4 text-sm">
          <div class="flex justify-between">
            <span class="text-gray-600">Route:</span>
            <span class="font-medium">{{ flight.departLocation }} → {{ flight.arrivalLocation }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Departure:</span>
            <span class="font-medium">{{ formatTime(flight.departTime) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Arrival:</span>
            <span class="font-medium">{{ formatTime(flight.arrivalTime) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Capacity:</span>
            <span class="font-medium">{{ flight.capacity }} ({{ flight.seatsRemaining }} remaining)</span>
          </div>
        </div>

        <div class="flex space-x-2">
          <button @click="openEditModal(flight)" class="flex-1 px-3 py-2 bg-blue-500 text-white text-sm rounded hover:bg-blue-600 transition">
            <i class="fas fa-edit mr-1"></i>Edit
          </button>
          <button @click="openAssignModal(flight)" class="flex-1 px-3 py-2 bg-green-500 text-white text-sm rounded hover:bg-green-600 transition">
            <i class="fas fa-user-plus mr-1"></i>Assign
          </button>
          <button @click="deleteFlight(flight.flightId)" class="px-3 py-2 bg-red-500 text-white text-sm rounded hover:bg-red-600 transition">
            <i class="fas fa-trash"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- Create/Edit Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.self="closeModal">
      <div class="bg-white rounded-xl p-8 max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <h2 class="text-2xl font-bold mb-6">{{ isEditing ? 'Edit Flight' : 'Create Flight' }}</h2>
        
        <form @submit.prevent="saveFlight" class="space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Flight Number</label>
              <input v-model="formData.flightNumber" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Capacity</label>
              <input v-model.number="formData.capacity" type="number" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Departure Location</label>
              <input v-model="formData.departLocation" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Arrival Location</label>
              <input v-model="formData.arrivalLocation" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Departure Time</label>
              <input v-model="formData.departTime" type="datetime-local" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">Flight Time (minutes)</label>
              <input v-model.number="formData.flightTime" type="number" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
            </div>
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
            <select v-model.number="formData.status" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent">
              <option :value="0">On Time</option>
              <option :value="1">Delayed</option>
              <option :value="2">Cancelled</option>
            </select>
          </div>

          <div class="flex items-center">
            <input v-model="formData.isRecurring" type="checkbox" class="mr-2">
            <label class="text-sm font-medium text-gray-700">Recurring Flight</label>
          </div>

          <div class="flex space-x-3 pt-4">
            <button type="submit" class="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
              {{ isEditing ? 'Update' : 'Create' }} Flight
            </button>
            <button type="button" @click="closeModal" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition">
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Assign Employees Modal -->
    <div v-if="showAssignModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.self="closeAssignModal">
      <div class="bg-white rounded-xl p-8 max-w-4xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <h2 class="text-2xl font-bold mb-6">Assign Employees to Flight {{ selectedFlight?.flightNumber }}</h2>
        
        <div class="mb-4">
          <input v-model="employeeSearch" type="text" placeholder="Search employees..." class="w-full px-4 py-2 border border-gray-300 rounded-lg">
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
          <div v-for="emp in filteredEmployees" :key="emp.id" class="border rounded-lg p-4 hover:bg-gray-50 transition">
            <div class="flex items-start justify-between">
              <div>
                <h4 class="font-semibold">{{ emp.firstName }} {{ emp.lastName }}</h4>
                <p class="text-sm text-gray-600">{{ emp.title }}</p>
                <p class="text-xs text-gray-500">{{ emp.email }}</p>
              </div>
              <input v-model="selectedEmployeeIds" :value="emp.id" type="checkbox" class="mt-1">
            </div>
          </div>
        </div>

        <div class="flex space-x-3">
          <button @click="assignEmployees" class="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition">
            Assign Selected ({{ selectedEmployeeIds.length }})
          </button>
          <button @click="closeAssignModal" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition">
            Cancel
          </button>
        </div>
      </div>
    </div>

    <!-- Success/Error Messages -->
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
  name: 'ManageFlights',
  data() {
    return {
      flights: [],
      employees: [],
      loading: true,
      error: null,
      showModal: false,
      showAssignModal: false,
      isEditing: false,
      selectedFlight: null,
      selectedEmployeeIds: [],
      employeeSearch: '',
      successMessage: '',
      errorMessage: '',
      formData: {
        flightNumber: '',
        capacity: 180,
        departLocation: '',
        arrivalLocation: '',
        departTime: '',
        flightTime: 60,
        isRecurring: false,
        status: 0
      }
    }
  },
  computed: {
    filteredEmployees() {
      if (!this.employeeSearch) return this.employees;
      const search = this.employeeSearch.toLowerCase();
      return this.employees.filter(emp => 
        emp.firstName.toLowerCase().includes(search) ||
        emp.lastName.toLowerCase().includes(search) ||
        emp.email.toLowerCase().includes(search) ||
        emp.title.toLowerCase().includes(search)
      );
    }
  },
  mounted() {
    this.fetchFlights();
    this.fetchEmployees();
  },
  methods: {
    async fetchFlights() {
      this.loading = true;
      this.error = null;
      try {
        this.flights = await managerAPI.getAllFlights();
      } catch (e) {
        this.error = e.response?.data?.message || e.message || 'Failed to load flights';
      } finally {
        this.loading = false;
      }
    },

    async fetchEmployees() {
      try {
        this.employees = await managerAPI.getAllEmployees();
      } catch (e) {
        console.error('Failed to load employees:', e);
      }
    },

    openCreateModal() {
      this.isEditing = false;
      this.formData = {
        flightNumber: '',
        capacity: 180,
        departLocation: '',
        arrivalLocation: '',
        departTime: '',
        flightTime: 60,
        isRecurring: false,
        status: 0
      };
      this.showModal = true;
    },

    openEditModal(flight) {
      this.isEditing = true;
      this.selectedFlight = flight;
      this.formData = {
        flightNumber: flight.flightNumber,
        capacity: flight.capacity,
        departLocation: flight.departLocation,
        arrivalLocation: flight.arrivalLocation,
        departTime: this.formatDateForInput(flight.departTime),
        flightTime: flight.flightTime,
        isRecurring: flight.isRecurring,
        status: flight.status || 0
      };
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.selectedFlight = null;
    },

    async saveFlight() {
      try {
        if (this.isEditing) {
          await managerAPI.updateFlight(this.selectedFlight.flightId, this.formData);
          this.showSuccess('Flight updated successfully');
        } else {
          await managerAPI.createFlight(this.formData);
          this.showSuccess('Flight created successfully');
        }
        this.closeModal();
        this.fetchFlights();
      } catch (e) {
        this.showError(e.response?.data?.message || 'Failed to save flight');
      }
    },

    async deleteFlight(flightId) {
      if (!confirm('Are you sure you want to delete this flight?')) return;
      
      try {
        await managerAPI.deleteFlight(flightId);
        this.showSuccess('Flight deleted successfully');
        this.fetchFlights();
      } catch (e) {
        this.showError(e.response?.data?.message || 'Failed to delete flight');
      }
    },

    openAssignModal(flight) {
      this.selectedFlight = flight;
      this.selectedEmployeeIds = [];
      this.employeeSearch = '';
      this.showAssignModal = true;
    },

    closeAssignModal() {
      this.showAssignModal = false;
      this.selectedFlight = null;
      this.selectedEmployeeIds = [];
    },

    async assignEmployees() {
      if (this.selectedEmployeeIds.length === 0) {
        this.showError('Please select at least one employee');
        return;
      }

      try {
        await managerAPI.assignFlight(this.selectedFlight.flightId, this.selectedEmployeeIds);
        this.showSuccess(`Assigned ${this.selectedEmployeeIds.length} employee(s) to flight`);
        this.closeAssignModal();
      } catch (e) {
        this.showError(e.response?.data?.message || 'Failed to assign employees');
      }
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
    },

    formatTime(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
    },

    formatDateForInput(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toISOString().slice(0, 16);
    },

    formatStatus(status) {
      const statuses = ['On Time', 'Delayed', 'Cancelled'];
      return statuses[status] || 'Unknown';
    },

    getStatusBadge(status) {
      const badges = {
        0: 'bg-green-100 text-green-800',
        1: 'bg-yellow-100 text-yellow-800',
        2: 'bg-red-100 text-red-800'
      };
      return badges[status] || 'bg-gray-100 text-gray-800';
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