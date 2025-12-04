<template>
  <div class="bg-gray-50 min-h-screen py-10 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">

      <header class="flex flex-col md:flex-row justify-between items-start md:items-center mb-10">
        <div>
          <h1 class="font-extrabold text-5xl text-gray-800 mb-2 tracking-tight">
            <i class="fas fa-ticket-alt text-indigo-500 mr-4"></i>My Reservations
          </h1>
          <p class="text-gray-500 text-lg">Manage your flight bookings (Customer ID: **{{ id }}**)</p>
        </div>
        <button
            @click="$emit('navigate', 'FlightBooking')"
            class="mt-4 md:mt-0 px-6 py-3 bg-white border border-gray-300 text-gray-700 rounded-xl hover:bg-indigo-50 hover:text-indigo-600 transition font-semibold shadow-md"
        >
          <i class="fas fa-home mr-2"></i>Back to Search
        </button>
      </header>

      <div v-if="isLoading" class="text-center py-20 bg-white rounded-3xl shadow-xl border border-indigo-100">
        <i class="fas fa-plane-departure fa-spin text-7xl text-indigo-500 mb-6"></i>
        <p class="text-3xl font-extrabold text-gray-800">Fetching Reservations...</p>
        <p class="text-gray-500 mt-2">Connecting to the booking system. Please wait.</p>
      </div>

      <div v-else-if="isError" class="text-center py-20 bg-white rounded-3xl shadow-xl border border-red-200">
        <i class="fas fa-exclamation-triangle text-7xl text-red-500 mb-6"></i>
        <p class="text-3xl font-extrabold text-gray-800">Server Connection Failed</p>
        <p class="text-gray-500 mt-2">We couldn't retrieve your bookings. Please check your connection or try again later.</p>
      </div>

      <div v-else-if="bookedFlights.length > 0">
        <p class="text-gray-500 text-lg mb-6 font-medium">
          <i class="fas fa-info-circle text-indigo-500 mr-2"></i>You have **{{ bookedFlights.length }}** active booking{{ bookedFlights.length > 1 ? 's' : '' }}
        </p>

        <div v-if="cancellationMessage" :class="cancellationStatus === 'success' ? 'bg-green-50 text-green-700 border-green-400' : 'bg-red-50 text-red-700 border-red-400'"
             class="p-5 mb-8 rounded-xl border font-semibold transition-opacity duration-300 shadow-sm">
          <i class="fas mr-3 text-lg" :class="cancellationStatus === 'success' ? 'fa-check-circle' : 'fa-times-circle'"></i>
          {{ cancellationMessage }}
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
              v-for="booking in bookedFlights"
              :key="booking.id"
              class="bg-white rounded-3xl shadow-xl border border-gray-100 hover:shadow-2xl transition-all duration-500 overflow-hidden relative"
          >
            <div v-if="confirmingId === booking.id" class="absolute inset-0 bg-white/95 backdrop-blur-sm flex flex-col items-center justify-center p-8 z-10 rounded-3xl">
              <i class="fas fa-exclamation-circle text-7xl text-amber-500 mb-4 animate-pulse"></i>
              <p class="text-2xl font-bold text-gray-800 mb-3">Confirm Cancellation</p>
              <p class="text-base text-center text-gray-500 mb-8">Are you sure you want to cancel reservation **#{{ booking.id }}**? This action cannot be reversed.</p>
              <div class="flex space-x-4 w-full">
                <button @click="confirmingId = null" class="flex-1 px-4 py-3 text-gray-700 bg-gray-200 rounded-xl hover:bg-gray-300 font-semibold transition">
                  No, Keep It
                </button>
                <button @click="executeCancelFlight(booking.id)" class="flex-1 px-4 py-3 bg-red-600 text-white rounded-xl hover:bg-red-700 font-bold transition shadow-lg">
                  Yes, Cancel Now
                </button>
              </div>
            </div>

            <div
                :class="{
                   'bg-gradient-to-br from-red-500 to-red-600': booking.status === 'CANCELLED',
                   'bg-gradient-to-br from-indigo-500 to-purple-600': booking.status !== 'CANCELLED' && booking.status !== 'WAITLISTED',
                   'bg-gradient-to-br from-amber-500 to-orange-600': booking.status === 'WAITLISTED'
                 }"
                class="h-[180px] rounded-t-3xl relative flex flex-col items-center justify-center p-6 text-white"
            >
              <i class="fas text-5xl mb-3 opacity-90"
                 :class="{
                    'fa-ban': booking.status === 'CANCELLED',
                    'fa-clock': booking.status === 'WAITLISTED',
                    'fa-check-circle': booking.status !== 'CANCELLED' && booking.status !== 'WAITLISTED'
                 }">
              </i>
              <p class="font-extrabold text-2xl tracking-wide">ECONOMY CLASS</p>

              <div class="absolute top-4 right-4 bg-white/20 backdrop-blur-sm px-4 py-1.5 rounded-full">
                <p class="text-white font-bold text-xs tracking-wider">Booking ID: {{ booking.id }}</p>
              </div>
            </div>

            <div class="p-6 space-y-5">

              <div class="text-center pb-4 border-b border-gray-100">
                <div class="flex justify-center items-center space-x-2 text-3xl font-extrabold text-gray-800">
                  <p class="font-extrabold text-2xl text-gray-800 tracking-tight">
                    Seat ID: {{ booking.seatId }}
                  </p>
                </div>

                <p class="text-sm text-gray-500 mt-2">
                  <i class="far fa-calendar-alt mr-2"></i>Booked on: **{{ booking.bookingDate }}**
                </p>
              </div>

              <div class="flex justify-around text-center border-b border-gray-100 pb-4">
                <div>
                  <p class="text-xs font-semibold text-gray-400">STATUS</p>
                  <p class="font-bold mt-1 text-sm" :class="{
                    'text-green-600': booking.status === 'CONFIRMED',
                    'text-amber-600': booking.status === 'WAITLISTED',
                    'text-red-600': booking.status === 'CANCELLED',
                  }">{{ booking.status }}</p>
                </div>
                <div>
                  <p class="text-xs font-semibold text-gray-400">PAYMENT</p>
                  <p class="font-bold mt-1 text-sm text-gray-700">{{ booking.paymentStatus }}</p>
                </div>
              </div>


              <div class="pt-4">
                <button
                    v-if="booking.status !== 'CANCELLED'"
                    @click="initiateCancelFlight(booking.id)"
                    :disabled="confirmingId !== null"
                    class="w-full bg-red-500 text-white font-bold text-lg px-8 py-4 rounded-xl hover:bg-red-600 transition-all duration-300 shadow-lg hover:shadow-xl disabled:opacity-50 disabled:cursor-wait"
                >
                  <i class="fas fa-times-circle mr-2"></i>Cancel Reservation
                </button>
                <button
                    v-else
                    disabled
                    class="w-full bg-gray-300 text-gray-600 font-bold text-lg px-8 py-4 rounded-xl opacity-90 cursor-not-allowed shadow-md"
                >
                  <i class="fas fa-check-circle mr-2"></i>Cancellation Confirmed
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-center py-20 bg-white rounded-3xl shadow-xl border border-indigo-100">
        <i class="fas fa-inbox text-7xl text-gray-300 mb-6"></i>
        <p class="text-3xl font-extrabold text-gray-800 mb-4">
          No active reservations
        </p>
        <p class="text-gray-500 mb-8 text-lg">
          It looks like your travel history is empty. Start exploring!
        </p>
        <button
            @click="$emit('navigate', 'FlightBooking')"
            class="px-10 py-4 bg-gradient-to-r from-indigo-500 to-purple-600 text-white rounded-xl hover:from-indigo-600 hover:to-purple-700 transition font-bold text-lg shadow-xl hover:shadow-2xl"
        >
          <i class="fas fa-search mr-2"></i>Browse Flights
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, defineProps, defineEmits } from 'vue'
import axios from 'axios'

