<template>
  <div class="view-all-bookings p-4 h-full flex flex-col">

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6 overflow-y-auto flex-grow">

      <BookingCard
          v-for="booking in bookings"
          :key="booking.bookingId"
          :booking="booking"
      />

    </div>

    <div v-if="!loading && !error" class="text-center py-6 mt-8 text-gray-500 border-t pt-6 flex-shrink-0">
      Showing {{ bookings.length }} bookings.
    </div>
  </div>
</template>

<script>
import BookingCard from '../components/BookingCard.vue';

export default {
  name: 'ViewAllBookings',
  components: {
    BookingCard,
  },
  data() {
    return {
      bookings: [],
      isLoading: true,
      error: null,
      apiUrl: 'http://localhost:8080/api/owners/view/booking',
    };
  },
  methods: {
    async fetchBookings() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const data = await response.json();

        this.bookings = data;

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
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
/* Scoped styles can be used here if needed, but Tailwind handles most styling */
/* The "grid grid-cols..." combined with "overflow-y-auto flex-grow" in the template
   ensures the card list scrolls independently within its container. */
</style>