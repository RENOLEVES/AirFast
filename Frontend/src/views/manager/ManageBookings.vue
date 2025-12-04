<template>
  <div class="manage-bookings h-full flex flex-col">
    <h1 class="text-3xl font-bold text-gray-800 mb-6">Manage Bookings</h1>

    <!-- Loading/Error States -->
    <div v-if="loading" class="flex-grow flex items-center justify-center text-indigo-600">
      Loading bookings...
    </div>
    <div v-else-if="error" class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-6 rounded-lg">
      <h3 class="text-red-600 font-bold mb-2">Error Loading Bookings</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchBookings" class="mt-4 px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700">
        Try Again
      </button>
    </div>

    <!-- Bookings Grid -->
    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 overflow-y-auto flex-grow">
      <div v-for="booking in bookings" :key="booking.bookingId" class="bg-white rounded-lg shadow-md p-6 border border-gray-200 hover:shadow-lg transition h-[240px]">
        <div class="flex justify-between items-start mb-4 ">
          <div>
            <h3 class="text-xl font-bold text-gray-800">Booking #{{ booking.bookingId }}</h3>
            <p class="text-sm text-gray-500">{{ formatDate(booking.bookingDate) }}</p>
          </div>
          <span :class="getStatusBadge(booking.bookingStatus)" class="px-3 py-1 text-xs rounded-full font-semibold">
            {{ formatStatus(booking.bookingStatus) }}
          </span>
        </div>

        <div class="space-y-2 mb-4 text-sm">
          <div class="flex justify-between">
            <span class="text-gray-600">Customer ID:</span>
            <span class="font-medium">{{ booking.customerId || 'N/A' }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Seat ID:</span>
            <span class="font-medium">{{ booking.seatId }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-600">Payment:</span>
            <span :class="getPaymentStatusClass(booking.paymentStatus)" class="font-medium">
              {{ formatPaymentStatus(booking.paymentStatus) }}
            </span>
          </div>
        </div>

        <button @click="deleteBooking(booking.bookingId)" class="w-full px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition">
          <i class="fas fa-trash mr-2"></i>Delete Booking
        </button>
      </div>
    </div>

    <!-- Messages -->
    <div v-if="successMessage" class="fixed bottom-4 right-4 bg-green-500 text-white px-6 py-3 rounded-lg shadow-lg">
      {{ successMessage }}
    </div>
    <div v-if="errorMessage" class="fixed bottom-4 right-4 bg-red-500 text-white px-6 py-3 rounded-lg shadow-lg">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'ManageBookings',
  data() {
    return {
      bookings: [],
      loading: true,
      error: null,
      successMessage: '',
      errorMessage: '',
      apiUrl: 'http://localhost:8080/api/bookings',
    }
  },
  mounted() {
    this.fetchBookings();
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

    async deleteBooking(bookingId) {
      if (!confirm(`Are you sure you want to delete booking #${bookingId}?`)) return;
      const deleteUrl = `http://localhost:8080/api/bookings/${bookingId}`;

      try {
        const response = await fetch(deleteUrl, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json'
          }
        });
        if (!response.ok) {
          let errorMessage = `Failed to delete booking. Status: ${response.status}.`;

          try {
            const backendData = await response.text();
            try {
              const jsonBody = JSON.parse(backendData);
              errorMessage = jsonBody.message || jsonBody.error || errorMessage;
            } catch {
              errorMessage = backendData || errorMessage;
            }
          } catch (e) {
          }

          throw new Error(errorMessage);
        }

        this.showSuccess(`Booking #${bookingId} deleted successfully`);
        this.fetchBookings();

      } catch (e) {
        console.error('Delete error:', e);
        this.showError(e.message || 'Failed to delete booking.');
      }
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' });
    },

    formatStatus(status) {
      return status || 'Unknown';
    },

    formatPaymentStatus(status) {
      const statusMap = {
        'NOT_PAID': 'Not Paid',
        'PAID': 'Paid'
      };
      return statusMap[status] || status || 'Unknown';
    },

    getStatusBadge(status) {
      const statusUpper = status?.toUpperCase();
      const badges = {
        'CONFIRMED': 'bg-green-100 text-green-800',
        'WAITLISTED': 'bg-yellow-100 text-yellow-800',
        'CANCELLED_BY_CUSTOMER': 'bg-red-100 text-red-800',
        'CANCELLED_BY_AIRLINE': 'bg-red-100 text-red-800'
      };
      return badges[statusUpper] || 'bg-gray-100 text-gray-800';
    },

    getPaymentStatusClass(status) {
      const statusUpper = status?.toUpperCase();
      return statusUpper === 'PAID' ? 'text-green-600' : 'text-red-600';
    },

    showSuccess(message) {
      this.successMessage = message;
      setTimeout(() => this.successMessage = '', 3000);
    },

    showError(message) {
      this.errorMessage = message;
      setTimeout(() => this.errorMessage = '', 5000);
    }
  }
}
</script>