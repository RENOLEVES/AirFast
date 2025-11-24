<template>
  <div class="overflow-clip">
    <!-- Flight Image Placeholder -->
    <div class="bg-[#e0e2e6] h-[340px] rounded-[12px] relative mb-4">
      <!-- You can add flight/airline images here -->
    </div>

    <!-- Flight Details -->
    <div class="space-y-2">
      <p class="font-semibold text-[18px] text-[#9a9a9a]">
        {{ flight.price }}
      </p>
      
      <div class="font-semibold text-[18px] text-[#9a9a9a] space-y-1">
        <p>Departing from {{ flight.departureCity }}: {{ flight.departureTime }}</p>
        <p>Arriving at {{ flight.arrivalCity }}: {{ flight.arrivalTime }}</p>
        <p>Remaining Seats: {{ flight.remainingSeats }}</p>
        <p>{{ flight.class }} Class</p>
      </div>
      
      <p class="font-bold text-[18px] text-[#484848]">
        {{ flight.route }}
      </p>
      
      <p class="text-[14px] text-[#9a9a9a]">
        {{ flight.dateRange }}
      </p>
      
      <button
        @click="$emit('book', flight)"
        class="w-full bg-[#484848] text-white font-bold text-[15px] px-8 py-3 rounded-lg hover:bg-opacity-90 transition mt-4"
      >
        Book
      </button>
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
