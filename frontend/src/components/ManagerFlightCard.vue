<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-xl transition-all duration-300 overflow-hidden">
    <div class="bg-gray-200 h-[200px] rounded-t-xl relative flex items-center justify-center p-4">
      
      <div class="text-left w-full text-gray-700 font-medium text-lg space-y-1">
        <p class="font-extrabold text-[#484848] text-2xl mb-2">{{ flight.flightCode }}</p>
        <p>Departing from {{ flight.departureCity }}: **{{ flight.departureTime }}**</p>
        <p>Arriving at {{ flight.arrivalCity }}: **{{ flight.arrivalTime }}**</p>
        <p>Remaining Seats: {{ flight.remainingSeats }}</p>
        <p>Flight Time: {{ flight.flightTime }}</p>
        <p :class="['font-bold', flight.status === 'Delayed' ? 'text-red-500' : 'text-green-600']">
          {{ flight.status }}
        </p>
      </div>
      
      <div class="absolute bottom-4 left-4 right-4 text-left">
        <p class="font-bold text-xl text-[#484848]">{{ flight.departureCity }} to {{ flight.arrivalCity }}</p>
        <p class="text-sm text-[#9a9a9a] mt-1">{{ flight.dateRange }}</p>
      </div>

    </div>

    <div class="p-4 bg-gray-50 border-t border-gray-100 flex flex-wrap justify-between gap-3">
      
      <button 
        @click="$emit('view-seats', flight.id)"
        class="action-btn"
      >
        View Seats
      </button>

      <button 
        @click="$emit('edit', flight.id)"
        class="action-btn"
      >
        Edit
      </button>

      <button 
        @click="$emit('delete', flight.id)"
        class="action-btn bg-red-600 hover:bg-red-700"
      >
        Delete
      </button>

      <button 
        v-if="flight.canAssignEmployees"
        @click="$emit('assign-employees', flight.id)"
        class="action-btn col-span-2 w-full"
      >
        Assign Employees
      </button>
      
    </div>
  </div>
</template>

<script setup>
// Props match the data structure needed for the manager view
defineProps({
  flight: {
    type: Object,
    required: true,
    default: () => ({
      id: null,
      flightCode: 'N/A',
      departureCity: 'City A',
      departureTime: '00:00AM',
      arrivalCity: 'City B',
      arrivalTime: '00:00PM',
      remainingSeats: 0,
      flightTime: '{x mins}',
      status: 'On Time',
      dateRange: '2025-11-22 to 2025 11-30',
      canAssignEmployees: true, // Flag for the optional button
    })
  }
})

defineEmits(['view-seats', 'edit', 'delete', 'assign-employees'])
</script>

<style scoped>
/* Styling for manager buttons, mimicking the original gray buttons (#9A9A9A) */
.action-btn {
  @apply flex-grow bg-gray-500 text-white font-bold text-sm px-4 py-2 rounded-3xl 
         hover:bg-gray-600 transition-all duration-200 border border-gray-400;
  min-width: 120px;
}
.action-btn:last-child {
  @apply w-full; /* Make assign employees full width */
}
</style>