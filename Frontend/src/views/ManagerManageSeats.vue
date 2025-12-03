<template>
  <div class="manager-seats-view min-h-screen bg-white rounded-2xl p-10 shadow-xl">
    
    <header class="flex justify-between items-center mb-16 pt-4">
      <h1 class="text-[#484848] text-4xl font-extrabold font-['Montserrat']">
        Manage Seats of Flight {{ flightId }}
      </h1>
      
      <div class="flex flex-col space-y-4">
        
        <button 
          @click="openCreateSeatModal"
          class="bg-[#9A9A9A] text-[#484848] text-xl font-bold font-['Montserrat'] 
                 w-[238px] h-[82px] rounded-[56px] hover:bg-gray-700 hover:text-white transition-colors duration-300"
        >
          Create Seat
        </button>

        <button 
          @click="goBackToFlightManagement"
          class="bg-[#9A9A9A] text-[#484848] text-xl font-bold font-['Montserrat'] 
                 w-[238px] h-[82px] rounded-[56px] hover:bg-gray-700 hover:text-white transition-colors duration-300 self-end mt-4"
        >
          Back to Flight {{ flightId }}
        </button>
      </div>
    </header>

    <main class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <ManagerSeatCard 
        v-for="seat in seatsData" 
        :key="seat.seatId" 
        :seat="seat"
        @set-price="handleSetPrice"
      />
    </main>

    <footer class="text-center mt-20 pb-4">
      <p class="text-[#484848] text-lg font-bold font-['Montserrat']">
        Paginations or Load on scroll...
      </p>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import ManagerSeatCard from './ManagerSeatCard.vue';
import { useRouter } from 'vue-router'; // Assuming you use Vue Router for navigation

const router = useRouter();

// Example: Retrieve Flight ID from route parameter
const flightId = ref(router.currentRoute.value.params.flightId || 'AC130'); 

// Mock Seat Data based on the repeated blocks in your original HTML
const seatsData = ref([
  { 
    seatId: 'S001', 
    flightId: flightId.value, 
    class: 'ECONOMY', 
    seatNumber: 'A20', 
    status: 'AVAILABLE', 
    price: '1000', 
    currency: 'CAD',
    route: 'Montreal to Toronto',
    dateRange: '2025-11-22 to 2025 11-30'
  },
  { 
    seatId: 'S002', 
    flightId: flightId.value, 
    class: 'ECONOMY', 
    seatNumber: 'A21', 
    status: 'BOOKED', 
    price: '1000', 
    currency: 'CAD',
    route: 'Montreal to Toronto',
    dateRange: '2025-11-22 to 2025 11-30'
  },
  { 
    seatId: 'S003', 
    flightId: flightId.value, 
    class: 'BUSINESS', 
    seatNumber: 'B10', 
    status: 'AVAILABLE', 
    price: '2500', 
    currency: 'CAD',
    route: 'Montreal to Toronto',
    dateRange: '2025-11-22 to 2025 11-30'
  },
  { 
    seatId: 'S004', 
    flightId: flightId.value, 
    class: 'FIRST', 
    seatNumber: 'F01', 
    status: 'MAINTENANCE', 
    price: '0', 
    currency: 'CAD',
    route: 'Montreal to Toronto',
    dateRange: '2025-11-22 to 2025 11-30'
  },
  // ... add more mock data to fill the page
]);

const openCreateSeatModal = () => {
  alert(`Action: Open Create Seat Modal for Flight ${flightId.value}`);
};

const handleSetPrice = (seatId) => {
  alert(`Action: Set Price requested for Seat ID: ${seatId}`);
  // In a real application, you would open a price editing modal here.
};

const goBackToFlightManagement = () => {
  // Assuming the previous page was ManagerManageFlights
  // You might use router.push() to navigate back or to a specific route.
  // router.push({ name: 'ManagerManageFlights' }); 
  alert(`Action: Back to managing Flight ${flightId.value}`);
};
</script>

<style scoped>
/* Scoped styles for the main container */
.manager-seats-view {
  max-width: 1366px; 
  margin: 0 auto;
}
</style>