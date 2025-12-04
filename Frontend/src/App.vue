<template>
  <component 
    :is="currentView" 
    v-bind="currentPageProps"
    @navigate="handleNavigation"
    @user-authenticated="handleAuthSuccess"
    ref="currentComponentRef"
  />
</template>

<script setup>
import { ref, computed, provide, nextTick } from 'vue'
import FlightBooking from './views/FlightBooking.vue'
import BookedFlights from './views/BookedFlights.vue'
import UserSignup from './views/UserSignup.vue'
import UserSignin from './views/UserSignin.vue'
import BookingPayment from './views/BookingPayment.vue'
import OwnerHomePage from './views/OwnerHomePage.vue'
import ManagerDashboard from './views/ManagerDashboard.vue'
import AllFlights from './views/AllFlights.vue'

const views = {
  FlightBooking,
  BookedFlights,
  UserSignup,
  UserSignin,
  BookingPayment,
  OwnerHomePage,
  ManagerDashboard,
  AllFlights
}

const currentPage = ref('FlightBooking')  // Changed back to FlightBooking as default
const currentPageProps = ref({})
const currentComponentRef = ref(null)

const currentView = computed(() => views[currentPage.value] || FlightBooking)

const handleNavigation = (pageName, data = {}) => {
  currentPage.value = pageName
  currentPageProps.value = data
  console.log(`Navigating to: ${pageName}`, data)
}

const handleAuthSuccess = async (userData) => {
  console.log('Auth success:', userData)
  
  // Route based on role from userData
  if (userData.role === 'OWNER') {
    currentPage.value = 'OwnerHomePage'
  } else if (userData.role === 'MANAGER') {
    currentPage.value = 'ManagerDashboard'
  } else {
    currentPage.value = 'FlightBooking'
  }
  
  await nextTick()
  
  // Pass user data to FlightBooking component if that's where we're going
  if (currentPage.value === 'FlightBooking' && currentComponentRef.value?.logInUser) {
    currentComponentRef.value.logInUser(userData)
  }
}

provide('navigate', handleNavigation)
</script>