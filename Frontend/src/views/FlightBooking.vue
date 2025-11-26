<template>
  <div class="bg-gray-50 min-h-screen p-8">
    <div class="max-w-7xl mx-auto">
      <!-- Header -->
      <div class="flex justify-between items-center mb-8">
        <div>

          <h1 class="font-extrabold text-[42px] text-[#484848] mb-2">
            <i class="fas fa-plane text-blue-500 mr-3"></i>Air Fast
          </h1>

          <p class="text-[#9a9a9a]">Search and book your next journey</p>

        </div>

        <!-- signin/signup -->
        <button
          @click="$emit('navigate', 'UserSignin')"
          class="text-[#484848] font-semibold hover:text-blue-600 transition-colors px-6 py-2 rounded-lg hover:bg-white/50 pl-[300px]"
        >
          <i class="fas fa-sign-in-alt mr-2"></i>Sign In
        </button>
        <span class="text-[#9a9a9a]">|</span>
        <button
          @click="$emit('navigate', 'UserSignup')"
          class="text-[#484848] font-semibold hover:text-blue-600 transition-colors px-6 py-2 rounded-lg hover:bg-white/50"
        >
          <i class="fas fa-user-plus mr-2"></i>Sign Up
        </button>
        <button
          @click="$emit('navigate', 'BookedFlights')"
          class="px-6 py-3 bg-white border border-gray-300 text-[#484848] rounded-lg hover:bg-gray-50 transition font-semibold shadow-sm"
        >
          <i class="fas fa-home mr-2"></i>View Bookings
        </button>


      </div>

      <!-- Search Section -->
      <div class="bg-white rounded-2xl shadow-md p-10 mb-12">
        <div class="grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
          <div>
            <label class="block font-semibold text-[14px] text-[#484848] mb-2">
              <i class="fas fa-map-marker-alt mr-2 text-blue-500"></i>Departure Location
            </label>
            <input
              v-model="searchParams.departureLocation"
              type="text"
              placeholder="Enter city"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
            />
          </div>

          <div>
            <label class="block font-semibold text-[14px] text-[#484848] mb-2">
              <i class="fas fa-map-marker-alt mr-2 text-blue-500"></i>Arrival Location
            </label>
            <input
              v-model="searchParams.arrivalLocation"
              type="text"
              placeholder="Enter city"
              class="w-full px-4 py-3 border border-gray-300 rounded-lg text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
            />
          </div>

          <!-- Range Date Picker -->
          <DatePicker class="wide-calendar"/>

          <button
            @click="searchFlights"
            class="bg-gradient-to-r from-blue-500 to-indigo-600 text-white font-bold text-[18px] px-8 py-3 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition-all duration-300 shadow-md hover:shadow-lg"
          >
            <i class="fas fa-search mr-2"></i>Search
          </button>

        </div>
      </div>

      <!-- No Results Message -->
      <div v-if="filteredFlights.length === 0" class="text-center py-20 bg-white rounded-2xl shadow-md">
        <i class="fas fa-plane-slash text-6xl text-gray-300 mb-6"></i>
        <p class="text-[28px] font-bold text-[#484848] mb-4">
          No flights found
        </p>
        <p class="text-[#9a9a9a] mb-8">
          Try adjusting your search criteria
        </p>
        <button
          @click="resetSearch"
          class="px-8 py-3 bg-gradient-to-r from-blue-500 to-indigo-600 text-white rounded-lg hover:from-blue-600 hover:to-indigo-700 transition font-semibold shadow-md"
        >
          <i class="fas fa-redo mr-2"></i>Show All Flights
        </button>
      </div>

      <!-- Flight Results Grid -->
      <div v-else>
        <p class="text-[#9a9a9a] mb-6">
          <i class="fas fa-info-circle mr-2"></i>Found {{ filteredFlights.length }} flight{{ filteredFlights.length > 1 ? 's' : '' }}
        </p>
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <FlightCard 
            v-for="flight in filteredFlights" 
            :key="flight.id"
            :flight="flight"
            @book="handleBook"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, inject, computed } from 'vue'
import FlightCard from '../components/FlightCard.vue'
import DatePicker from '../components/RangeDatePicker.vue'

const emit = defineEmits(['navigate'])
const navigate = inject('navigate')

const searchParams = ref({
  departureLocation: '',
  arrivalLocation: '',
  departureDate: '',
  returnDate: ''
})

const isSearchActive = ref(false)

// All available flights with diverse routes
const flights = ref([
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
    dateRange: '2025-11-22 to 2025-11-30',
    departureDate: '2025-11-22',
    returnDate: '2025-11-30'
  }
])

// Filtered flights based on search criteria
const filteredFlights = computed(() => {
  if (!isSearchActive.value) {
    return flights.value
  }

  return flights.value.filter(flight => {
    const matchesDeparture = !searchParams.value.departureLocation || 
      flight.departureCity.toLowerCase().includes(searchParams.value.departureLocation.toLowerCase())
    
    const matchesArrival = !searchParams.value.arrivalLocation || 
      flight.arrivalCity.toLowerCase().includes(searchParams.value.arrivalLocation.toLowerCase())
    
    const matchesDepartureDate = !searchParams.value.departureDate || 
      flight.departureDate === searchParams.value.departureDate
    
    const matchesReturnDate = !searchParams.value.returnDate || 
      flight.returnDate === searchParams.value.returnDate

    return matchesDeparture && matchesArrival && matchesDepartureDate && matchesReturnDate
  })
})

const searchFlights = () => {
  isSearchActive.value = true
  console.log('Searching flights...', searchParams.value)
  console.log('Found flights:', filteredFlights.value.length)
}

const resetSearch = () => {
  searchParams.value = {
    departureLocation: '',
    arrivalLocation: '',
    departureDate: '',
    returnDate: ''
  }
  isSearchActive.value = false
}

const handleBook = (flight) => {
  console.log('Booking flight:', flight)
  // Navigate to payment page
  if (navigate) {
    navigate('BookingPayment')
  }
}
</script>