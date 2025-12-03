<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-xl transition-all duration-300 overflow-hidden">
    <!-- Flight Header with gradient -->
    <div class="bg-gradient-to-br from-blue-500 to-indigo-600 h-[200px] rounded-t-xl relative flex items-center justify-center">
      <div class="text-center text-white">
        <i class="fas fa-plane text-6xl mb-4 opacity-90"></i>
        <p class="font-bold text-xl">{{ flight.class }} Class</p>
      </div>
      <div class="absolute top-4 right-4 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full">
        <p class="text-white font-bold text-sm">{{ flight.remainingSeats }} seats left</p>
      </div>
    </div>

    <!-- Flight Details -->
    <div class="p-6 space-y-4">
      <!-- Route -->
      <div class="text-center pb-4 border-b border-gray-100">
        <p class="font-bold text-2xl text-[#484848] mb-2">
          {{ flight.route }}
        </p>
        <p class="text-sm text-[#9a9a9a]">
          <i class="far fa-calendar mr-2"></i>{{ flight.dateRange }}
        </p>
      </div>

      <!-- Flight Times -->
      <div class="flex items-center justify-between">
        <div class="text-center flex-1">
          <p class="text-xs text-[#9a9a9a] mb-1">Departure</p>
          <p class="font-bold text-lg text-[#484848]">{{ flight.departureTime }}</p>
          <p class="text-sm text-[#9a9a9a]">{{ flight.departureCity }}</p>
        </div>
        
        <div class="flex-1 flex justify-center">
          <i class="fas fa-arrow-right text-2xl text-blue-500"></i>
        </div>
        
        <div class="text-center flex-1">
          <p class="text-xs text-[#9a9a9a] mb-1">Arrival</p>
          <p class="font-bold text-lg text-[#484848]">{{ flight.arrivalTime }}</p>
          <p class="text-sm text-[#9a9a9a]">{{ flight.arrivalCity }}</p>
        </div>
      </div>

      <!-- Price and Book Button -->
      <div class="pt-4 border-t border-gray-100">
        <div class="flex items-center justify-between mb-4">
          <p class="font-bold text-3xl text-blue-600">
            {{ flight.price }}
          </p>
        </div>
        
        <button
          @click="$emit('book', flight)"
          class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white font-bold text-[15px] px-8 py-4 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition-all duration-300 shadow-md hover:shadow-lg"
        >
          <i class="fas fa-ticket-alt mr-2"></i>Book Flight
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  flight: {
    type: Object,
    required: true
  }
})

defineEmits(['book'])
</script>
