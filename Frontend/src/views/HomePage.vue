<template>
  <div class="bg-white overflow-clip relative min-h-screen flex items-center justify-center p-8">
    <div class="relative w-[658px] h-[490px]">
      <div class="absolute bg-white h-[454px] left-[24px] rounded-[10px] shadow-[0px_0px_8px_0px_rgba(194,198,204,0.6)] top-[18px] w-[612px]"></div>
      
      <p class="absolute font-bold h-[33px] left-[46px] text-[#484848] text-[18px] top-[45px] w-[187px]">
        Welcome
      </p>
      
      <!-- Book Flights Button -->
      <button
        @click="$emit('navigate', 'FlightBooking')"
        class="absolute h-[83px] left-[46px] top-[115px] w-[187px] bg-[#484848] rounded-xl hover:bg-opacity-90 transition flex items-center justify-center"
      >
        <p class="font-bold text-[15px] text-white">
          Book Flights
        </p>
      </button>
      
      <!-- View Booked Flights Button -->
      <button
        @click="$emit('navigate', 'BookedFlights')"
        class="absolute h-[83px] left-[46px] top-[224px] w-[187px] bg-[#484848] rounded-xl hover:bg-opacity-90 transition flex items-center justify-center"
      >
        <p class="font-bold text-[15px] text-white">
          View Booked Flights
        </p>
      </button>
      
      <div class="absolute h-[1px] left-[62px] top-[89px] w-[536px] bg-[#c2c6cc]"></div>
      
      <!-- Auth buttons -->
      <div class="absolute right-[46px] top-[45px] flex gap-4">
        <button
          @click="$emit('navigate', 'UserSignin')"
          class="px-6 py-2 border-2 border-[#484848] text-[#484848] rounded-full hover:bg-[#484848] hover:text-white transition font-semibold"
        >
          Sign In
        </button>
        <button
          @click="$emit('navigate', 'UserSignup')"
          class="px-6 py-2 bg-[#484848] text-white rounded-full hover:bg-opacity-90 transition font-semibold"
        >
          Sign Up
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['navigate'])
</script>

