<<template>
  <div class="bg-gray-50 min-h-screen p-8">
    <div class="max-w-7xl mx-auto">
      <div class="flex justify-between items-center mb-8">
        <div>
          <h1 class="font-extrabold text-[42px] text-[#484848] mb-2">
            <i class="fas fa-plane text-blue-500 mr-3"></i>Air Fast
          </h1>
          <p class="text-[#9a9a9a]">Search and book your next journey</p>
        </div>

        <div class="flex justify-end items-center">
          <div v-if="isLoggedIn" class="relative">
            <button
                @click="toggleDropdown"
                class="flex items-center space-x-2 text-[#484848] font-semibold text-lg p-2 rounded-full hover:bg-gray-200 transition"
            >
              <i class="fas fa-user-circle text-2xl text-blue-600"></i>
              <span class="hidden sm:inline">{{ username }}</span>
              <i class="fas" :class="isDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </button>

            <div
                v-if="isDropdownOpen"
                class="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-lg shadow-xl z-10 overflow-hidden"
            >
              <div class="px-4 py-3 border-b border-gray-100">
                <p class="text-sm text-gray-700">Welcome, {{ username }}</p>
                <p class="text-xs font-medium text-blue-600">Points: {{ userPoints }}</p>
              </div>

              <button
                  @click="handleProfileClick"
                  class="w-full text-left px-4 py-3 text-[#484848] hover:bg-gray-50 transition block font-medium"
              >
                <i class="fas fa-id-badge mr-2 text-blue-500"></i>My Profile
              </button>

              <button
                  @click="handleViewBookingsClick" class="w-full text-left px-4 py-3 text-[#484848] hover:bg-gray-50 transition block font-medium">
                <i class="fas fa-id-badge mr-2 text-blue-500"></i>View Bookings
              </button>

              <button
                  @click="logOutUser"
                  class="w-full text-left px-4 py-3 text-red-500 hover:bg-red-50 transition block font-medium border-t border-gray-100"
              >
                <i class="fas fa-sign-out-alt mr-2"></i>Sign Out
              </button>
            </div>
          </div>

          <div v-else class="flex items-center">
            <button
                @click="$emit('navigate', 'UserSignin')"
                class="text-[#484848] font-semibold hover:text-blue-600 transition-colors px-3 py-2 rounded-lg hover:bg-white/50"
            >
              <i class="fas fa-sign-in-alt mr-2"></i>Sign In
            </button>
            <span class="text-[#9a9a9a] px-2">|</span>
            <button
                @click="$emit('navigate', 'UserSignup')"
                class="text-[#484848] font-semibold hover:text-blue-600 transition-colors px-3 py-2 rounded-lg hover:bg-white/50"
            >
              <i class="fas fa-user-plus mr-2"></i>Sign Up
            </button>
          </div>
        </div>
      </div>

      <!-- Search Section -->
      <div class="bg-white rounded-2xl shadow-md p-10 mb-12">
<!--        <div class="pb-[10px] flex justify-content-center ">-->
<!--          <SelectButton-->
<!--              v-model="value"-->
<!--              :options="options"-->
<!--              aria-labelledby="basic"-->
<!--              @change="handleChange"-->
<!--          />-->
<!--        </div>-->
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
          <DatePicker ref="datePickerRef" class="wide-calendar"/>

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
import { ref, inject, computed, onMounted } from 'vue'
import FlightCard from '../components/FlightCard.vue'
import DatePicker from '../components/RangeDatePicker.vue'
import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});


const emit = defineEmits(['navigate'])
const navigate = inject('navigate')

const searchParams = ref({
  departureLocation: '',
  arrivalLocation: '',
  departureDate: '',
  returnDate: ''
})

//------------- sign in functionality----------------------
const isLoggedIn = ref(false);
const customerId = ref(null);
const username = ref('Traveler');
const userPoints = ref(0); // New: Tracks user points
const isDropdownOpen = ref(false); // New: State for dropdown

const logInUser = (userData) => {
  isLoggedIn.value = true;
  customerId.value = userData.id;
  username.value = userData.username;
  userPoints.value = userData.points || 0;
  localStorage.setItem('flightUserSession', JSON.stringify({
    id: userData.id,
    username: userData.username,
    points: userData.points
  }));

  console.log(`User ${username.value} logged in. Customer ID: ${customerId.value}`);
};

