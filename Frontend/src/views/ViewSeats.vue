<template>
  <div class="view-all-seats p-4 h-full flex flex-col">

    <div v-if="loading" class="text-center py-10 text-indigo-500 font-semibold text-lg flex-grow">
      Loading seats...
    </div>

    <div v-else-if="error" class="text-center py-10 bg-red-50 border border-red-200 p-6 rounded-lg flex-grow">
      <h3 class="text-red-600 font-bold mb-2">Error Fetching Data</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchSeats" class="mt-4 px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600">
        Try Again
      </button>
    </div>

    <div v-else class="seat-list-container flex flex-col flex-grow overflow-hidden">

      <div
        class="flex items-center font-bold text-sm text-gray-700 bg-gray-200 border-b border-gray-300 p-3 rounded-t-lg shadow-sm flex-shrink-0 pl-5">
        <div class="column w-1/12">SEAT #</div>
        <div class="column w-2/12">FLIGHT ID</div>
        <div class="column w-2/12">STATUS</div>
        <div class="column w-2/12">CLASS</div>
        <div class="column w-2/12">PRICE</div>
        <div class="column w-3/12">SEAT ID</div>
      </div>

      <div class="overflow-y-auto flex-grow">
        <template v-for="item in groupedSeats" :key="item.isHeader ? 'header-' + item.flightId : 'seat-' + item.seat.seatId">

          <div
            v-if="item.isHeader"
            class="bg-indigo-100 text-indigo-800 font-extrabold text-sm p-2 mt-4 sticky top-0 z-10 border-y border-indigo-300 shadow-inner"
          >
            FLIGHT ID:
            <span class="pl-2">
              {{ item.flightId }} ({{ item.count }} Seats)
            </span>
          </div>

          <SeatCard
            v-else
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
import { ownerAPI } from '@/api/service'

export default {
  name: 'ViewAllSeats',
  components: { SeatCard },
  data() {
    return {
      seats: [],
      loading: true,
      error: null,
    };
  },
  computed: {
    groupedSeats() {
      if (!this.seats || this.seats.length === 0) return [];
      
      const byFlight = this.seats.reduce((acc, seat) => {
        const id = seat.flightId;
        if (!acc[id]) acc[id] = [];
        acc[id].push(seat);
        return acc;
      }, {});

      const result = [];
      Object.keys(byFlight)
        .sort((a, b) => Number(a) - Number(b))
        .forEach(flightId => {
          const groupSeats = byFlight[flightId].sort((a, b) => 
            a.seatNumber.localeCompare(b.seatNumber)
          );
          result.push({ isHeader: true, flightId, count: groupSeats.length });
          groupSeats.forEach(seat => result.push({ isHeader: false, seat }));
        });
      return result;
    },
  },
  methods: {
    async fetchSeats() {
      this.loading = true;
      this.error = null;
      
      try {
        const data = await ownerAPI.getAllSeats();
        this.seats = (data || []).sort((a, b) => a.flightId - b.flightId);
      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.response?.data?.message || e.message || 'Backend unavailable';
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
