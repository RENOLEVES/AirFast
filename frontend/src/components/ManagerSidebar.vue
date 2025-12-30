<template>
  <aside class="w-64 bg-gray-800 text-white flex flex-col p-4 h-full shadow-lg">
    <div class="pt-10 space-y-3">
      <a href="#" @click.prevent="$emit('navigate', 'flights')" class="block p-3 rounded-lg hover:bg-gray-700 transition">
        <i class="fas fa-plane mr-2"></i>Manage Flights
      </a>
      <a href="#" @click.prevent="$emit('navigate', 'employees')" class="block p-3 rounded-lg hover:bg-gray-700 transition">
        <i class="fas fa-users mr-2"></i>Manage Employees
      </a>
      <a href="#" @click.prevent="$emit('navigate', 'bookings')" class="block p-3 rounded-lg hover:bg-gray-700 transition">
        <i class="fas fa-ticket-alt mr-2"></i>Manage Bookings
      </a>
      <a href="#" @click.prevent="$emit('navigate', 'seats')" class="block p-3 rounded-lg hover:bg-gray-700 transition">
        <i class="fas fa-chair mr-2"></i>Manage Seat Prices
      </a>
      <a href="#" @click.prevent="$emit('navigate', 'assign')" class="block p-3 rounded-lg hover:bg-gray-700 transition">
        <i class="fas fa-user-plus mr-2"></i>Assign Staff to Flights
      </a>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'ManagerSidebar',
  emits: ['navigate']
};
</script>