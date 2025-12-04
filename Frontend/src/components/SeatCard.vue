<template>
  <div
      :class="['seat-row', 'flex', 'items-center', 'border', 'border-gray-200', 'bg-white', 'hover:bg-gray-50', 'transition-colors', 'duration-150', statusClasses]"
      @click="selectSeat"
  >

    <div class="column w-1/12 font-bold text-lg text-indigo-700">
      {{ seat.seatNumber }}
    </div>

    <div class="column w-2/12 text-sm text-gray-500">
      {{ seat.flightId }}
    </div>

    <div class="column w-2/12">
      <span :class="seatStatusClasses" class="px-3 py-1 text-xs leading-5 font-semibold rounded-full">
        {{ formattedSeatStatus }}
      </span>
    </div>

    <div class="column w-2/12">
      <span :class="seatClassClasses" class="px-3 py-1 text-xs leading-5 font-semibold rounded-full">
        {{ formattedSeatClass }}
      </span>
    </div>

    <div class="column w-2/12 font-semibold text-green-600">
      {{ formattedPrice }}
    </div>

    <div class="column w-3/12 text-sm text-gray-400">
      ID: {{ seat.seatId }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'SeatRow',
  props: {
    seat: {
      type: Object,
      required: true,
      validator: (value) => {
        return ['seatId', 'flightId', 'seatClass', 'price', 'seatNumber', 'seatStatus'].every(key => key in value);
      }
    },
    isSelected: {
      type: Boolean,
      default: false,
    }
  },
  computed: {
    // --- Formatting Helpers ---
    formattedSeatClass() {
      if (!this.seat.seatClass) return 'N/A';
      const s = this.seat.seatClass.toLowerCase();
      return s.charAt(0).toUpperCase() + s.slice(1);
    },

    formattedSeatStatus() {
      if (!this.seat.seatStatus) return 'N/A';
      const s = this.seat.seatStatus.toLowerCase();
      return s.charAt(0).toUpperCase() + s.slice(1).replace(/([a-z])([A-Z])/g, '$1 $2');
    },

    formattedPrice() {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'CAD'
      }).format(this.seat.price);
    },

    // --- Dynamic Class Logic (Badges) ---
    seatClassClasses() {
      const seatClass = this.seat.seatClass.toUpperCase();
      switch (seatClass) {
        case 'FIRST':
          return 'bg-yellow-100 text-yellow-800';
        case 'BUSINESS':
          return 'bg-blue-100 text-blue-800';
        case 'ECONOMY':
          return 'bg-green-100 text-green-800';
        default:
          return 'bg-gray-200 text-gray-800';
      }
    },

    seatStatusClasses() {
      const status = this.seat.seatStatus.toUpperCase();
      switch (status) {
        case 'AVAILABLE':
          return 'bg-green-100 text-green-800';
        case 'OCCUPIED':
        case 'BLOCKED':
          return 'bg-red-100 text-red-800';
        case 'TO_BE_DETERMINED':
        case 'TOBEDETERMINED':
          return 'bg-yellow-100 text-yellow-800';
        default:
          return 'bg-gray-200 text-gray-800';
      }
    },

    // --- Overall Row Interactivity/Status Classes ---
    statusClasses() {
      const status = this.seat.seatStatus.toLowerCase();

      // Selected State
      if (this.isSelected) {
        return 'bg-indigo-50 border-indigo-500 border-l-4 shadow-md ring-2 ring-indigo-100 cursor-pointer';
      }

      // Available State
      if (status === 'available') {
        return 'border-l-4 border-l-transparent cursor-pointer';
      }

      // Unavailable State
      return 'bg-gray-100 opacity-80 cursor-not-allowed border-l-4 border-l-gray-400';
    }
  },
  methods: {
    selectSeat() {
      if (this.seat.seatStatus === 'AVAILABLE') {
        this.$emit('seat-selected', this.seat.seatId);
      }
    }
  }
};
</script>

<style scoped>
.seat-row {
  /* Ensure minimal height and consistent padding for a "table row" look */
  padding: 12px 16px;
  min-height: 50px;
  width: 100%;
}

.column {
  /* Apply consistent padding to column contents for separation */
  padding: 0 8px;
  /* Ensure text doesn't wrap unnecessarily */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>