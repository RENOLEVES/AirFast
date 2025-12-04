<template>
  <div class="view-all-customers p-4 h-full flex flex-col">

    <div class="customer-header flex items-center bg-gray-100 p-3 rounded-t-lg shadow-sm border-b border-gray-300 font-semibold text-gray-600 sticky top-0 z-10">
      <div class="column w-1/12">ID</div>
      <div class="column w-2/12">Name</div>
      <div class="column w-2/12">Membership</div>
      <div class="column w-3/12">Email</div>
      <div class="column w-3/12">Activity</div> </div>

    <div class="customer-list overflow-y-auto flex-grow rounded-b-lg border-x border-b border-gray-200">
      <div
          v-if="loading"
          class="p-4 text-center text-gray-500"
      >
        <i class="fas fa-spinner fa-spin mr-2"></i> Loading customer data...
      </div>
      <div
          v-else-if="error"
          class="p-4 text-center text-red-600 bg-red-50 rounded-b-lg"
      >
        <i class="fas fa-exclamation-triangle mr-2"></i> Error: {{ error }}
      </div>
      <div
          v-else-if="customers.length === 0"
          class="p-4 text-center text-gray-500"
      >
        <i class="fas fa-users-slash mr-2"></i> No customers found.
      </div>
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

<script setup>
  import { ref, onMounted } from 'vue';

  const customers = ref([]);
  const loading = ref(false);
  const error = ref(null);
  const apiUrl = 'http://localhost:8080/api/owners/view/customer';

  const fetchCustomers = async () => {
    loading.value = true;
    error.value = null;

    try {
      const response = await fetch(apiUrl);

      if (!response.ok) {
        throw new Error(`Server returned status: ${response.status}`);
      }

      const data = await response.json();
      customers.value = data;

      console.log('Customers fetched successfully:', customers.value);

    } catch (e) {
      console.error('Fetch error:', e);
      error.value = e.message || 'The backend service is unavailable.';
      customers.value = [];
    } finally {
      loading.value = false;
    }
  };

  const formatTimeInFlight = (minutes) => {
    if (typeof minutes !== 'number' || minutes < 0) return '0 min';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;

    if (hours > 0) {
      return `${hours} hrs ${mins} min`;
    }
    return `${mins} min`;
  };

  onMounted(() => {
    fetchCustomers();
});
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