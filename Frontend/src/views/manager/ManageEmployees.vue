<template>
  <div class="manage-employees h-full flex flex-col">
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-3xl font-bold text-gray-800">Manage Employees</h1>
      <button @click="openCreateModal" class="px-6 py-3 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
        <i class="fas fa-plus mr-2"></i>Create Employee
      </button>
    </div>

    <!-- Loading/Error States -->
    <div v-if="loading" class="flex-grow flex items-center justify-center text-indigo-600">
      Loading employees...
    </div>
    <div v-else-if="error" class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-6 rounded-lg">
      <h3 class="text-red-600 font-bold mb-2">Error Loading Employees</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchEmployees" class="mt-4 px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700">
        Try Again
      </button>
    </div>

    <!-- Employees Grid -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 overflow-y-auto flex-grow">
      <div v-for="emp in employees" :key="emp.id" class="bg-white rounded-lg shadow-md p-6 border border-gray-200 hover:shadow-lg transition">
        <div class="flex items-start justify-between mb-4">
          <div>
            <h3 class="text-xl font-bold text-gray-800">{{ emp.firstName }} {{ emp.lastName }}</h3>
            <p class="text-sm text-gray-500">{{ emp.email }}</p>
          </div>
          <span :class="getRoleBadge(emp.title)" class="px-3 py-1 text-xs rounded-full font-semibold">
            {{ emp.title }}
          </span>
        </div>

        <div class="space-y-2 text-sm">
          <div class="flex justify-between">
            <span class="text-gray-600">ID:</span>
            <span class="font-medium">{{ emp.id }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Salary:</span>
            <span class="font-medium">${{ emp.salary?.toLocaleString() || 'N/A' }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Status:</span>
            <span :class="emp.isActive ? 'text-green-600' : 'text-red-600'" class="font-medium">
              {{ emp.isActive ? 'Active' : 'Inactive' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- Create Employee Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.self="closeModal">
      <div class="bg-white rounded-xl p-8 max-w-md w-full mx-4">
        <h2 class="text-2xl font-bold mb-6">Create Employee</h2>
        
        <form @submit.prevent="createEmployee" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">First Name</label>
            <input v-model="formData.firstName" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
            <input v-model="formData.lastName" type="text" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Email</label>
            <input v-model="formData.email" type="email" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input v-model="formData.password" type="password" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Employee Type</label>
            <select v-model="formData.type" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
              <option value="">Select Type</option>
              <option value="Pilot">Pilot</option>
              <option value="FlightAttendant">Flight Attendant</option>
            </select>
          </div>

          <div class="flex space-x-3 pt-4">
            <button type="submit" class="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
              Create Employee
            </button>
            <button type="button" @click="closeModal" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition">
              Cancel
            </button>
          </div>
        </form>
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
  name: 'ManageEmployees',
  data() {
    return {
      employees: [],
      loading: true,
      error: null,
      showModal: false,
      successMessage: '',
      errorMessage: '',
      formData: {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        type: ''
      }
    }
  },
  mounted() {
    this.fetchEmployees();
  },
  methods: {
    async fetchEmployees() {
      this.loading = true;
      this.error = null;
      try {
        this.employees = await managerAPI.getAllEmployees();
      } catch (e) {
        this.error = e.response?.data?.message || e.message || 'Failed to load employees';
      } finally {
        this.loading = false;
      }
    },

    openCreateModal() {
      this.formData = {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        type: ''
      };
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
    },

    async createEmployee() {
      try {
        await managerAPI.createEmployee(this.formData);
        this.showSuccess('Employee created successfully');
        this.closeModal();
        this.fetchEmployees();
      } catch (e) {
        this.showError(e.response?.data?.message || 'Failed to create employee');
      }
    },

    getRoleBadge(title) {
      const badges = {
        'Pilot': 'bg-blue-100 text-blue-800',
        'Flight Attendant': 'bg-green-100 text-green-800',
        'Manager': 'bg-purple-100 text-purple-800'
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