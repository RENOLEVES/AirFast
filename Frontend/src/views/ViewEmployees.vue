<template>
  <div class="p-4 sm:p-8">

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">

      <div
          v-for="employee in employees"
          :key="employee.id"
          class="customer-card bg-white p-6 rounded-xl shadow-lg border-t-4 border-indigo-700 hover:shadow-2xl transition duration-300 transform hover:-translate-y-1"
      >
        <h3 class="text-2xl font-bold text-zinc-800 mb-1">{{ employee.firstName }} {{ employee.lastName }}</h3>

        <p class="text-sm font-semibold text-indigo-800 uppercase mb-4 tracking-wider"></p>

        <div class="space-y-3 border-t pt-3">

          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H5a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
            </svg>
            <p class="text-md text-gray-700 font-medium">
              <span class="font-semibold text-gray-500 text-sm">Employee ID:</span> {{ employee.id }}
            </p>
          </div>

          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
            </svg>
            <p class="text-md text-gray-700 break-words font-medium min-w-0">{{ employee.email }}</p>
          </div>

          <div class="flex items-center space-x-2">
            <svg fill="#000000" width="18px" height="18px" viewBox="0 0 24 24" id="medal" data-name="Line Color" xmlns="http://www.w3.org/2000/svg"
                 class="icon line-color"><polygon id="secondary" points="11.38 13.4 10 13.59 11 14.48 10.76 15.75 12 15.15 13.24 15.75 13 14.48 14 13.59 12.62 13.4 12 12.25 11.38 13.4"
                 style="fill: none; stroke: rgb(44, 169, 188); stroke-linecap: round; stroke-linejoin: round; stroke-width: 2;"></polygon>
              <polyline id="primary" points="15.57 9.06 18 3 14 3 12.04 7.91" style="fill: none; stroke: rgb(0, 0, 0); stroke-linecap: round; stroke-linejoin: round; stroke-width: 2;"></polyline>
              <polyline id="primary-2" data-name="primary" points="11.96 7.91 10 3 6 3 8.43 9.06" style="fill: none; stroke: rgb(0, 0, 0); stroke-linecap: round; stroke-linejoin: round; stroke-width: 2;"></polyline>
              <circle id="primary-3" data-name="primary" cx="12" cy="14" r="6" style="fill: none; stroke: rgb(0, 0, 0); stroke-linecap: round; stroke-linejoin: round; stroke-width: 2;"></circle>
            </svg>
            <p class="text-md text-gray-700 font-medium">{{ employee.title }}</p>
          </div>
        </div>
      </div>
    </div>

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
      apiUrl: 'http://localhost:8080/api/owners/view/employee',
    };
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

        this.employees = data;

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },
  },
  mounted() {
    this.fetchEmployees();
  },
};
</script>