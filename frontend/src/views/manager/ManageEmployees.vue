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
            <span class="text-gray-600">Status:</span>
            <span :class="emp.isActive ?  'text-red-600': 'text-green-600'" class="font-medium">
              {{ emp.isActive ? 'Inactive' : 'Active' }}
            </span>
          </div>
        </div>
        <!-- Corrected function calls to use 'emp' instead of 'employee' -->
        <div class="flex space-x-2 mt-4">
          <button @click="openEditModal(emp)" class="h-10 flex-1 px-3 py-2 bg-blue-500 text-white text-base rounded hover:bg-blue-600 transition">
            <i class="fas fa-edit mr-1"></i>Edit
          </button>
          <button @click="deleteEmployee(emp.id)" class="h-10 flex-1 px-3 py-2 bg-red-500 text-white text-base rounded hover:bg-red-600 transition">
            <i class="fas fa-trash"></i> Delete
          </button>
        </div>
      </div>
    </div>

    <!-- Create/Edit Employee Modal -->
    <div v-if="showModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.self="closeModal">
      <div class="bg-white rounded-xl p-8 max-w-md w-full mx-4">
        <h2 class="text-2xl font-bold mb-6">{{ isEditing ? 'Edit Employee' : 'Create Employee' }}</h2>

        <!-- Updated to call handleSaveEmployee which manages POST/PUT -->
        <form @submit.prevent="handleSaveEmployee" class="space-y-4">
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
            <!-- Email not editable during update, or handle PUT logic for email change -->
            <input v-model="formData.email" type="email" required :disabled="isEditing" class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 disabled:bg-gray-100 disabled:text-gray-500">
          </div>

          <div v-if="!isEditing">
            <label class="block text-sm font-medium text-gray-700 mb-1">Password</label>
            <input v-model="formData.password" type="password" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
          </div>
          <div v-else class="text-sm text-gray-500 italic">
            Note: Password is not updated through this form.
          </div>

          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">Employee Title</label>
            <!-- Changed v-model from 'type' to 'title' to match DTO -->
            <select v-model="formData.title" required class="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500">
              <option value="">Select Title</option>
              <option value="Pilot">Pilot</option>
              <option value="Flight Attendant">Flight Attendant</option>
              <option value="Manager">Manager</option>
            </select>
          </div>

          <div class="flex space-x-3 pt-4">
            <button type="submit" class="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition">
              {{ isEditing ? 'Update Employee' : 'Create Employee' }}
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

export default {
  name: 'ManageEmployees',
  data() {
    return {
      employees: [],
      loading: true,
      error: null,
      showModal: false,
      isEditing: false, // Tracks if modal is for editing
      selectedEmployeeId: null, // Stores ID for PUT/DELETE operations
      successMessage: '',
      errorMessage: '',
      formData: {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        title: '' // Corrected from 'type' to 'title'
      },
      apiUrl: 'http://localhost:8080/api/employees',
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
        const response = await fetch(this.apiUrl);

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

    async handleSaveEmployee() {
      try {
        const payload = {
          firstName: this.formData.firstName,
          lastName: this.formData.lastName,
          email: this.formData.email,
          title: this.formData.title,
          ...(this.isEditing ? {} : { password: this.formData.password }),
        };

        let url = this.apiUrl;
        let method = 'POST';

        if (this.isEditing) {
          url = `${this.apiUrl}/${this.selectedEmployeeId}`;
          method = 'PUT';
        }

        const response = await fetch(url, {
          method: method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(payload)
        });

        if (!response.ok) {
          const err = await response.json();
          throw new Error(err.message || `Failed to update seat ${seatId}`);
        }

        this.showSuccess(`Employee ${this.isEditing ? 'updated' : 'created'} successfully.  It may show at the bottom.`);

        this.closeModal();
        await this.fetchEmployees(); // Refresh list

      } catch (err) {
        console.error("Save Employee Error:", e);
        this.showError(e.message || 'Failed to save employee. Check server connection.');
      }
    },

    async deleteEmployee(employeeId) {
      if (!confirm(`Are you sure you want to delete employee #${employeeId}?`)) return;

      const deleteUrl = `${this.apiUrl}/${employeeId}`;

      try {
        const response = await fetch(deleteUrl, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json'
          }
        });

        if (!response.ok) {
          let errorMessage = `Failed to delete employee. Status: ${response.status}.`;

          try {
            const backendData = await response.text();
            try {
              const jsonBody = JSON.parse(backendData);
              errorMessage = jsonBody.message || jsonBody.error || errorMessage;
            } catch {
              errorMessage = backendData || errorMessage;
            }
          } catch (e) {
          }

          throw new Error(errorMessage);
        }

        this.showSuccess(`Employee #${employeeId} deleted successfully.`);
        this.fetchEmployees(); // Refresh the list

      } catch (e) {
        console.error('Delete Employee Error:', e);
        this.showError(e.message.includes('Failed to fetch')
            ? 'Network Error: Failed to connect to the server.'
            : e.message || 'Failed to delete employee.');
      }
    },


    openCreateModal() {
      this.isEditing = false;
      this.selectedEmployeeId = null;
      this.formData = {
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        title: ''
      };
      this.showModal = true;
    },

    openEditModal(employee) {
      this.isEditing = true;
      this.selectedEmployeeId = employee.id;
      this.formData = {
        firstName: employee.firstName,
        lastName: employee.lastName,
        email: employee.email,
        password: '', // Password field is intentionally left empty/unused for PUT
        title: employee.title
      };
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.isEditing = false;
      this.selectedEmployeeId = null;
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