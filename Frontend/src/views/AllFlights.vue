<template>
  <div class="p-4 sm:p-8">
    <h1 class="text-zinc-700 text-4xl font-extrabold mb-8">All Flights</h1>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
      <FlightCard
          v-for="(flight, index) in flights"
          :key="index"
          :flight="flight"
      />
    </div>
  </div>
</template>

<script>
// Import the child component
import FlightCard from '../components/FlightCard.vue';

export default {
  name: 'AllFlights',
  components: {
    FlightCard,
  },
  data() {
    return {
      // Data structure to hold all flight information
      flights: [
        {
          code: 'AC130',
          origin: 'Montreal',
          destination: 'Toronto',
          departMontreal: '8:30AM',
          departToronto: '9PM',
          remainingSeats: 10,
          flightTime: '90 mins',
          status: 'On Time',
          dateRange: '2025-11-22 to 2025-11-30',
        },
        // Repeat the same data structure for the 8 other flight cards
        // In a real application, you would fetch this data from an API.
        // For demonstration, we'll repeat the same object structure.
        ...Array(8).fill({
          code: 'AC130',
          origin: 'Montreal',
          destination: 'Toronto',
          departMontreal: '8:30AM',
          departToronto: '9PM',
          remainingSeats: 10,
          flightTime: '90 mins',
          status: 'On Time',
          dateRange: '2025-11-22 to 2025-11-30',
        }),
      ],
    };
  },
};
</script>