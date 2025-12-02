<template>
  <div class="manager-flights-view min-h-screen bg-white rounded-2xl p-10 shadow-xl">
    
    <header class="flex justify-between items-center mb-16 pt-4">
      <h1 class="text-[#484848] text-4xl font-extrabold font-['Montserrat']">Manage Flights</h1>
      
      <button 
        @click="openCreateFlightModal"
        class="bg-[#9A9A9A] text-[#484848] text-xl font-bold font-['Montserrat'] 
               w-[238px] h-[82px] rounded-[56px] hover:bg-gray-700 hover:text-white transition-colors duration-300"
      >
        Create Flight
      </button>
    </header>

    <main class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <ManagerFlightCard 
        v-for="flight in flightsData" 
        :key="flight.id" 
        :flight="flight"
        @view-seats="handleAction('View Seats', flight.id)"
        @edit="handleAction('Edit', flight.id)"
        @delete="handleAction('Delete', flight.id)"
        @assign-employees="handleAction('Assign Employees', flight.id)"
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
import ManagerFlightCard from './ManagerFlightCard.vue';

// Mock Data based on the repeated blocks in your original HTML
const flightsData = ref([
  { 
    id: 1, 
    flightCode: 'AC130', 
    departureCity: 'Montreal', 
    departureTime: '8:30AM', 
    arrivalCity: 'Toronto', 
    arrivalTime: '9PM', 
    remainingSeats: 10, 
    flightTime: '{x mins}', 
    status: 'On Time', 
    dateRange: '2025-11-22 to 2025 11-30',
    canAssignEmployees: true 
  },
  { 
    id: 2, 
    flightCode: 'AC130', 
    departureCity: 'Montreal', 
    departureTime: '8:30AM', 
    arrivalCity: 'Toronto', 
    arrivalTime: '9PM', 
    remainingSeats: 10, 
    flightTime: '{x mins}', 
    status: 'On Time', 
    dateRange: '2025-11-22 to 2025 11-30',
    canAssignEmployees: false // Example with fewer buttons
  },
  { 
    id: 3, 
    flightCode: 'AC130', 
    departureCity: 'Montreal', 
    departureTime: '8:30AM', 
    arrivalCity: 'Toronto', 
    arrivalTime: '9PM', 
    remainingSeats: 10, 
    flightTime: '{x mins}', 
    status: 'Delayed', 
    dateRange: '2025-11-22 to 2025 11-30',
    canAssignEmployees: true 
  },
  { 
    id: 4, 
    flightCode: 'AC131', 
    departureCity: 'Montreal', 
    departureTime: '8:30AM', 
    arrivalCity: 'Toronto', 
    arrivalTime: '9PM', 
    remainingSeats: 10, 
    flightTime: '{x mins}', 
    status: 'On Time', 
    dateRange: '2025-11-22 to 2025 11-30',
    canAssignEmployees: true 
  },
  // ... more flights for a longer list
]);

const openCreateFlightModal = () => {
  alert('Action: Open Create Flight Modal');
};

const handleAction = (action, flightId) => {
  console.log(`${action} requested for Flight ID: ${flightId}`);
  // In a real application, you would implement the necessary API calls/routing here.
};
</script>

<style scoped>
/* Scoped styles for the main container (optional, most is handled by Tailwind) */
.manager-flights-view {
  max-width: 1366px; /* Keeping original max-width for design intent */
  margin: 0 auto;
}
</style>