const props = defineProps({
  id: {
    type: [Number, String],
    required: true
  }
})

defineEmits(['navigate'])

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

const bookedFlights = ref([])
const isLoading = ref(true)
const isError = ref(false)
const confirmingId = ref(null)
const cancellationMessage = ref(null)
const cancellationStatus = ref(null)

const formatDate = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toISOString().split('T')[0];
};

const formatTime = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  // Returns time only, e.g., 10:30 PM
  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
};

const showMessage = (message, status = 'success') => {
  cancellationMessage.value = message;
  cancellationStatus.value = status;
  setTimeout(() => {
    cancellationMessage.value = null;
  }, 5000); // Message disappears after 5 seconds
}

const fetchBookings = async (customerId) => {
  isLoading.value = true
  isError.value = false
  try {
    const response = await api.get(`/api/customers/booking/${customerId}`)

    bookedFlights.value = response.data.map(booking => {
      // NOTE: Using the simplified mapping based on your limited data structure.
      // If you include the nested 'flight' object in the future, you'll need to update this map.
      // Placeholders are used for missing flight details.
      return {
        id: booking.bookingId,
        seatId: booking.seatId || 'N/A',
        bookingDate: formatDate(booking.bookingDate),
        paymentStatus: booking.paymentStatus || 'N/A',
        status: booking.bookingStatus || 'CONFIRMED',

        // Placeholders for missing flight data (used in some design elements):
        flightClass: 'N/A',
        departureTime: 'N/A',
        arrivalTime: 'N/A',
        departureCity: 'Missing Data',
        arrivalCity: 'Missing Data',
        price: 'N/A',
      }
    })
    console.log('Fetched bookings:', bookedFlights.value)

  } catch (error) {
    console.error('Failed to fetch bookings:', error)
    isError.value = true
    bookedFlights.value = []
  } finally {
    isLoading.value = false
  }
}

// Step 1: Initiate cancellation confirmation
const initiateCancelFlight = (bookingId) => {
  cancellationMessage.value = null;
  confirmingId.value = bookingId;
}

// Step 2: Execute cancellation after confirmation
const executeCancelFlight = async (bookingId) => {
  confirmingId.value = null;
  isLoading.value = true;

  try {
    // API: http://localhost:8080/api/bookings/{bookingId}
    const response = await api.delete(`/api/bookings/${bookingId}`)

    if (response.status === 200 || response.status === 204) {
      showMessage(`Reservation #${bookingId} has been successfully cancelled.`, 'success');
      // Refresh the list to reflect the cancelled status
      await fetchBookings(props.id);
    } else {
      showMessage(`Cancellation failed: Received unexpected status ${response.status}.`, 'error');
    }

  } catch (error) {
    console.error('Cancellation failed:', error)
    const errorMessage = error.response?.data?.message || 'The reservation could not be cancelled. Please try again.';
    showMessage(`Cancellation failed: ${errorMessage}`, 'error');

  } finally {
    isLoading.value = false;
  }
}

// Fetch bookings when the component mounts
onMounted(() => {
  if (props.id) {
    fetchBookings(props.id)
  } else {
    console.error('Customer ID prop is missing. Cannot fetch bookings.')
    isError.value = true
    isLoading.value = false
  }
})
</script>