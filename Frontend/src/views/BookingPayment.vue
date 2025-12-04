<template>
  <div class="bg-gradient-to-br from-blue-50 via-white to-indigo-50 min-h-screen flex items-center justify-center p-8">

    <!-- Success Message Modal -->
    <div v-if="isSuccessMessageVisible" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50 p-4">
      <div class="bg-white p-8 rounded-xl shadow-2xl max-w-sm w-full text-center transform transition-all duration-300 scale-100">
        <div class="inline-block bg-green-100 p-4 rounded-full mb-4">
          <i class="fas fa-check-circle text-4xl text-green-600"></i>
        </div>
        <h3 class="text-2xl font-bold text-[#484848] mb-2">Booking Confirmed!</h3>
        <p class="text-[#9a9a9a]">Your flight has been booked and payment processed.</p>
      </div>
    </div>

    <div class="w-full max-w-2xl">
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <!-- Header -->
        <div class="text-center mb-8">
          <div class="inline-block bg-gradient-to-br from-green-500 to-emerald-600 p-4 rounded-full mb-4">
            <i class="fas fa-credit-card text-3xl text-white"></i>
          </div>
          <h2 class="font-bold text-[28px] text-[#484848]">
            Complete Your Booking
          </h2>
          <p class="text-[#9a9a9a] mt-2">Enter your payment details below. Flight:
            <span class="font-semibold text-blue-600">{{ flightDetails.route || 'Unknown' }}</span>
          </p>
        </div>

        <!-- Error Display -->
        <div v-if="bookingError" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-6">
          <strong class="font-bold mr-2">Booking Failed!</strong>
          <span class="block sm:inline">{{ bookingError }}</span>
        </div>
        <!-- Seat Fetching Error Display -->
        <div v-if="seatError" class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative mb-6">
          <strong class="font-bold mr-2">Seat Error!</strong>
          <span class="block sm:inline">{{ seatError }}</span>
        </div>


        <div class="h-[1px] w-full bg-gray-200 mb-8"></div>

        <form @submit.prevent="handlePayment" class="space-y-6">

          <!-- SEAT SELECTION DROPDOWN -->
          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-chair mr-2 text-blue-500"></i>Select Your Seat
            </label>
            <div v-if="isSeatsLoading" class="flex items-center space-x-2 p-3 bg-gray-100 rounded-lg">
              <i class="fas fa-spinner fa-spin text-blue-500"></i>
              <span class="text-sm text-gray-600">Loading available seats...</span>
            </div>
            <select
                v-else
                v-model="selectedSeatId"
                class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                required
                :disabled="isProcessing || availableSeats.length === 0"
            >
              <option :value="null" disabled>
                {{ availableSeats.length === 0 ? 'No seats available' : '-- Choose an available seat --' }}
              </option>
              <option v-for="seat in availableSeats" :key="seat.seatId" :value="seat.seatId">
                <!-- FIX: Use (seat.price || 0) to ensure a number exists before calling toFixed(2) -->
                {{ seat.seatNumber }} ({{ seat.seatClass }} - ${{ (seat.price || 0).toFixed(2) }})
              </option>
            </select>
            <p v-if="availableSeats.length === 0 && !isSeatsLoading" class="text-sm text-red-500 mt-2">
              This flight is fully booked or seats could not be loaded.
            </p>
          </div>
          <!-- END SEAT SELECTION -->

          <!-- Payment Details -->
          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-credit-card mr-2 text-blue-500"></i>Credit Card Number
            </label>
            <input
                v-model="paymentData.cardNumber"
                type="text"
                placeholder="1234 5678 9012 3456"
                maxlength="19"
                class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                required
                :disabled="isProcessing"
            />
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-semibold text-[#484848] mb-2">
                <i class="far fa-calendar mr-2 text-blue-500"></i>Expiry Date
              </label>
              <input
                  v-model="paymentData.expiryDate"
                  type="text"
                  placeholder="MM/YY"
                  maxlength="5"
                  class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                  required
                  :disabled="isProcessing"
              />
            </div>

            <div>
              <label class="block text-sm font-semibold text-[#484848] mb-2">
                <i class="fas fa-lock mr-2 text-blue-500"></i>CVV
              </label>
              <input
                  v-model="paymentData.cvv"
                  type="text"
                  placeholder="123"
                  maxlength="3"
                  class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                  required
                  :disabled="isProcessing"
              />
            </div>
          </div>

          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-user mr-2 text-blue-500"></i>Cardholder Name
            </label>
            <input
                v-model="paymentData.cardholderName"
                type="text"
                placeholder="John Doe"
                class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                required
                :disabled="isProcessing"
            />
          </div>

          <!-- Security Notice -->
          <div class="bg-blue-50 border border-blue-200 rounded-lg p-4 flex items-start gap-3">
            <i class="fas fa-shield-alt text-blue-600 text-xl mt-1"></i>
            <div>
              <p class="font-semibold text-sm text-[#484848]">Secure Payment</p>
              <p class="text-xs text-[#9a9a9a] mt-1">Your payment information is encrypted and secure</p>
            </div>
          </div>

          <button
              type="submit"
              :disabled="isProcessing || availableSeats.length === 0 || isSeatsLoading || seatError"
              class="w-full bg-gradient-to-r from-green-500 to-emerald-600 text-white font-bold text-[15px] px-12 py-4 rounded-lg transition-all duration-300 shadow-md hover:shadow-lg disabled:opacity-70 disabled:cursor-not-allowed"
              :class="{ 'hover:from-green-600 hover:to-emerald-700': !isProcessing }"
          >
            <i v-if="isProcessing || isSeatsLoading" class="fas fa-spinner fa-spin mr-2"></i>
            <i v-else class="fas fa-check-circle mr-2"></i>
            {{ isProcessing ? 'Processing...' : (isSeatsLoading ? 'Loading Seats...' : 'Confirm Payment') }}
          </button>
        </form>

        <button
            @click="$emit('navigate', 'FlightBooking')"
            class="mt-6 w-full text-[14px] text-[#9a9a9a] hover:text-[#484848] font-semibold transition"
            :disabled="isProcessing"
        >
          <i class="fas fa-arrow-left mr-2"></i>Back to Flights
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, onMounted } from 'vue' // <-- Added onMounted
import axios from 'axios'

