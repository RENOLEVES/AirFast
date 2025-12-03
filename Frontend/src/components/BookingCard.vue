<template>
  <div class="booking-card bg-white shadow-lg rounded-xl p-5 border-t-4 border-indigo-500 max-w-sm mx-auto my-4 w-[300px] h-[300px]">

    <div class="flex justify-between items-start mb-4 border-b pb-2">
      <h1 class="text-xl font-bold text-gray-800">Booking #{{ booking.bookingId }}
        <div class="text-sm text-gray-500">
        {{ formattedDate }}
        </div>
      </h1>

    </div>

    <div class="space-y-3">

      <div class="flex justify-between items-center text-gray-700">
        <div class="flex items-center space-x-2">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          <span class="font-medium">Customer ID:</span>
        </div>
        <span class="font-semibold text-indigo-600">{{ booking.customerId }}</span>
      </div>

      <div class="flex justify-between items-center text-gray-700">
        <div class="flex items-center space-x-2">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-indigo-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
          </svg>
          <span class="font-medium">Seat ID:</span>
        </div>
        <span class="font-semibold">{{ booking.seatId }}</span>
      </div>

    </div>

    <div class="mt-4 pt-4 border-t border-gray-200 space-y-3">

      <div class="flex justify-between items-center">
        <span class="text-sm font-medium text-gray-600">Payment Status:</span>
        <span :class="paymentStatusClasses" class="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full">
          {{ booking.paymentStatus }}
        </span>
      </div>

      <div class="flex justify-between items-center">
        <span class="text-sm font-medium text-gray-600">Booking Status:</span>
        <span :class="bookingStatusClasses" class="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full">
          {{ booking.bookingStatus }}
        </span>
      </div>

    </div>
  </div>
</template>

<script>
const BookingResponseDTO = {
  bookingId: Number,
  customerId: Number,
  seatId: Number,
  bookingDate: [String, Date],
  paymentStatus: String,
  bookingStatus: String,
};

export default {
  name: 'BookingCard',
  props: {
    booking: {
      type: Object,
      required: true,
      validator: (value) => {
        return ['bookingId', 'customerId', 'seatId', 'paymentStatus', 'bookingStatus'].every(key => key in value);
      }
    }
  },
  computed: {
    formattedDate() {
      if (!this.booking.bookingDate) return 'N/A';

      const date = new Date(this.booking.bookingDate);
      if (isNaN(date)) return 'Invalid Date';

      return date.toLocaleDateString(undefined, {
        year: 'numeric',
        month: 'short',
        day: 'numeric',
        hour: '2-digit'
      });
    },

    paymentStatusClasses() {
      const status = this.booking.paymentStatus.toUpperCase();
      switch (status) {
        case 'PAID':
          return 'bg-green-100 text-green-800';
        case 'PENDING':
          return 'bg-yellow-100 text-yellow-800';
        case 'FAILED':
          return 'bg-red-100 text-red-800';
        default:
          return 'bg-gray-200 text-gray-800';
      }
    },

    bookingStatusClasses() {
      const status = this.booking.bookingStatus.toUpperCase();
      switch (status) {
        case 'CONFIRMED':
          return 'bg-green-100 text-green-800';
        case 'WAITLISTED':
          return 'bg-yellow-100 text-yellow-800';
        case 'CANCELLED_BY_CUSTOMER':
          return 'bg-red-100 text-red-800';
        case 'CANCELLED_BY_AIRLINE':
          return 'bg-red-100 text-red-800';
        default:
          return 'bg-gray-200 text-gray-800';
      }

    }
  }
}
</script>