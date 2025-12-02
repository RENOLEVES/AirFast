<template>
  <div class="min-h-screen">
    <component :is="currentViewComponent" @navigate="navigateTo" />
  </div>
</template>

<script setup>
import { ref, computed, provide } from 'vue'
import HomePage from './views/FlightBooking.vue'
import BookedFlights from './views/BookedFlights.vue'
import UserSignup from './views/UserSignup.vue'
import UserSignin from './views/UserSignin.vue'
import BookingPayment from './views/BookingPayment.vue'

const currentView = ref('HomePage')

const views = {
  HomePage,
  BookedFlights,
  UserSignup,
  UserSignin,
  BookingPayment,
}

const currentViewComponent = computed(() => views[currentView.value])

const navigateTo = (view) => {
  currentView.value = view
}

// Make navigation available globally
provide('navigate', navigateTo)
</script>
