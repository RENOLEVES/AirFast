<template>
  <div class="view-all-bookings p-4 h-full flex flex-col">

    <!-- Loading state -->
    <div v-if="loading" class="text-center py-10 text-indigo-500 font-semibold text-lg flex-grow">
      Loading bookings...
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="text-center py-10 bg-red-50 border border-red-200 p-6 rounded-lg flex-grow">
      <h3 class="text-red-600 font-bold mb-2">Error Fetching Bookings</h3>
      <p class="text-red-500">{{ error }}</p>
      <button
        @click="fetchBookings"
        class="mt-4 px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600"
      >
        Try Again
      </button>
    </div>

    <!-- Data state -->
    <div v-else class="flex flex-col flex-grow">
      <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 overflow-y-auto flex-grow">
        <BookingCard
          v-for="booking in bookings"
          :key="booking.bookingId"
          :booking="booking"
        />
      </div>

      <div class="text-center py-6 mt-8 text-gray-500 border-t pt-6 flex-shrink-0">
        Showing {{ bookings.length }} bookings.
      </div>
    </div>
  </div>
</template>
<script>
import BookingCard from '../components/BookingCard.vue';
import { ownerAPI } from '@/api/service'

export default {
  name: 'ViewAllBookings',
  components: {
    BookingCard,
  },
  data() {
    return {
      bookings: [],
      loading: true,
      error: null,
    };
  },
  methods: {
    async fetchBookings() {
      this.loading = true;
      this.error = null;
      
      try {
        const data = await ownerAPI.getAllBookings();
        this.bookings = data || [];
      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.response?.data?.message || e.message || 'Backend unavailable';
      } finally {
        this.loading = false;
      }
    },
  },
  mounted() {
    this.fetchBookings();
  },
};
</script>

<style scoped>
/* Tailwind covers most styling */
</style>
