<template>
  <div class="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-xl transition-all duration-300 overflow-hidden">
    <!-- Flight Header with gradient -->
    <div class="bg-gradient-to-br from-blue-500 to-indigo-600 h-[100px] rounded-t-xl relative flex items-center">
      <div class="pl-[50px] text-left text-white">
        <p class="font-bold text-xl">{{ flight.class }} Class</p>
      </div>
      <div class="absolute right-4 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full">
        <p class="text-white font-bold text-sm">{{ flight.remainingSeats }} seats left</p>
      </div>
    </div>

    <div class="p-6 space-y-4">
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
        
        <div class="text-center flex-1">
          <p class="text-xs text-[#9a9a9a] mb-1">Arrival</p>
          <p class="font-bold text-lg text-[#484848]">{{ flight.arrivalTime }}</p>
          <p class="text-sm text-[#9a9a9a]">{{ flight.arrivalCity }}</p>
        </div>
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
