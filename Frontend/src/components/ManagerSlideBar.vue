<template>
  <aside class="w-64 bg-gray-800 text-white flex flex-col p-4 h-full shadow-lg">
    <div class="pt-10 space-y-3">
      <a
          href="#"
          class="block p-3 rounded-lg hover:bg-gray-700 transition duration-150"
          @click.prevent="$emit('navigate', 'customers')"
      >
        View Customers
      </a>
      <a
          href="#"
          class="block p-3 rounded-lg hover:bg-gray-700 transition duration-150"
          @click.prevent="$emit('navigate', 'flights')"
      >
        View Flights
      </a>
      <a
          href="#"
          class="block p-3 rounded-lg hover:bg-gray-700 transition duration-150"
          @click.prevent="$emit('navigate', 'employees')"
      >
        View Employees
      </a>
      <a
          href="#"
          class="block p-3 rounded-lg hover:bg-gray-700 transition duration-150"
          @click.prevent="$emit('navigate', 'bookings')"
      >
        View Bookings
      </a>
      <a
          href="#"
          class="block p-3 rounded-lg hover:bg-gray-700 transition duration-150"
          @click.prevent="$emit('navigate', 'seats')"
      >
        View Seats
      </a>
      <a
          href="#"
          class="block p-3 rounded-lg hover:bg-gray-700 transition duration-150"
          @click.prevent="$emit('navigate', 'dashboard')"
      >
        Dash Board
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