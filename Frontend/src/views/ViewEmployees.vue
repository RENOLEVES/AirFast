<template>
  <div class="p-4 sm:p-8">
    <h1 class="text-3xl font-extrabold text-zinc-700 mb-8">All Employees</h1>

    <!-- Loading State -->
    <div v-if="loading" class="text-center py-12">
      <div class="animate-spin rounded-full h-12 w-12 border-4 border-indigo-500 border-t-transparent mx-auto mb-4"></div>
      <p class="text-lg text-indigo-500 font-medium">Loading employee data...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="text-center py-12 bg-red-100 border-l-4 border-red-500 p-4 rounded-lg">
      <p class="text-xl font-semibold text-red-700 mb-2">API Connection Error</p>
      <p class="text-red-500 font-mono text-sm">Could not fetch from {{ apiUrl }}.</p>
      <p class="text-sm mt-2">Detail: {{ error }}</p>
      <p class="text-xs mt-4 italic text-gray-500">Showing fallback data.</p>
    </div>

    <!-- Employees List -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">

      <!-- Employee Card Loop -->
      <div
          v-for="employee in employees"
          :key="employee.id"
          class="customer-card bg-white p-6 rounded-xl shadow-lg border-t-4 border-indigo-700 hover:shadow-2xl transition duration-300 transform hover:-translate-y-1"
      >
        <!-- Name -->
        <h3 class="text-2xl font-bold text-zinc-800 mb-1">{{ employee.name }}</h3>

        <!-- Role/Type -->
        <p class="text-sm font-semibold text-indigo-800 uppercase mb-4 tracking-wider">{{ employee.role }}</p>

        <!-- Details -->
        <div class="space-y-3 border-t pt-3">

          <!-- Employee ID -->
          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H5a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
            <p class="text-md text-gray-700 font-medium">
              <span class="font-semibold text-gray-500">Employee ID:</span> {{ employee.employeeId }}
            </p>
          </div>

          <!-- Email -->
          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
            </svg>
            <p class="text-md text-gray-700 break-words font-medium">{{ employee.email }}</p>
          </div>

          <!-- Phone -->
          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-11a2 2 0 01-2-2v-10z" />
            </svg>
            <p class="text-md text-gray-700 font-medium">{{ employee.phone }}</p>
          </div>
        </div>

        <!-- Action Button -->
        <button class="mt-6 w-full py-2 bg-indigo-700 text-white font-bold rounded-lg hover:bg-indigo-800 transition duration-150 shadow-md">
          View Profile
        </button>
      </div>
    </div>

    <!-- Pagination Placeholder -->
    <div class="text-center py-6 mt-8 text-gray-500 border-t pt-6">
      Showing {{ employees.length }} results.
    </div>
  </div>
</template>

<script>
export default {
  name: 'ViewEmployees',
  data() {
    return {
      employees: [],
      loading: false,
      error: null,
      // API endpoint for fetching employees
      apiUrl: 'http://localhost:8080/api/employees',
    };
  },
  methods: {
    /**
     * Fetches employee data from the local backend API endpoint.
     */
    async fetchEmployees() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const data = await response.json();

        this.employees = data;

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
        // Fallback to demo data including employee details
        this.employees = this.getDemoEmployees();
      } finally {
        this.loading = false;
      }
    },

    // Fallback/Demo data method with employee details added
    getDemoEmployees() {
      return [
        { id: 201, employeeId: 'EMP001', name: 'James T. Kirk', email: 'kirk@starfleet.com', phone: '555-001-123', role: 'Pilot/Captain' },
        { id: 202, employeeId: 'EMP002', name: 'Leonard McCoy', email: 'bones@starfleet.com', phone: '555-002-456', role: 'Doctor' },
        { id: 203, employeeId: 'EMP003', name: 'Montgomery Scott', email: 'scotty@starfleet.com', phone: '555-003-789', role: 'Engineer' },
        { id: 204, employeeId: 'EMP004', name: 'Nyota Uhura', email: 'uhura@starfleet.com', phone: '555-004-012', role: 'Comms Officer' },
      ];
    }
  },
  // Fetch data when the component is first loaded
  mounted() {
    this.fetchEmployees();
  },
};
</script>