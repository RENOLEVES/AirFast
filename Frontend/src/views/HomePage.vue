<template>
  <div class="bg-gradient-to-br from-blue-50 via-white to-indigo-50 min-h-screen flex items-center justify-center p-8">
    <div class="text-center max-w-2xl">
      <!-- Logo/Icon -->
      <div class="mb-8">
        <div class="inline-block bg-gradient-to-br from-blue-500 to-indigo-600 p-8 rounded-full shadow-xl">
          <i class="fas fa-plane-departure text-6xl text-white"></i>
        </div>
      </div>

      <!-- Title -->
      <h1 class="font-extrabold text-[56px] text-[#484848] mb-4 leading-tight">
        Flight Booking<br>System
      </h1>
      
      <p class="text-xl text-[#9a9a9a] mb-12">
        Find and book your perfect flight with ease
      </p>
      
      <!-- Main Actions -->
      <div class="space-y-4 mb-12">
        <button
          @click="$emit('navigate', 'FlightBooking')"
          class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white font-bold text-[20px] px-12 py-6 rounded-full hover:from-blue-600 hover:to-indigo-700 transition-all duration-300 shadow-lg hover:shadow-xl transform hover:-translate-y-1"
        >
          <i class="fas fa-search mr-3"></i>Book Flights
        </button>
        
        <button
          @click="$emit('navigate', 'BookedFlights')"
          class="w-full bg-white border-2 border-[#484848] text-[#484848] font-bold text-[20px] px-12 py-6 rounded-full hover:bg-[#484848] hover:text-white transition-all duration-300 shadow-md hover:shadow-lg transform hover:-translate-y-1"
        >
          <i class="fas fa-ticket-alt mr-3"></i>View Booked Flights
        </button>
      </div>
      
      <!-- Auth Links -->
      <div class="flex justify-center items-center gap-4 text-[16px]">
        <button
          @click="$emit('navigate', 'UserSignin')"
          class="text-[#484848] font-semibold hover:text-blue-600 transition-colors px-6 py-2 rounded-lg hover:bg-white/50"
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
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['navigate'])
</script>
