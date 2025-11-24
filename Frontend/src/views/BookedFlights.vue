<template>
  <div class="bg-gray-50 min-h-screen p-8">
    <div class="max-w-7xl mx-auto">
      <!-- Header -->
      <div class="flex justify-between items-center mb-8">
        <div>
          <h1 class="font-extrabold text-[42px] text-[#484848] mb-2">
            <i class="fas fa-ticket-alt text-blue-500 mr-3"></i>My Bookings
          </h1>
          <p class="text-[#9a9a9a]">Manage your flight reservations</p>
        </div>
        <button
          @click="$emit('navigate', 'HomePage')"
          class="px-6 py-3 bg-white border border-gray-300 text-[#484848] rounded-lg hover:bg-gray-50 transition font-semibold shadow-sm"
        >
          <i class="fas fa-home mr-2"></i>Back to Home
        </button>
      </div>

      <!-- Booked Flights Grid -->
      <div v-if="bookedFlights.length > 0">
        <p class="text-[#9a9a9a] mb-6">
          <i class="fas fa-info-circle mr-2"></i>You have {{ bookedFlights.length }} active booking{{ bookedFlights.length > 1 ? 's' : '' }}
        </p>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
            v-for="flight in bookedFlights"
            :key="flight.id"
            class="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-xl transition-all duration-300 overflow-hidden"
          >
            <!-- Flight Header with gradient -->
            <div class="bg-gradient-to-br from-green-500 to-emerald-600 h-[200px] rounded-t-xl relative flex items-center justify-center">
              <div class="text-center text-white">
                <i class="fas fa-check-circle text-6xl mb-4 opacity-90"></i>
                <p class="font-bold text-xl">{{ flight.class }} Class</p>
              </div>
              <div class="absolute top-4 left-4 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full">
                <p class="text-white font-bold text-sm">
                  <i class="fas fa-calendar-check mr-2"></i>Confirmed
                </p>
              </div>
              <div class="absolute top-4 right-4 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full">
                <p class="text-white font-bold text-sm">{{ flight.remainingSeats }} seats</p>
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
                  <i class="fas fa-arrow-right text-2xl text-green-500"></i>
                </div>
                
                <div class="text-center flex-1">
                  <p class="text-xs text-[#9a9a9a] mb-1">Arrival</p>
                  <p class="font-bold text-lg text-[#484848]">{{ flight.arrivalTime }}</p>
                  <p class="text-sm text-[#9a9a9a]">{{ flight.arrivalCity }}</p>
                </div>
              </div>

              <!-- Price and Cancel Button -->
              <div class="pt-4 border-t border-gray-100">
                <div class="flex items-center justify-between mb-4">
                  <p class="font-bold text-2xl text-green-600">
                    {{ flight.price }}
                  </p>
                </div>
                
                <button
                  @click="cancelFlight(flight.id)"
                  class="w-full bg-red-500 text-white font-bold text-[15px] px-8 py-4 rounded-lg hover:bg-red-600 transition-all duration-300 shadow-md hover:shadow-lg"
                >
                  <i class="fas fa-times-circle mr-2"></i>Cancel Booking
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-else class="text-center py-20 bg-white rounded-2xl shadow-md">
        <i class="fas fa-inbox text-6xl text-gray-300 mb-6"></i>
        <p class="text-[28px] font-bold text-[#484848] mb-4">
          No bookings yet
        </p>
        <p class="text-[#9a9a9a] mb-8">
          Start exploring and book your next adventure!
        </p>
        <button
          @click="$emit('navigate', 'FlightBooking')"
          class="px-8 py-4 bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-lg hover:from-blue-600 hover:to-indigo-700 transition font-bold text-[15px] shadow-md hover:shadow-lg"
        >
          <i class="fas fa-search mr-2"></i>Browse Flights
        </button>
      </div>
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
    arrivalTime: '9:30AM',
    remainingSeats: 10,
    class: 'ECONOMY',
    price: '$ 1000 CAD',
    dateRange: '2025-11-22 to 2025-11-30'
  },
  {
    id: 2,
    route: 'Montreal to Toronto',
    departureCity: 'Montreal',
    departureTime: '2:00PM',
    arrivalCity: 'Toronto',
    arrivalTime: '3:00PM',
    remainingSeats: 10,
    class: 'BUSINESS',
    price: '$ 2000 CAD',
    dateRange: '2025-11-22 to 2025-11-30'
  }
])

const cancelFlight = (flightId) => {
  if (confirm('Are you sure you want to cancel this flight booking?')) {
    bookedFlights.value = bookedFlights.value.filter(f => f.id !== flightId)
  }
}
</script>
