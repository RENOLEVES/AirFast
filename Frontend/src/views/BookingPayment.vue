<template>
  <div class="bg-gradient-to-br from-blue-50 via-white to-indigo-50 min-h-screen flex items-center justify-center p-8">
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
          <p class="text-[#9a9a9a] mt-2">Enter your payment details below</p>
        </div>
        
        <div class="h-[1px] w-full bg-gray-200 mb-8"></div>
        
        <form @submit.prevent="handlePayment" class="space-y-6">
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
            class="w-full bg-gradient-to-r from-green-500 to-emerald-600 text-white font-bold text-[15px] px-12 py-4 rounded-lg hover:from-green-600 hover:to-emerald-700 transition-all duration-300 shadow-md hover:shadow-lg"
          >
            <i class="fas fa-check-circle mr-2"></i>Confirm Payment
          </button>
        </form>
        
        <button
          @click="$emit('navigate', 'FlightBooking')"
          class="mt-6 w-full text-[14px] text-[#9a9a9a] hover:text-[#484848] font-semibold transition"
        >
          <i class="fas fa-arrow-left mr-2"></i>Back to Flights
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['navigate'])

const paymentData = ref({
  cardNumber: '',
  expiryDate: '',
  cvv: '',
  cardholderName: ''
})

const handlePayment = () => {
  console.log('Processing payment...', paymentData.value)
  // Add your payment logic here
  alert('Payment successful! Your flight has been booked.')
  emit('navigate', 'BookedFlights')
}
</script>
