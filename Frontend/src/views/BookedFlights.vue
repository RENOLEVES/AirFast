<template>
  <div class="bg-white relative min-h-screen p-8">
    <!-- Header -->
    <div class="flex justify-between items-center mb-12">
      <p class="font-extrabold text-[38px] text-[#484848]">
        Booked Flights
      </p>
      <button
        @click="$emit('navigate', 'HomePage')"
        class="px-6 py-2 bg-[#484848] text-white rounded-full hover:bg-opacity-90 transition font-semibold"
      >
        Back to Home
      </button>
    </div>

    <!-- Booked Flights Grid -->
    <div v-if="bookedFlights.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <div
        v-for="flight in bookedFlights"
        :key="flight.id"
        class="overflow-clip"
      >
        <div class="bg-[#e0e2e6] h-[340px] rounded-[12px] mb-4"></div>
        
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
            @click="cancelFlight(flight.id)"
            class="w-full bg-[#9a9a9a] text-white font-bold text-[15px] px-8 py-3 rounded-lg hover:bg-red-600 transition mt-4"
          >
            Cancel
          </button>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-20">
      <p class="text-[24px] text-[#9a9a9a] mb-8">
        You have no booked flights yet.
      </p>
      <button
        @click="$emit('navigate', 'FlightBooking')"
        class="px-8 py-4 bg-[#484848] text-white rounded-full hover:bg-opacity-90 transition font-bold text-[15px]"
      >
        Book a Flight
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

defineEmits(['navigate'])

const bookedFlights = ref([
  {
    id: 1,
    route: 'Montreal to Toronto',
    departureCity: 'Montreal',
    departureTime: '8:30AM',
    arrivalCity: 'Toronto',
    arrivalTime: '9PM',
    remainingSeats: 10,
    class: 'ECONOMY',
    price: '$ 1000 CAD',
    dateRange: '2025-11-22 to 2025-11-30'
  },
  {
    id: 2,
    route: 'Montreal to Toronto',
    departureCity: 'Montreal',
    departureTime: '8:30AM',
    arrivalCity: 'Toronto',
    arrivalTime: '9PM',
    remainingSeats: 10,
    class: 'BUSINESS',
    price: '$ 1000 CAD',
    dateRange: '2025-11-22 to 2025-11-30'
  }
])

const cancelFlight = (flightId) => {
  if (confirm('Are you sure you want to cancel this flight?')) {
    bookedFlights.value = bookedFlights.value.filter(f => f.id !== flightId)
  }
}
</script>

