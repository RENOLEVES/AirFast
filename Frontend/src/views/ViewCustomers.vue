<template>
  <div class=" p-4 h-full flex flex-col">

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 flex-grow overflow-y-auto pr-4 -mr-4 ">

      <div
          v-for="customer in customers"
          :key="customer.id"
          class="customer-card bg-white p-6 rounded-xl shadow-lg border-t-4 border-indigo-400 hover:shadow-2xl transition duration-300 transform hover:-translate-y-1 h-[200px]"
      >
        <h3 class="text-2xl font-bold text-zinc-800 mb-1">{{ customer.name }}</h3>

        <p class="text-sm font-semibold text-indigo-600 uppercase mb-4 tracking-wider">{{ customer.role }}</p>

        <div class="space-y-3 border-t pt-3">
          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
            <p class="text-md text-gray-700 font-medium">{{ customer.firstName }} {{ customer.lastName }}</p>
          </div>
          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
            </svg>
            <p class="text-md text-gray-700 break-words font-medium min-w-0">{{ customer.email }}</p>
          </div>
          <div class="flex items-center space-x-2">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.863 4.253A8.961 8.961 0 0112 3c4.418 0 8 3.582 8 8 0 1.017-.215 1.981-.663 2.853l-2.54 2.54a4.01 4.01 0 01-3.693 1.157A8.961 8.961 0 0112 21c-4.418 0-8-3.582-8-8 0-1.017.215-1.981.663-2.853l2.54-2.54A4.01 4.01 0 019 5.41a8.961 8.961 0 012.863-1.157z" />
            </svg>
            <p class="text-md text-gray-700 font-medium"><span class="font-semibold text-gray-500 text-sm">Membership Code:</span>   {{ customer.membershipNumber}}</p>
          </div>

        </div>
      </div>
    </div>

    <div class="text-center py-6 mt-8 text-gray-500 border-t pt-6">
      Showing {{ customers.length }} results
    </div>
  </div>
</template>

<script>
export default {
  name: 'ViewCustomers',
  data() {
    return {
      customers: [],
      loading: false,
      error: null,
      apiUrl: 'http://localhost:8080/api/owners/view/customer',
    };
  },
  methods: {
    async fetchCustomers() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const data = await response.json();

        this.customers = data;

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },
  },

  mounted() {
    this.fetchCustomers();
  },
};
</script>