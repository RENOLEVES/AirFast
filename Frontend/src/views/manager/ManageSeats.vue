<template>
  <div class="manage-seats h-full flex flex-col">
    <h1 class="text-3xl font-bold text-gray-800 mb-6">Manage Seat Prices</h1>

    <!-- Loading/Error States -->
    <div v-if="loading" class="flex-grow flex items-center justify-center text-indigo-600">
      Loading seats...
    </div>
    <div v-else-if="error" class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-6 rounded-lg">
      <h3 class="text-red-600 font-bold mb-2">Error Loading Seats</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchSeats" class="mt-4 px-4 py-2 bg-indigo-600 text-white rounded hover:bg-indigo-700">
        Try Again
      </button>
    </div>

    <!-- Seats Grid -->
    <div v-else class="overflow-y-auto flex-grow">
      <!-- Group by Flight -->
      <div v-for="(flightSeats, flightId) in groupedSeats" :key="flightId" class="mb-8">
        <h2 class="text-xl font-bold text-indigo-700 mb-4 pb-2 border-b-2 border-indigo-200">
          Flight ID: {{ flightId }} ({{ flightSeats.length }} seats)
        </h2>
        
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
          <div v-for="seat in flightSeats" :key="seat.seatId" class="bg-white rounded-lg shadow-md p-4 border border-gray-200 hover:shadow-lg transition">
            <div class="flex justify-between items-start mb-3">
              <div>
                <h3 class="text-lg font-bold text-gray-800">Seat {{ seat.seatNumber }}</h3>
                <p class="text-xs text-gray-500">ID: {{ seat.seatId }}</p>
              </div>
              <span :class="getClassBadge(seat.seatClass)" class="px-2 py-1 text-xs rounded-full font-semibold">
                {{ formatClass(seat.seatClass) }}
              </span>
            </div>

            <div class="space-y-1 mb-3 text-sm">
              <div class="flex justify-between">
                <span class="text-gray-600">Status:</span>
                <span :class="getStatusClass(seat.seatStatus)" class="font-medium">
                  {{ formatSeatStatus(seat.seatStatus) }}
                </span>
              </div>
              <div class="flex justify-between items-center">
                <span class="text-gray-600">Price:</span>
                <span class="text-lg font-bold text-green-600">${{ seat.price }}</span>
              </div>
            </div>

            <button @click="openPriceModal(seat)" class="w-full px-3 py-2 bg-blue-500 text-white text-sm rounded hover:bg-blue-600 transition">
              <i class="fas fa-dollar-sign mr-1"></i>Update Price
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Update Price Modal -->
    <div v-if="showPriceModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click.self="closePriceModal">
      <div class="bg-white rounded-xl p-8 max-w-md w-full mx-4">
        <h2 class="text-2xl font-bold mb-6">Update Seat Price</h2>
        
        <div class="mb-4 p-4 bg-gray-50 rounded-lg">
          <div class="grid grid-cols-2 gap-2 text-sm">
            <div><span class="text-gray-600">Flight:</span> <span class="font-medium">{{ selectedSeat?.flightId }}</span></div>
            <div><span class="text-gray-600">Seat:</span> <span class="font-medium">{{ selectedSeat?.seatNumber }}</span></div>
            <div><span class="text-gray-600">Class:</span> <span class="font-medium">{{ formatClass(selectedSeat?.seatClass) }}</span></div>
            <div><span class="text-gray-600">Current:</span> <span class="font-bold text-green-600">${{ selectedSeat?.price }}</span></div>
          </div>
        </div>

        <form @submit.prevent="updatePrice" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">New Price ($)</label>
            <input v-model.number="newPrice" type="number" step="0.01" min="0" required class="w-full px-4 py-3 border border-gray-300 rounded-lg text-lg focus:ring-2 focus:ring-indigo-500">
          </div>

          <div class="flex space-x-3 pt-4">
            <button type="submit" class="flex-1 px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition font-semibold">
              Update Price
            </button>
            <button type="button" @click="closePriceModal" class="px-4 py-2 bg-gray-300 text-gray-700 rounded-lg hover:bg-gray-400 transition">
              Cancel
            </button>
          </div>
        </form>
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
  name: 'ManageSeats',
  data() {
    return {
      seats: [],
      loading: true,
      error: null,
      showPriceModal: false,
      selectedSeat: null,
      newPrice: 0,
      successMessage: '',
      errorMessage: '',
      apiUrl: 'http://localhost:8080/api/owners/view/booking',
    }
  },
  computed: {
    groupedSeats() {
      return this.seats.reduce((acc, seat) => {
        const flightId = seat.flightId;
        if (!acc[flightId]) acc[flightId] = [];
        acc[flightId].push(seat);
        return acc;
      }, {});
    }
  },
  mounted() {
    this.fetchSeats();
  },
  methods: {
    async fetchSeats() {
      this.isLoading = true;
      this.error = null;
      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }
        const data = await response.json();

        this.seats = data;

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },

    openPriceModal(seat) {
      this.selectedSeat = seat;
      this.newPrice = seat.price;
      this.showPriceModal = true;
    },

    closePriceModal() {
      this.showPriceModal = false;
      this.selectedSeat = null;
      this.newPrice = 0;
    },

    async updatePrice() {
      try {
        await managerAPI.setSeatPrice(this.selectedSeat.seatId, this.newPrice);
        this.showSuccess(`Seat ${this.selectedSeat.seatNumber} price updated to $${this.newPrice}`);
        this.closePriceModal();
        this.fetchSeats();
      } catch (e) {
        this.showError(e.response?.data?.message || 'Failed to update seat price');
      }
    },

    formatClass(seatClass) {
      const classes = ['Business', 'Economy'];
      return classes[seatClass] || 'Unknown';
    },

    formatSeatStatus(status) {
      const statuses = ['TBD', 'Available'];
      return statuses[status] || 'Unknown';
    },

    getClassBadge(seatClass) {
      const badges = {
        0: 'bg-purple-100 text-purple-800',
        1: 'bg-blue-100 text-blue-800'
      };
      return badges[seatClass] || 'bg-gray-100 text-gray-800';
    },

    getStatusClass(status) {
      return status === 1 ? 'text-green-600' : 'text-yellow-600';
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