<template>
  <div class="relative min-h-screen flex items-center justify-center p-8 bg-white">
    <div class="relative w-[658px]">
      <div class="bg-white rounded-[10px] shadow-[0px_0px_8px_0px_rgba(194,198,204,0.6)] p-12">
        <p class="font-bold text-[18px] text-[#484848] mb-6">
          Booking Payment
        </p>
        
        <div class="h-[1px] w-full bg-[#c2c6cc] mb-8"></div>
        
        <form @submit.prevent="handlePayment" class="space-y-6">
          <div>
            <label class="block font-semibold text-[14px] text-[#484848] mb-2">
              Credit Card Number
            </label>
            <input
              v-model="paymentData.cardNumber"
              type="text"
              placeholder="1234 5678 9012 3456"
              class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
              required
            />
          </div>
          
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block font-semibold text-[14px] text-[#484848] mb-2">
                Expiry Date
              </label>
              <input
                v-model="paymentData.expiryDate"
                type="text"
                placeholder="MM/YY"
                class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
                required
              />
            </div>
            
            <div>
              <label class="block font-semibold text-[14px] text-[#484848] mb-2">
                CVV
              </label>
              <input
                v-model="paymentData.cvv"
                type="text"
                placeholder="123"
                class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
                required
              />
            </div>
          </div>
          
          <div>
            <label class="block font-semibold text-[14px] text-[#484848] mb-2">
              Cardholder Name
            </label>
            <input
              v-model="paymentData.cardholderName"
              type="text"
              placeholder="John Doe"
              class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
              required
            />
          </div>
          
          <button
            type="submit"
            class="bg-[#484848] text-white font-bold text-[15px] px-12 py-4 rounded-full hover:bg-opacity-90 transition w-full"
          >
            Confirm Payment
          </button>
        </form>
        
        <button
          @click="$emit('navigate', 'FlightBooking')"
          class="mt-6 text-[14px] text-[#9a9a9a] hover:text-[#484848] font-semibold"
        >
          ← Back to Flights
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
  alert('Payment successful! Flight booked.')
  emit('navigate', 'BookedFlights')
}
</script>

