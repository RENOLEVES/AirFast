


<template>
  <div class="range-datepicker-container">

    <!--    range-->
    <div class="range-header">
      <label class="block font-semibold text-[14px] text-[#484848] mb-2">
        <i class="fas fa-map-marker-alt mr-2 text-blue-500"></i>Range<span class="info-icon">ⓘ</span>
      </label>
    </div>

    <!--    start date, end date-->
    <div
        class="date-range-display-wrapper w-full px-4 py-3 border border-gray-300 rounded-lg"
        @click="toggleCalendar"
    >
      <div class="date-input-box start-date">
        {{ startDateDisplay }}
      </div>
      <div class="date-input-box end-date">
        {{ endDateDisplay }}
      </div>
      <div class="calendar-icon-box">
        <span role="img" aria-label="calendar-icon">📅</span>
      </div>
    </div>

    <!--    toggle calendar-->
    <div class="calendar-popover" v-if="isCalendarOpen">
      <VDatePicker
          v-model="range"
          is-range
          borderless
          class="custom-calendar"
          :columns="2"
          title-position="middle"
          @update:model-value="toggleCalendar"
      />
    </div>
  </div>

</template>

<script setup>
import { ref, computed } from 'vue';
// Since you are using it locally, keep the import
import { DatePicker as VDatePicker } from 'v-calendar';


// New state variable to control the pop-up visibility
const isCalendarOpen = ref(false);

// Function to toggle the calendar's visibility
const toggleCalendar = () => {
  isCalendarOpen.value = !isCalendarOpen.value;
};

// 1. Reactive Data for the Date Range
const range = ref({
  start: new Date(2025, 10, 25),
  end: new Date(2025, 10, 30)
});

// 2. Date Formatting for Display (No changes needed here)
const formatDate = (date) => {
  if (!date) return 'N/A';
  return new Intl.DateTimeFormat('en-CA', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).format(date).replace(/\//g, '-');
};

const startDateDisplay = computed(() => {
  return formatDate(range.value.start);
});

const endDateDisplay = computed(() => {
  return formatDate(range.value.end);
});

// 3. Configure Date Masks (No changes needed here)
const masks = ref({
  title: 'YYYY MMMM',
  weekdays: 'WWW',
});
</script>

<style scoped>
/* 1. Structural Container */
.range-datepicker-container {
  /* Set position relative for the popover to position correctly */
  position: relative;
  font-family: Arial, sans-serif;
  border: none;
  z-index: 10;
}

/* 2. Range Label (Header) */
.range-header {
  padding: 0;
  font-weight: bold;
  font-size: 16px;
  color: #333;
  border-bottom: none;
}

/* 3. Date Range Input Wrapper (The element receiving the Tailwind classes) */
.date-range-display-wrapper {
  /* This class receives the Tailwind classes (w-full, px-4, py-3, border, rounded-lg)
     The styles below ensure its children are displayed correctly within that box. */
  display: flex;
  align-items: center; /* Vertically center content */
  cursor: pointer;
  /* Important: Remove redundant styles applied by the Tailwind classes */
  padding: 0;
  margin: 0;
  height: auto; /* Let the py-3 from the class control the height */
}

/* 4. Date Text Boxes (Start/End) */
.date-input-box {
  flex-grow: 1;
  padding: 0 5px;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: #333;
  font-size: 16px;
  font-weight: 500;
}

.date-input-box:first-child {
  border-right: 1px solid #e0e0e0;
}

/* 5. Calendar Icon Box */
.calendar-icon-box {
  padding: 0 5px 0 10px; /* Adjusted padding to look better next to the date text */
  background-color: transparent; /* Remove background color */
  border-left: 1px solid #e0e0e0;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #54c1f3;
  flex-shrink: 0;
}

/* 6. Popover Positioning */
.calendar-popover {
  position: absolute;
  top: 45px;
  left: 0;
  z-index: 9999;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

/* --- Input Boxes (Start/End) --- */
.date-input-box {
  padding: 0 5px; /* Adjust padding for height */
  display: flex;
  align-items: center; /* Center the text vertically */
  justify-content: center;
  text-align: center;
  flex-grow: 1;
  color: #333;
  font-size: 16px;
  font-weight: 500;
}

.date-input-box:first-child {
  border-right: 1px solid #e0e0e0;
}

/* --- Popover Positioning (Ensure it's attached to the new wrapper's boundary) --- */
.calendar-popover {
  position: absolute;
  /* Top value adjusted to sit directly below the input boxes (approx 40px input height + 5px margin from header) */
  top: 45px;
  left: 0;
  z-index: 9999;
  background-color: white;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
.info-icon {
  color: #54c1f3; /* Red color for info icon */
  font-size: 14px;
  margin-left: 5px;
}

/* --- Display Boxes for Selected Range --- */
.date-range-display {
  display: flex;
  margin: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
}

.date-input-box {
  padding: 10px 15px;
  text-align: center;
  flex-grow: 1;
  color: #333;
  font-size: 16px;
  font-weight: 500;
}

.date-input-box:first-child {
  border-right: 1px solid #e0e0e0;
}

.calendar-icon-box {
  padding: 10px;
  background-color: #f5f5f5;
  border-left: 1px solid #e0e0e0;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: #54c1f3; /* Red icon color */
}

/* --- Calendar Styling Overrides (using :deep for Vue 3 component styling) --- */
.custom-calendar {
  border: none !important;
  box-shadow: none !important;
  padding: 15px;
}

/* Target the header/month title */
:deep(.vc-title) {
  font-size: 18px;
  font-weight: bold;
}

/* --- Selection Highlighting (Red for start/end, Lighter for middle) --- */


/* Highlight color for the *start* and *end* of the range */
:deep(.vc-highlight-content-solid) {
  background-color: #0061ef !important; /* Lighter red */
  color: #333;
  border-radius: 0 !important;
}

/* Highlight color for the *middle* of the range */
:deep(.vc-highlight-content-light) {
  background-color: #54c1f3 !important; /* Your image's red color */
  border-radius: 0 !important;
}

/* Ensure day text is white on the start/end dates */
:deep(.vc-highlight-content-light .vc-day-content) {
  color: white !important;
}
</style>