<template>
  <div class="view-all-customers p-4 h-full flex flex-col">

    <!-- Loading -->
    <div v-if="loading" class="text-center py-10 text-indigo-500 font-semibold text-lg flex-grow">
      Loading customers...
    </div>

    <!-- Error -->
    <div v-else-if="error" class="text-center py-10 bg-red-50 border border-red-200 p-6 rounded-lg flex-grow">
      <h3 class="text-red-600 font-bold mb-2">Error Fetching Customers</h3>
      <p class="text-red-500">{{ error }}</p>
      <button
        @click="fetchCustomers"
        class="mt-4 px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600"
      >
        Try Again
      </button>
    </div>

    <!-- Data -->
    <div v-else class="flex flex-col flex-grow">

      <div
        class="customer-header flex items-center bg-gray-100 p-3 rounded-t-lg shadow-sm border-b border-gray-300 font-semibold text-gray-600 sticky top-0 z-10">
        <div class="column w-1/12">ID</div>
        <div class="column w-2/12">Name</div>
        <div class="column w-2/12">Membership</div>
        <div class="column w-3/12">Email</div>
        <div class="column w-1/12">Role</div>
        <div class="column w-3/12">Activity</div>
      </div>

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
            <span
              :class="getRoleClasses(customer.role)"
              class="px-3 py-1 text-xs leading-5 font-semibold rounded-full"
            >
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
  </div>
</template>

<script>
import { customerAPI } from '@/api/service'
export default {
  name: 'ViewCustomers',
  data() {
    return {
      customers: [],
      loading: false,
      error: null,
    };
  },
  methods: {
    /**
     * Fetch all customers from the backend using axios API service
     */
    async fetchCustomers() {
      this.loading = true;
      this.error = null;

      try {
        const data = await customerAPI.getAllCustomers();

        // Map backend response to frontend format with fallbacks
        this.customers = (data || []).map(cust => ({
          ...cust,
          role: cust.role || (cust.id % 3 === 0 ? 'GOLD' : (cust.id % 2 === 0 ? 'SILVER' : 'BRONZE')),
          membershipNumber: cust.membershipNumber || `M${String(cust.id).padStart(5, '0')}`,
        }));

      } catch (e) {
        console.error('Fetch error:', e);
        
        // Better error messages based on error type
        if (e.code === 'ERR_NETWORK') {
          this.error = 'Cannot connect to backend server. Make sure it\'s running on http://localhost:8080';
        } else if (e.response) {
          this.error = `Server error: ${e.response.status} - ${e.response.data?.message || 'Unknown error'}`;
        } else {
          this.error = e.message || 'The backend service is unavailable.';
        }
      } finally {
        this.loading = false;
      }
    },

    /**
     * Format role string for display
     */
    formatRole(role) {
      if (!role) return 'N/A';
      const s = role.toLowerCase().replace(/_/g, ' ');
      return s.charAt(0).toUpperCase() + s.slice(1);
    },

    /**
     * Format time in flight from minutes to human-readable format
     */
    formatTimeInFlight(minutes) {
      if (typeof minutes !== 'number' || minutes < 0) return '0 hrs';
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      if (hours > 0) {
        return `${hours} hrs ${mins} min`;
      }
      return `${mins} min`;
    },

    /**
     * Get CSS classes for role badges
     */
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

    /**
     * Handle customer details view (to be implemented)
     */
    viewCustomerDetails(customer) {
      console.log('Viewing details for:', customer.id);
      // TODO: Navigate to customer details page or open modal
      // this.$router.push({ name: 'CustomerDetails', params: { id: customer.id } });
    },
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