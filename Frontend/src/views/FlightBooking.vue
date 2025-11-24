<template>
  <div class="bg-white overflow-clip relative min-h-screen p-8">
    <!-- Header -->
    <div class="flex justify-between items-center mb-8">
      <p class="font-extrabold text-[38px] text-[#484848]">
        Flights
      </p>
      <button
        @click="$emit('navigate', 'HomePage')"
        class="px-6 py-2 bg-[#484848] text-white rounded-full hover:bg-opacity-90 transition font-semibold"
      >
        Back to Home
      </button>
    </div>

    <!-- Search Section -->
    <div class="bg-white border border-[#c2c6cc] rounded-[30px] p-6 mb-12 grid grid-cols-5 gap-4 items-end">
      <div>
        <label class="block font-semibold text-[14px] text-[#484848] mb-2">
          Departure Location
        </label>
        <input
          v-model="searchParams.departureLocation"
          type="text"
          placeholder="Enter city"
          class="w-full px-4 py-3 border border-[#c2c6cc] rounded-lg text-[#9a9a9a] outline-none focus:border-[#484848]"
        />
      </div>

      <div>
        <label class="block font-semibold text-[14px] text-[#484848] mb-2">
          Arrival Location
        </label>
        <input
          v-model="searchParams.arrivalLocation"
          type="text"
          placeholder="Enter city"
          class="w-full px-4 py-3 border border-[#c2c6cc] rounded-lg text-[#9a9a9a] outline-none focus:border-[#484848]"
        />
      </div>

      <div>
        <label class="block font-semibold text-[14px] text-[#484848] mb-2">
          Departure Date
        </label>
        <input
          v-model="searchParams.departureDate"
          type="date"
          class="w-full px-4 py-3 border border-[#c2c6cc] rounded-lg text-[#9a9a9a] outline-none focus:border-[#484848]"
        />
      </div>

      <div>
        <label class="block font-semibold text-[14px] text-[#484848] mb-2">
          Return Date
        </label>
        <input
          v-model="searchParams.returnDate"
          type="date"
          class="w-full px-4 py-3 border border-[#c2c6cc] rounded-lg text-[#9a9a9a] outline-none focus:border-[#484848]"
        />
      </div>

      <button
        @click="searchFlights"
        class="bg-[#484848] text-white font-semibold text-[20px] px-8 py-3 rounded-lg hover:bg-opacity-90 transition"
      >
        Search
      </button>
    </div>

    <!-- No Results Message -->
    <div v-if="filteredFlights.length === 0" class="text-center py-20">
      <p class="text-[24px] text-[#9a9a9a] mb-4">
        No flights found matching your search criteria.
      </p>
      <button
        @click="resetSearch"
        class="px-8 py-3 bg-[#484848] text-white rounded-lg hover:bg-opacity-90 transition font-semibold"
      >
        Show All Flights
      </button>
    </div>

    <!-- Flight Results Grid -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <FlightCard 
        v-for="flight in filteredFlights" 
        :key="flight.id"
        :flight="flight"
        @book="handleBook"
      />
    </div>

    <!-- Pagination -->
    <p class="text-center font-bold text-[16px] text-[#484848] mt-12">
      Paginations or Load on scroll...
    </p>
  </div>
</template>

<script setup>
import { ref, inject, computed } from 'vue'
import FlightCard from '../components/FlightCard.vue'

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
  },
  {
    id: 2,
    route: 'Montreal to Toronto',
    departureCity: 'Montreal',
    departureTime: '2:00PM',
    arrivalCity: 'Toronto',
    arrivalTime: '3:00PM',
    remainingSeats: 5,
    class: 'BUSINESS',
    price: '$ 2000 CAD',
    dateRange: '2025-11-22 to 2025-11-30',
    departureDate: '2025-11-22',
    returnDate: '2025-11-30'
  },
  {
    id: 3,
    route: 'Montreal to Vancouver',
    departureCity: 'Montreal',
    departureTime: '6:00AM',
    arrivalCity: 'Vancouver',
    arrivalTime: '9:30AM',
    remainingSeats: 15,
    class: 'ECONOMY',
    price: '$ 1500 CAD',
    dateRange: '2025-11-23 to 2025-12-01',
    departureDate: '2025-11-23',
    returnDate: '2025-12-01'
  },
  {
    id: 4,
    route: 'Toronto to Montreal',
    departureCity: 'Toronto',
    departureTime: '10:00AM',
    arrivalCity: 'Montreal',
    arrivalTime: '11:00AM',
    remainingSeats: 8,
    class: 'ECONOMY',
    price: '$ 950 CAD',
    dateRange: '2025-11-24 to 2025-12-02',
    departureDate: '2025-11-24',
    returnDate: '2025-12-02'
  },
  {
    id: 5,
    route: 'Toronto to Vancouver',
    departureCity: 'Toronto',
    departureTime: '1:00PM',
    arrivalCity: 'Vancouver',
    arrivalTime: '4:00PM',
    remainingSeats: 20,
    class: 'BUSINESS',
    price: '$ 2500 CAD',
    dateRange: '2025-11-25 to 2025-12-03',
    departureDate: '2025-11-25',
    returnDate: '2025-12-03'
  },
  {
    id: 6,
    route: 'Vancouver to Montreal',
    departureCity: 'Vancouver',
    departureTime: '7:00AM',
    arrivalCity: 'Montreal',
    arrivalTime: '3:00PM',
    remainingSeats: 12,
    class: 'ECONOMY',
    price: '$ 1600 CAD',
    dateRange: '2025-11-26 to 2025-12-04',
    departureDate: '2025-11-26',
    returnDate: '2025-12-04'
  },
  {
    id: 7,
    route: 'Montreal to Toronto',
    departureCity: 'Montreal',
    departureTime: '5:00PM',
    arrivalCity: 'Toronto',
    arrivalTime: '6:00PM',
    remainingSeats: 3,
    class: 'BUSINESS',
    price: '$ 2100 CAD',
    dateRange: '2025-11-27 to 2025-12-05',
    departureDate: '2025-11-27',
    returnDate: '2025-12-05'
  },
  {
    id: 8,
    route: 'Vancouver to Toronto',
    departureCity: 'Vancouver',
    departureTime: '9:00AM',
    arrivalCity: 'Toronto',
    arrivalTime: '5:00PM',
    remainingSeats: 18,
    class: 'ECONOMY',
    price: '$ 1400 CAD',
    dateRange: '2025-11-28 to 2025-12-06',
    departureDate: '2025-11-28',
    returnDate: '2025-12-06'
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

