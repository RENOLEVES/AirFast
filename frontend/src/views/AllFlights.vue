<template>
  <div class="view-all-flights p-4 h-full flex flex-col">

    <div class="flight-header flex items-center bg-gray-100 p-3 rounded-t-lg shadow-sm border-b border-gray-300 font-semibold text-gray-600 sticky top-0 z-10">
      <div class="column w-1/12">ID</div>
      <div class="column w-2/12">Route</div>
      <div class="column w-2/12">Departure</div>
      <div class="column w-2/12">Arrival</div>
      <div class="column w-2/12">Status</div>
      <div class="column w-2/12">Seats/Price</div>
    </div>

    <div class="flight-list overflow-y-auto flex-grow rounded-b-lg border-x border-b border-gray-200">
      <div
          v-for="flight in flights"
          :key="flight.id"
          class="flight-row flex items-center border-t border-gray-200 bg-white hover:bg-indigo-50 transition-colors duration-150 cursor-pointer"
          @click="viewFlightDetails(flight)"
      >
        <div class="column w-1/12 font-bold text-lg text-indigo-700">
          {{ flight.flightNumber }}
        </div>

        <div class="column w-2/12 font-medium text-gray-800" :title="flight.route">
          {{ flight.departureCity }} → {{ flight.arrivalCity }}
        </div>

        <div class="column w-2/12 text-sm text-gray-700">
          <span class="font-semibold">{{ flight.departureTime }}</span>
          <div class="text-xs text-gray-500">{{ flight.departureDate }}</div>
        </div>

        <div class="column w-2/12 text-sm text-gray-700">
          <span class="font-semibold">{{ flight.arrivalTime }}</span>
          <div class="text-xs text-gray-500">{{ flight.returnDate }}</div>
        </div>

        <div class="column w-2/12">
          <span :class="getStatusClasses(flight.status)" class="px-3 py-1 text-xs leading-5 font-semibold rounded-full">
            {{ formatStatus(flight.status) }}
          </span>
        </div>

        <div class="column w-2/12 text-sm text-gray-700">
          <span class="font-semibold text-green-600">{{ flight.price }}</span>
          <div class="text-xs text-gray-500">{{ flight.remainingSeats }} seats left</div>
        </div>

      </div>

      <div v-if="loading" class="p-8 text-center text-indigo-600">Loading flights...</div>
      <div v-if="error" class="p-8 text-center text-red-600">Error fetching data: {{ error }}</div>
      <div v-if="!loading && !flights.length && !error" class="p-8 text-center text-gray-500">No flights found.</div>
    </div>

    <div v-if="!loading && !error" class="text-center py-6 mt-4 text-gray-500 border-t pt-6 flex-shrink-0">
      Showing {{ flights.length }} results.
    </div>
  </div>
</template>

<script>

export default {
  name: 'AllFlights',
  data() {
    return {
      flights: [],
      loading: false,
      error: null,
      apiUrl: 'http://localhost:8080/api/flights',
    };
  },
  methods: {
    // New formatting and utility methods
    formatStatus(status) {
      if (!status) return 'N/A';
      const s = status.toLowerCase().replace(/_/g, ' ');
      return s.charAt(0).toUpperCase() + s.slice(1);
    },
    getStatusClasses(status) {
      const s = (status || '').toUpperCase();
      if (s === 'SCHEDULED') return 'bg-green-100 text-green-800';
      if (s === 'DELAYED') return 'bg-yellow-100 text-yellow-800';
      if (s === 'CANCELED' || s === 'CANCELLED') return 'bg-red-100 text-red-800';
      return 'bg-gray-200 text-gray-800';
    },
    viewFlightDetails(flight) {
      console.log('Viewing details for flight:', flight.id);
    },

    formatTime(dateTimeString) {
      if (!dateTimeString) return 'N/A';
      try {
        const date = new Date(dateTimeString);
        return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit' });
      } catch {
        return 'Invalid Time';
      }
    },
    formatDate(dateTimeString) {
      if (!dateTimeString) return 'N/A';
      try {
        const date = new Date(dateTimeString);
        return date.toLocaleDateString('en-CA', { year: 'numeric', month: 'short', day: '2-digit' });
      } catch {
        return 'Invalid Date';
      }
    },
    async fetchFlights() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const backendData = await response.json();
        console.log('Backend response:', backendData);

        this.flights = backendData.map(flight => ({
          id: flight.flightId || flight.id,
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

      } catch (e) {
        console.error('Failed to fetch flights:', e);
        this.error = e.message || 'The backend service is unavailable.';
        this.flights = this.getDemoFlights();
      } finally {
        this.loading = false;
      }
    },
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

<style scoped>
.flight-row {
  padding: 12px 16px;
  min-height: 50px;
  width: 100%;
}

.flight-header {
  padding: 12px 16px;
}

.column {
  padding: 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>