const emit = defineEmits(['navigate'])

const props = defineProps({
  // Contains flight details passed from FlightBooking.vue
  flightDetails: {
    type: Object,
    required: true
  },
  // Customer ID for the booking API call. Assuming this is passed from the parent.
  customerId: {
    type: [Number, String],
    required: true
  }
})

// --- LOGGING THE FLIGHT ID ---
console.log("Current Flight ID:", props.flightDetails.id);
// -----------------------------


// State for UI management
const isSuccessMessageVisible = ref(false)
const isProcessing = ref(false)
const bookingError = ref(null)

// --- SEAT SELECTION STATE (NOW DYNAMIC) ---
const availableSeats = ref([]);
const selectedSeatId = ref(null);
const isSeatsLoading = ref(false);
const seatError = ref(null);

const fetchAvailableSeats = async () => {
  isSeatsLoading.value = true;
  seatError.value = null;

  const flightId = props.flightDetails.id;

  try {
    const response = await axios.get(`http://localhost:8080/api/seats/flight/${flightId}`);
    availableSeats.value = response.data;

    if (availableSeats.value.length > 0) {
      selectedSeatId.value = availableSeats.value[0].seatId;
    }

  } catch (err) {
    seatError.value = "Failed to load seats.";
  } finally {
    isSeatsLoading.value = false;
  }
};

// --- Execute seat fetching when component is mounted ---
onMounted(() => {
  fetchAvailableSeats();
});

const paymentData = ref({
  cardNumber: '4500 1234 5678 9012',
  expiryDate: '12/26',
  cvv: '123',
  cardholderName: 'John Doe',
})

const handlePayment = async () => {
  console.log('Starting booking process...');

  isProcessing.value = true;
  bookingError.value = null;

  if (!selectedSeatId.value) {
    bookingError.value = "Please select a seat before proceeding with payment.";
    isProcessing.value = false;
    return;
  }

  try {
    const bookingPayload = {
      customerId: Number(props.customerId),
      seatId: Number(selectedSeatId.value),
      bookingDate: new Date(),
      paymentStatus: "PAID",
      bookingStatus: "WAITLISTED"
    };

    console.log("Sending booking payload:", bookingPayload);

    const response = await axios.post(
        "http://localhost:8080/api/bookings",
        bookingPayload
    );

    console.log("Booking created:", response.data);

    isSuccessMessageVisible.value = true;

    setTimeout(() => {
      isSuccessMessageVisible.value = false;

      emit("navigate", "FlightBooking", { id: props.customerId });
    }, 1000);

  } catch (error) {
    console.error("Booking error:", error);

    if (error.response) {
      const backendMessage = error.response.data || error.response.statusText;
      bookingError.value = "You already have a booking for this flight.";
      console.error("Backend message:", backendMessage);
    } else if (error.request) {
      bookingError.value = "Cannot reach the booking service. Please ensure the backend is running.";
    } else {
      bookingError.value = "An unexpected error occurred during payment processing.";
    }
  } finally {
    isProcessing.value = false;
  }
};

</script>