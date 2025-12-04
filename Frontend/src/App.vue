<template>
  <div id="app">
    <FlightBooking
        v-if="currentPage === 'FlightBooking'"
        ref="flightBookingRef"
        @navigate="handleNavigation"
        @user-authenticated="handleAuthSuccess"
        v-bind="currentPageProps"
    />

    <UserSignin
        v-else-if="currentPage === 'UserSignin'"
        @navigate="handleNavigation"
        @user-authenticated="handleAuthSuccess"
        v-bind="currentPageProps"
    />

    <UserSignup
        v-else-if="currentPage === 'UserSignup'"
        @navigate="handleNavigation"
        v-bind="currentPageProps"
    />

    <BookedFlights
        v-else-if="currentPage === 'BookedFlights'"
        @navigate="handleNavigation"
        v-bind="currentPageProps"/>

    <OwnerHomePage
        v-else-if="currentPage === 'OwnerHomePage'"
        @navigate="handleNavigation"
        v-bind="currentPageProps"
    />

  </div>
</template>

<script setup>
import { ref, computed, provide, nextTick } from 'vue'
import FlightBooking from './views/FlightBooking.vue' // Renamed from HomePage for clarity
import BookedFlights from './views/BookedFlights.vue'
import UserSignup from './views/UserSignup.vue'
import UserSignin from './views/UserSignin.vue'
import BookingPayment from './views/BookingPayment.vue'
import OwnerHomePage from './views/OwnerHomePage.vue'


const currentPage = ref('FlightBooking');
const flightBookingRef = ref(null);
const currentPageProps = ref({});

// --- Navigation ---
const handleNavigation = (pageName, data = {}) => {
  currentPage.value = pageName;
  currentPageProps.value = data; // Saves data payload (e.g., { id: customerId })
  console.log(`Navigating to: ${pageName}, with props:`, data);
};

provide('navigate', handleNavigation);

const handleAuthSuccess = async (userData) => {
  console.log('Authentication successful in App.vue. User Data:', userData);

  currentPage.value = 'FlightBooking';

  await nextTick();

  if (flightBookingRef.value && flightBookingRef.value.logInUser) {
    flightBookingRef.value.logInUser(userData);
    console.log('FlightBooking component updated with user data.');
  } else {
    console.error('Could not find flightBookingRef or logInUser method.');
  }
};
</script>