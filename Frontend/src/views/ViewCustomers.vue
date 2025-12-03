<template>
  <div class="view-all-customers p-4 h-full flex flex-col">

    <div class="customer-header flex items-center bg-gray-100 p-3 rounded-t-lg shadow-sm border-b border-gray-300 font-semibold text-gray-600 sticky top-0 z-10">
      <div class="column w-1/12">ID</div>
      <div class="column w-2/12">Name</div>
      <div class="column w-2/12">Membership</div>
      <div class="column w-3/12">Email</div>
      <div class="column w-1/12">Role</div>
      <div class="column w-3/12">Activity</div> </div>

    <div class="customer-list overflow-y-auto flex-grow rounded-b-lg border-x border-b border-gray-200">
      <div
          v-for="customer in customers"
          :key="customer.id"
          class="customer-row flex items-center border-t border-gray-200 bg-white hover:bg-indigo-50 transition-colors duration-150 cursor-pointer"
      >
        <div class="column w-1/12 font-bold text-lg text-indigo-700">
          {{ customer.id }}
        </div>

        <div class="column w-2/12 font-medium text-gray-800">
          {{ customer.firstName }} {{ customer.lastName }}
        </div>

        <div class="column w-2/12 text-sm text-gray-700">
          {{ customer.membershipNumber }}
        </div>

        <div class="column w-3/12 text-sm text-gray-500 min-w-0 break-words">
          {{ customer.email }}
        </div>

        <div class="column w-1/12">
          <span :class="getRoleClasses(customer.role)" class="px-3 py-1 text-xs leading-5 font-semibold rounded-full">
            {{ formatRole(customer.role) }}
          </span>
        </div>

        <div class="column w-3/12 text-xs text-gray-700 space-y-0.5">
          <div class="flex justify-between">
            <span class="text-gray-500">Points:</span>
            <span class="font-semibold">{{ customer.points }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-500">Flight:</span>
            <span class="font-semibold">{{ formatTimeInFlight(customer.timeInFlight) }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-500">Bookings:</span>
            <span class="font-semibold">{{ customer.totalBookings }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="text-center py-6 mt-4 text-gray-500 border-t pt-6">
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

        this.customers = data.map(cust => ({
          ...cust,
          role: cust.role || (cust.id % 3 === 0 ? 'GOLD' : (cust.id % 2 === 0 ? 'SILVER' : 'BRONZE')),
          membershipNumber: cust.membershipNumber || `M${String(cust.id).padStart(5, '0')}`
        }));

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },

    formatRole(role) {
      if (!role) return 'N/A';
      const s = role.toLowerCase().replace(/_/g, ' ');
      return s.charAt(0).toUpperCase() + s.slice(1);
    },

    formatTimeInFlight(minutes) {
      if (typeof minutes !== 'number' || minutes < 0) return '0 hrs';
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      if (hours > 0) {
        return `${hours} hrs ${mins} min`;
      }
      return `${mins} min`;
    },

    formatTimeInFLight(timeInFLight) {
      if (!timeInFLight) return 'N/A';
      return this.formatTimeInFlight(timeInFLight);
    },

    getRoleClasses(role) {
      const r = role ? role.toUpperCase() : '';
      switch (r) {
        case 'GOLD':
          return 'bg-yellow-100 text-yellow-800';
        case 'SILVER':
          return 'bg-gray-200 text-gray-800';
        case 'BRONZE':
          return 'bg-amber-100 text-amber-800';
        default:
          return 'bg-blue-100 text-blue-800';
      }
    },

    viewCustomerDetails(customer) {
      console.log('Viewing details for:', customer.id);
    }
  },
  mounted() {
    this.fetchCustomers();
  },
};
</script>

<style scoped>
.customer-row {
  padding: 12px 16px;
  min-height: 50px;
  width: 100%;
}

.customer-header {
  padding: 12px 16px;
}

.column {
  padding: 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>