const logOutUser = () => {
  isLoggedIn.value = false;
  customerId.value = null;
  isDropdownOpen.value = false;
  username.value = 'Customer';
  userPoints.value = 0;

  localStorage.removeItem('flightUserSession');
};

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const handleProfileClick = () => {
  isDropdownOpen.value = false;
  alert('Navigating to My Profile (Feature not yet implemented)');
};

const handleViewBookingsClick = () => {
  isDropdownOpen.value = false;
  if (customerId.value) {
    emit('navigate', 'BookedFlights', { id: customerId.value });
  } else {
    console.error('Customer ID is missing. Cannot view bookings.');

    alert('Please sign in to view your bookings.');
  }
};

defineExpose({
  logInUser,
  logOutUser
});

//--------------------------------------------------------
const isSearchActive = ref(false)

const flights = ref([])
const isLoading = ref(false)

onMounted(async () => {
  const sessionData = localStorage.getItem('flightUserSession');
  if (sessionData) {
    const userData = JSON.parse(sessionData);
    logInUser(userData); // Re-uses the logInUser method to restore state
  }

  await fetchAllFlights();
});

const fetchAllFlights = async () => {
  isLoading.value = true;
  try {
    const response = await api.get('/api/flights');

    console.log(response)

    flights.value = response.data.map(flight => ({
      id: flight.flightId,
      route: `${flight.departLocation} to ${flight.arrivalLocation}`,
      departureCity: flight.departLocation,
      departureTime: formatTime(flight.departTime),
      arrivalCity: flight.arrivalLocation,
      arrivalTime: formatTime(flight.arrivalTime),
      remainingSeats: flight.seatsRemaining,
      class: 'ECONOMY', // You may need to add this to your backend
      price: `$ ${flight.price || 1000} CAD`, // Add price to backend if needed
      dateRange: `${formatDate(flight.departTime)} to ${formatDate(flight.arrivalTime)}`,
      departureDate: formatDate(flight.departTime),
      returnDate: formatDate(flight.arrivalTime),
      status: flight.status,
      flightNumber: flight.flightNumber
    }));
    isSearchActive.value = false;
    console.log('Fetched flights:', flights.value);
  } catch (error) {
    console.error('Failed to fetch flights:', error);
  } finally {
    isLoading.value = false;
  }
};

// Helper function to format date (YYYY-MM-DD)
const formatDate = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toISOString().split('T')[0];
};

// Helper function to format time (HH:MM AM/PM)
const formatTime = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
};


const datePickerRef = ref(null);

// Combined search function
const searchFlights = async () => {
  try {
    const startDate = datePickerRef.value?.startDateDisplay;
    const endDate = datePickerRef.value?.endDateDisplay;

    const searchData = {
      "departureLocation": searchParams.value.departureLocation,
      "arrivalLocation": searchParams.value.arrivalLocation,
      "departTimeStr": startDate,
      "arrivalTimeStr": endDate,
      // tripType: value.value
    };

    console.log('Searching with params:', searchData); // ← Check browser console

    const response = await api.post('/api/flights/search', searchData);
    // Transform backend data to match your FlightCard component
    flights.value = response.data.map(flight => ({
      id: flight.flightId,
      route: `${flight.departLocation} to ${flight.arrivalLocation}`,
      departureCity: flight.departLocation,
      departureTime: formatTime(flight.departTime),
      arrivalCity: flight.arrivalLocation,
      arrivalTime: formatTime(flight.arrivalTime),
      remainingSeats: flight.seatsRemaining,
      class: 'ECONOMY',
      price: `$ ${flight.price || 1000} CAD`,
      dateRange: `${formatDate(flight.departTime)} to ${formatDate(flight.arrivalTime)}`,
      departureDate: formatDate(flight.departTime),
      returnDate: formatDate(flight.arrivalTime),
      status: flight.status,
      flightNumber: flight.flightNumber
    }));
    isSearchActive.value = false;
    console.log('Fetched flights:', flights.value);

  } catch (error) {
    console.error('Search failed:', error);
  }
};

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
  if (navigate) {
    navigate('BookingPayment')
  }
}
</script>