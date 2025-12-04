<template>
  <div class="bg-gray-50 min-h-screen p-8">
    <div class="max-w-7xl mx-auto">
      <!-- Header -->
      <div class="flex justify-between items-center mb-8">
        <div>
          <h1 class="font-extrabold text-[42px] text-[#484848] mb-2">
            <i class="fas fa-ticket-alt text-blue-500 mr-3"></i>My Bookings
          </h1>
          <p class="text-[#9a9a9a]">Manage your flight reservations (Customer ID: {{ id }})</p>
        </div>
        <button
            @click="$emit('navigate', 'FlightBooking')"
            class="px-6 py-3 bg-white border border-gray-300 text-[#484848] rounded-lg hover:bg-gray-50 transition font-semibold shadow-sm"
        >
          <i class="fas fa-home mr-2"></i>Back to Home
        </button>
      </div>

      <!-- Loading State -->
      <div v-if="isLoading" class="text-center py-20 bg-white rounded-2xl shadow-md">
        <i class="fas fa-spinner fa-spin text-6xl text-blue-500 mb-6"></i>
        <p class="text-[28px] font-bold text-[#484848]">Loading Bookings...</p>
        <p class="text-[#9a9a9a] mt-2">Fetching your reservations from the system.</p>
      </div>

      <!-- Error State -->
      <div v-else-if="isError" class="text-center py-20 bg-white rounded-2xl shadow-md">
        <i class="fas fa-exclamation-triangle text-6xl text-red-500 mb-6"></i>
        <p class="text-[28px] font-bold text-[#484848]">Failed to Load Bookings</p>
        <p class="text-[#9a9a9a] mt-2">There was an issue connecting to the server or retrieving your data.</p>
      </div>


      <!-- Booked Flights Grid -->
      <div v-else-if="bookedFlights.length > 0">
        <p class="text-[#9a9a9a] mb-6">
          <i class="fas fa-info-circle mr-2"></i>You have {{ bookedFlights.length }} active booking{{ bookedFlights.length > 1 ? 's' : '' }}
        </p>

        <!-- Global Status Message (for cancellation) -->
        <div v-if="cancellationMessage" :class="cancellationStatus === 'success' ? 'bg-green-100 text-green-700 border-green-400' : 'bg-red-100 text-red-700 border-red-400'"
             class="p-4 mb-6 rounded-lg border-l-4 font-medium transition-opacity duration-300">
          <i class="fas mr-2" :class="cancellationStatus === 'success' ? 'fa-check-circle' : 'fa-times-circle'"></i>
          {{ cancellationMessage }}
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
              v-for="booking in bookedFlights"
              :key="booking.id"
              class="bg-white rounded-xl shadow-sm border border-gray-200 hover:shadow-xl transition-all duration-300 overflow-hidden relative"
          >
            <!-- Cancellation Confirmation Overlay (Custom Modal Replacement) -->
            <div v-if="confirmingId === booking.id" class="absolute inset-0 bg-white/95 backdrop-blur-sm flex flex-col items-center justify-center p-6 z-10 rounded-xl">
              <i class="fas fa-exclamation-triangle text-6xl text-yellow-500 mb-4 animate-pulse"></i>
              <p class="text-xl font-bold text-[#484848] mb-3">Confirm Cancellation</p>
              <p class="text-sm text-center text-[#9a9a9a] mb-6">Are you sure you want to cancel booking #{{ booking.id }}? This action cannot be undone.</p>
              <div class="flex space-x-4 w-full">
                <button @click="confirmingId = null" class="flex-1 px-4 py-2 text-[#484848] bg-gray-200 rounded-lg hover:bg-gray-300 font-semibold transition">
                  No, Keep Booking
                </button>
                <button @click="executeCancelFlight(booking.id)" class="flex-1 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 font-semibold transition shadow-md">
                  Yes, Cancel It
                </button>
              </div>
            </div>

            <!-- Flight Header with gradient -->
            <div :class="booking.status === 'CANCELLED' ? 'from-red-500 to-red-600' : 'from-green-500 to-emerald-600'"
                 class="bg-gradient-to-br h-[200px] rounded-t-xl relative flex items-center justify-center"
            >
              <div class="text-center text-white">
                <i class="fas text-6xl mb-4 opacity-90" :class="booking.status === 'CANCELLED' ? 'fa-ban' : 'fa-check-circle'"></i>
                <p class="font-bold text-xl">{{ booking.flightClass || 'Economy' }} Class</p>
              </div>
              <div class="absolute top-4 left-4 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full">
                <p class="text-white font-bold text-sm">
                  <i class="fas mr-2" :class="booking.status === 'CANCELLED' ? 'fa-info-circle' : 'fa-calendar-check'"></i>
                  {{ booking.status || 'CONFIRMED' }}
                </p>
              </div>
              <div class="absolute top-4 right-4 bg-white/20 backdrop-blur-sm px-4 py-2 rounded-full">
                <p class="text-white font-bold text-sm">Booking ID: {{ booking.id }}</p>
              </div>
            </div>

            <!-- Flight Details -->
            <div class="p-6 space-y-4">
              <!-- Route -->
              <div class="text-center pb-4 border-b border-gray-100">
                <p class="font-bold text-2xl text-[#484848] mb-2">
                  {{ booking.departureCity }} to {{ booking.arrivalCity }}
                </p>
                <p class="text-sm text-[#9a9a9a]">
                  <i class="far fa-calendar mr-2"></i>{{ booking.departureDate }}
                </p>
              </div>

              <!-- Flight Times -->
              <div class="flex items-center justify-between">
                <div class="text-center flex-1">
                  <p class="text-xs text-[#9a9a9a] mb-1">Departure</p>
                  <p class="font-bold text-lg text-[#484848]">{{ booking.departureTime }}</p>
                  <p class="text-sm text-[#9a9a9a]">{{ booking.departureCity }}</p>
                </div>

                <div class="flex-1 flex justify-center">
                  <i class="fas fa-arrow-right text-2xl text-green-500"></i>
                </div>

                <div class="text-center flex-1">
                  <p class="text-xs text-[#9a9a9a] mb-1">Arrival</p>
                  <p class="font-bold text-lg text-[#484848]">{{ booking.arrivalTime }}</p>
                  <p class="text-sm text-[#9a9a9a]">{{ booking.arrivalCity }}</p>
                </div>
              </div>

              <!-- Price and Cancel Button -->
              <div class="pt-4 border-t border-gray-100">
                <button
                    v-if="booking.status !== 'CANCELLED'"
                    @click="initiateCancelFlight(booking.id)"
                    :disabled="confirmingId !== null"
                    class="w-full bg-red-500 text-white font-bold text-[15px] px-8 py-4 rounded-lg hover:bg-red-600 transition-all duration-300 shadow-md hover:shadow-lg disabled:opacity-50"
                >
                  <i class="fas fa-times-circle mr-2"></i>Cancel Booking
                </button>
                <button
                    v-else
                    disabled
                    class="w-full bg-gray-400 text-white font-bold text-[15px] px-8 py-4 rounded-lg opacity-80 cursor-not-allowed"
                >
                  <i class="fas fa-check-circle mr-2"></i>Already Cancelled
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
    // API: http://localhost:8080/api/customers/booking/{customerId}
    const response = await api.get(`/api/customers/booking/${customerId}`)

    // Map the backend booking data to the format expected by the template
    bookedFlights.value = response.data.map(booking => {
      // Assuming 'flight' object is nested inside the booking object
      const flight = booking.flight || {};

      return {
        id: booking.bookingId,
        departureCity: flight.departLocation || 'N/A',
        arrivalCity: flight.arrivalLocation || 'N/A',
        departureTime: formatTime(flight.departTime),
        arrivalTime: formatTime(flight.arrivalTime),
        departureDate: formatDate(flight.departTime),
        price: flight.price || '0',
        flightClass: booking.flightClass || 'ECONOMY', // Assuming this is part of the booking data
        status: booking.bookingStatus || 'CONFIRMED'
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

// Step 1: Initiate cancellation confirmation (replaces window.confirm)
const initiateCancelFlight = (bookingId) => {
  cancellationMessage.value = null; // Clear previous messages
  confirmingId.value = bookingId;
}

// Step 2: Execute cancellation after confirmation
const executeCancelFlight = async (bookingId) => {
  confirmingId.value = null; // Close confirmation overlay
  isLoading.value = true;

  try {
    // API: http://localhost:8080/api/bookings/{bookingId}
    const response = await api.delete(`/api/bookings/${bookingId}`)

    if (response.status === 200 || response.status === 204) {
      showMessage(`Booking #${bookingId} has been successfully cancelled.`, 'success');
      // Refresh the list to reflect the cancelled status
      await fetchBookings(props.id);
    } else {
      showMessage(`Cancellation failed: Received unexpected status ${response.status}.`, 'error');
    }

  } catch (error) {
    console.error('Cancellation failed:', error)
    const errorMessage = error.response?.data?.message || 'The booking could not be cancelled. Please try again.';
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