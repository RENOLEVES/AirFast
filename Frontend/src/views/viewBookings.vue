<template>
  <div class="p-4 sm:p-8 w-full h-full flex flex-col overflow-x-hidden">

    <div class="flex-grow overflow-y-auto pr-4 -mr-4">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8 min-w-0">
        <FlightCard
            v-for="(flight, index) in flights"
            :key="index"
            :flight="flight"
        />
      </div>
    </div>

    <div v-if="!loading && !error" class="text-center py-6 mt-8 text-gray-500 border-t pt-6 flex-shrink-0">
      Showing {{ flights.length }} flights.
    </div>
  </div>
</template>

<script>
import FlightCard from '../components/FlightCardForOwner.vue';

export default {
  name: 'AllFlights',
  components: {
    FlightCard,
  },
  data() {
    return {
      flights: [],
      loading: false,
      error: null,
      // NOTE: Using a relative URL '/api/flights' is often better than 'http://localhost:8080/...'
      // in production environments, but we keep the user-specified URL here.
      apiUrl: 'http://localhost:8080/api/flights',
    };
  },
  methods: {
    /**
     * Helper to format a time string (e.g., 'HH:MM').
     * @param {string | Date} dateTimeString - ISO date string or similar.
     * @returns {string} Formatted time string.
     */
    formatTime(dateTimeString) {
      if (!dateTimeString) return 'N/A';
      try {
        const date = new Date(dateTimeString);
        return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
      } catch {
        return 'Invalid Time';
      }
    },

    /**
     * Helper to format a date string (e.g., 'YYYY-MM-DD').
     * @param {string | Date} dateTimeString - ISO date string or similar.
     * @returns {string} Formatted date string.
     */
    formatDate(dateTimeString) {
      if (!dateTimeString) return 'N/A';
      try {
        const date = new Date(dateTimeString);
        return date.toLocaleDateString('en-CA', { year: 'numeric', month: 'short', day: '2-digit' });
      } catch {
        return 'Invalid Date';
      }
    },

    /**
     * Fetches flight data from the API and transforms the response structure.
     */
    async fetchFlights() {
      this.loading = true;
      this.error = null;

      try {
        // Since we cannot use 'api.get' or Axios directly here, we use fetch().
        // This simulates the behavior of the API call.
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        // The response body is the data property that would be in axios's response.data
        const backendData = await response.json();

        console.log('Backend response:', backendData);

        // Transform backend data to match your FlightCard component's expected structure
        this.flights = backendData.map(flight => ({
          // Map properties from the backend structure to the frontend structure
          id: flight.flightId || flight.id, // Use flightId or fallback to id
          route: `${flight.departLocation} to ${flight.arrivalLocation}`,
          departureCity: flight.departLocation,
          departureTime: this.formatTime(flight.departTime),
          arrivalCity: flight.arrivalLocation,
          arrivalTime: this.formatTime(flight.arrivalTime),
          remainingSeats: flight.seatsRemaining,
          class: flight.class || 'ECONOMY',
          price: `$${(flight.price || 1000).toFixed(2)} CAD`,
          dateRange: `${this.formatDate(flight.departTime)} to ${this.formatDate(flight.arrivalTime)}`,
          departureDate: this.formatDate(flight.departTime),
          returnDate: this.formatDate(flight.arrivalTime),
          status: flight.status || 'Scheduled',
          flightNumber: flight.flightNumber
        }));

        console.log('Transformed flights:', this.flights);
        // Note: isSearchActive.value is irrelevant in this Options API context, so it's omitted.

      } catch (e) {
        console.error('Failed to fetch flights:', e);
        this.error = e.message || 'The backend service is unavailable.';
        // Add fallback to demo data on failure
        this.flights = this.getDemoFlights();
      } finally {
        this.loading = false;
      }
    },

    // Demo data for fallback, now matching the transformed structure better
    getDemoFlights() {
      return [
        { id: 'FL101', route: 'JFK to LAX', departureCity: 'JFK', arrivalCity: 'LAX', departureTime: '08:00 AM', arrivalTime: '11:00 AM', remainingSeats: 50, class: 'ECONOMY', price: '$299.99 CAD', dateRange: 'May 10 to May 10', departureDate: 'May 10', returnDate: 'May 10', status: 'Scheduled', flightNumber: 'B737-101' },
        { id: 'FL204', route: 'SFO to DFW', departureCity: 'SFO', arrivalCity: 'DFW', departureTime: '02:30 PM', arrivalTime: '07:30 PM', remainingSeats: 20, class: 'BUSINESS', price: '$850.00 CAD', dateRange: 'May 11 to May 11', departureDate: 'May 11', returnDate: 'May 11', status: 'Delayed', flightNumber: 'A320-204' },
        { id: 'FL330', route: 'ATL to MIA', departureCity: 'ATL', arrivalCity: 'MIA', departureTime: '07:45 PM', arrivalTime: '09:30 PM', remainingSeats: 120, class: 'ECONOMY', price: '$125.00 CAD', dateRange: 'May 10 to May 10', departureDate: 'May 10', returnDate: 'May 10', status: 'Canceled', flightNumber: 'CRJ-330' },
      ];
    }
  },
  mounted() {
    this.fetchFlights();
  }
};
</script>