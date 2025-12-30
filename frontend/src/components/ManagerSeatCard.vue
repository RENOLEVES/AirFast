<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-xl transition-all duration-300 overflow-hidden">
    
    <div class="bg-gray-200 h-[250px] rounded-t-xl relative p-4">
      
      <div class="text-left w-full text-gray-700 font-medium text-lg space-y-1">
        <p>Seat ID: **{{ seat.seatId }}**</p>
        <p>Flight ID: **{{ seat.flightId }}**</p>
        <p>Class: <span :class="{'font-bold': true, 'text-blue-600': seat.class === 'BUSINESS', 'text-gray-600': seat.class === 'ECONOMY'}">{{ seat.class }}</span></p>
        <p>Number: **{{ seat.seatNumber }}**</p>
        <p>Status: 
          <span :class="['font-bold', seat.status === 'AVAILABLE' ? 'text-green-600' : 'text-red-500']">
            {{ seat.status }}
          </span>
        </p>
      </div>

      <div class="absolute bottom-16 left-4 text-[#9A9A9A] text-xl font-bold">
        {{ seat.price }} {{ seat.currency }}
      </div>
      
      <div class="absolute bottom-4 left-4 right-4 text-left">
        <p class="font-bold text-xl text-[#484848]">{{ seat.route }}</p>
        <p class="text-sm text-[#9a9a9a] mt-1">{{ seat.dateRange }}</p>
      </div>

    </div>

    <div class="p-4 bg-gray-50 border-t border-gray-100 flex justify-end">
      <button 
        @click="$emit('set-price', seat.seatId)"
        class="action-btn"
      >
        Set Price
      </button>
    </div>
  </div>
</template>

<script setup>
// Props for the Seat data
defineProps({
  seat: {
    type: Object,
    required: true,
    default: () => ({
      seatId: 'N/A',
      flightId: 'N/A',
      class: 'ECONOMY',
      seatNumber: 'A00',
      status: 'AVAILABLE',
      price: '$ 0',
      currency: 'CAD',
      route: 'City A to City B',
      dateRange: 'YYYY-MM-DD to YYYY-MM-DD',
    })
  }
})

defineEmits(['set-price'])
</script>

<style scoped>
/* Styling for manager buttons, mimicking the original gray buttons (#9A9A9A) */
.action-btn {
  @apply bg-gray-500 text-white font-bold text-sm px-8 py-2 rounded-3xl 
         hover:bg-gray-600 transition-all duration-200 border border-gray-400;
  min-width: 160px;
}
</style>