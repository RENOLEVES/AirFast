<template>
  <div class="view-all-seats p-4 h-full flex flex-col">

    <div v-if="loading" class="text-center py-10 text-indigo-500 font-semibold text-lg flex-grow">
      Loading seats...
    </div>

    <div v-else-if="error" class="text-center py-10 bg-red-50 border border-red-200 p-6 rounded-lg flex-grow">
      <h3 class="text-red-600 font-bold mb-2">Error Fetching Data</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchSeats" class="mt-4 px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600">Try Again</button>
    </div>

    <div v-else class="seat-list-container flex flex-col flex-grow overflow-hidden">

      <div class="flex items-center font-bold text-sm text-gray-700 bg-gray-200 border-b border-gray-300 p-3 rounded-t-lg shadow-sm flex-shrink-0 pl-5">
        <div class="column w-1/12">SEAT #</div>
        <div class="column w-2/12">FLIGHT ID</div> <div class="column w-2/12">STATUS</div>
        <div class="column w-2/12">CLASS</div>
        <div class="column w-2/12">PRICE</div>
        <div class="column w-3/12">SEAT ID</div>
      </div>

      <div class="overflow-y-auto flex-grow">
        <template v-for="(item, index) in groupedSeats">

          <div
              v-if="item.isHeader"
              :key="'header-' + item.flightId"
              class="bg-indigo-100 text-indigo-800 font-extrabold text-sm p-2 mt-4 sticky top-0 z-10 border-y border-indigo-300 shadow-inner"
          >
            FLIGHT ID: <span class="pl-2">{{ item.flightId }} ({{ item.count }} Seats)</span>
          </div>

          <SeatCard
              v-else
              :key="'seat-' + item.seat.seatId"
              :seat="item.seat"
          />
        </template>
      </div>

      <div v-if="seats.length > 0" class="text-center py-4 mt-2 text-gray-500 border-t pt-4 flex-shrink-0">
        Showing {{ seats.length }} seats.
      </div>

    </div>
  </div>
</template>

<script>
import SeatCard from "../components/SeatCard.vue";

export default {
  name: 'ViewAllSeats',
  components: {SeatCard},
  data() {
    return {
      seats: [],
      loading: true,
      error: null,
      apiUrl: 'http://localhost:8080/api/owners/view/seat',
    };
  },
  computed: {
    groupedSeats() {
      if (!this.seats || this.seats.length === 0) {
        return [];
      }

      const grouped = [];
      let currentFlightId = null;
      let groupCount = 0;

      const sortedSeats = [...this.seats].sort((a, b) => {
        return a.flightId - b.flightId;
      });

      for (const seat of sortedSeats) {
        if (seat.flightId !== currentFlightId) {
          if (currentFlightId !== null) {
          }

          currentFlightId = seat.flightId;
          groupCount = 0;

          grouped.push({
            isHeader: true,
            flightId: currentFlightId,
          });
        }

        grouped.push({ isHeader: false, seat: seat });
      }

      const seatsByFlight = this.seats.reduce((acc, seat) => {
        const id = seat.flightId;
        if (!acc[id]) {
          acc[id] = [];
        }
        acc[id].push(seat);
        return acc;
      }, {});

      const finalGroupedList = [];
      for (const flightId in seatsByFlight) {
        const seatsInGroup = seatsByFlight[flightId];

        finalGroupedList.push({
          isHeader: true,
          flightId: flightId,
          count: seatsInGroup.length
        });

        seatsInGroup.forEach(seat => {
          finalGroupedList.push({ isHeader: false, seat: seat });
        });
      }

      return finalGroupedList;
    },

    groups() {
      return this.seats.reduce((acc, seat) => {
        acc[seat.flightId] = true;
        return acc;
      }, {});
    }
  },
  methods: {
    async fetchSeats() {
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const data = await response.json();
        this.seats = data.sort((a, b) => a.flightId - b.flightId);

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },
  },
  mounted() {
    this.fetchSeats();
  },
};
</script>