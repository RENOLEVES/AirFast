<template>
  <div class="flight-form-container w-[612px] h-[881px] bg-white rounded-2xl shadow-xl p-10 font-['Montserrat'] overflow-y-auto">

    <header class="mb-8">
      <h1 class="text-[#484848] text-4xl font-extrabold">
        Update Flight
      </h1>
      </header>

    <form @submit.prevent="updateFlight">
      
      <div class="mb-6">
        <label for="capacity" class="text-[#484848] text-sm font-semibold block mb-2">Capacity</label>
        <div class="input-wrapper">
          <input 
            id="capacity" 
            v-model.number="formData.capacity" 
            type="number" 
            placeholder="E.g., 200"
            class="input-field"
            required
          >
        </div>
      </div>

      <div class="mb-6">
        <label class="text-[#484848] text-sm font-semibold block mb-2">Expected Depart Time</label>
        <div class="flex space-x-2">
          <div class="date-time-mock-pill">
            <span>{{ departureDateDisplay }}</span>
          </div>
          <div class="date-time-mock-pill">
            <span>{{ departureTimeDisplay }}</span>
          </div>
        </div>
        </div>

      <div class="mb-6">
        <label for="departLocation" class="text-[#484848] text-sm font-semibold block mb-2">Depart Location</label>
        <div class="input-wrapper">
          <input 
            id="departLocation" 
            v-model="formData.departLocation" 
            type="text" 
            placeholder="E.g., Montreal (YUL)"
            class="input-field"
            required
          >
        </div>
      </div>

      <div class="mb-6">
        <label for="arrivalLocation" class="text-[#484848] text-sm font-semibold block mb-2">Arrival Location</label>
        <div class="input-wrapper">
          <input 
            id="arrivalLocation" 
            v-model="formData.arrivalLocation" 
            type="text" 
            placeholder="E.g., Toronto (YYZ)"
            class="input-field"
            required
          >
        </div>
      </div>

      <div class="mb-6">
        <label for="flightNumber" class="text-[#484848] text-sm font-semibold block mb-2">Flight Number</label>
        <div class="input-wrapper">
          <input 
            id="flightNumber" 
            v-model="formData.flightNumber" 
            type="text" 
            placeholder="E.g., AC130"
            class="input-field"
            required
          >
        </div>
      </div>

      <div class="mb-6">
        <label for="flightTime" class="text-[#484848] text-sm font-semibold block mb-2">Flight Time (minutes)</label>
        <div class="input-wrapper">
          <input 
            id="flightTime" 
            v-model.number="formData.flightTime" 
            type="number" 
            placeholder="E.g., 90"
            class="input-field"
            required
          >
        </div>
      </div>

      <div class="mb-8">
        <label class="text-[#484848] text-sm font-semibold block mb-3">Is Recurring?</label>
        <div class="flex space-x-4">
          <div class="radio-button-group">
            <input type="radio" id="recurringYes" value="true" v-model="formData.isRecurring" class="hidden">
            <label for="recurringYes" :class="['radio-label', { 'radio-selected': formData.isRecurring === 'true' }]">
              Yes
            </label>
          </div>
          <div class="radio-button-group">
            <input type="radio" id="recurringNo" value="false" v-model="formData.isRecurring" class="hidden">
            <label for="recurringNo" :class="['radio-label', { 'radio-selected': formData.isRecurring === 'false' }]">
              No
            </label>
          </div>
        </div>
      </div>

      <div class="mb-10">
        <label class="text-[#484848] text-sm font-semibold block mb-3">Flight Status</label>
        <div class="flex space-x-4">
          <div class="radio-button-group">
            <input type="radio" id="statusOnTime" value="On Time" v-model="formData.status" class="hidden">
            <label for="statusOnTime" :class="['radio-label-status', { 'radio-selected': formData.status === 'On Time' }]">
              On Time
            </label>
          </div>
          <div class="radio-button-group">
            <input type="radio" id="statusDelayed" value="Delayed" v-model="formData.status" class="hidden">
            <label for="statusDelayed" :class="['radio-label-status', { 'radio-selected': formData.status === 'Delayed' }]">
              Delayed
            </label>
          </div>
          <div class="radio-button-group">
            <input type="radio" id="statusCancelled" value="Cancelled" v-model="formData.status" class="hidden">
            <label for="statusCancelled" :class="['radio-label-status', { 'radio-selected': formData.status === 'Cancelled' }]">
              Cancelled
            </label>
          </div>
        </div>
      </div>

      <button 
        type="submit" 
        class="bg-[#9A9A9A] text-white text-base font-bold w-[187px] h-[54px] rounded-3xl hover:bg-gray-700 transition-colors duration-300"
      >
        Update Flight
      </button>

    </form>
    
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';

// Define reactive state for the form data
const formData = ref({
  capacity: null,
  expectedDepartTime: new Date(), // Use a Date object for real time data
  departLocation: '',
  arrivalLocation: '',
  flightNumber: '',
  flightTime: null, // in minutes
  isRecurring: 'false', // String 'true' or 'false' to match radio value
  status: 'On Time' // Default status
});

// Computed properties to display date/time in the format from the HTML mock
const departureDateDisplay = computed(() => {
  return formData.value.expectedDepartTime.toLocaleDateString('en-US', { month: 'short', day: 'numeric' }) + ', ' + formData.value.expectedDepartTime.getFullYear();
});

const departureTimeDisplay = computed(() => {
  return formData.value.expectedDepartTime.toLocaleTimeString('en-US', { hour: 'numeric', minute: '2-digit', hour12: true });
});

// Form submission handler
const updateFlight = () => {
  // Logic to process the new flight data
  console.log('Updating flight with data:', {
    ...formData.value,
    isRecurring: formData.value.isRecurring === 'true' // Convert string back to boolean for submission
  });
  
  // In a real application, you would make an API call here.
  alert('Flight creation simulated. Check console for data.');
  
  // Optional: Reset form after submission
  // resetForm(); 
};

// Function to reset the form (if needed)
// const resetForm = () => {
//   formData.value = {
//     capacity: null,
//     expectedDepartTime: new Date(),
//     departLocation: '',
//     arrivalLocation: '',
//     flightNumber: '',
//     flightTime: null,
//     isRecurring: 'false',
//     status: 'On Time'
//   };
// };
</script>

<style scoped>
/* Base Input Field Styling */
.input-wrapper {
  @apply w-full h-[60px] bg-white rounded-[30px] border border-[#C2C6CC] overflow-hidden flex items-center px-6;
}

.input-field {
  @apply w-full text-[#484848] text-base font-medium placeholder-[#C2C6CC] focus:outline-none;
}

/* Date/Time Picker Mockup Styling (as seen in the original HTML) */
.date-time-mock-pill {
  @apply px-4 py-2 bg-gray-100 rounded-lg text-blue-600 text-base font-normal cursor-pointer hover:bg-gray-200 transition-colors;
}

/* Radio Button Styling (for Is Recurring & Status) */
.radio-button-group {
  /* This wrapper helps manage the hidden input and the visible label */
  @apply relative;
}

.radio-label {
  /* Style for 'Yes' / 'No' buttons */
  @apply block w-[109px] h-[34px] text-center leading-[34px] rounded-[30px] bg-[#9A9A9A] text-white text-sm font-bold cursor-pointer transition-colors duration-200;
}

.radio-label-status {
  /* Style for 'On Time' / 'Delayed' / 'Cancelled' buttons - slightly variable width */
  @apply block min-w-[109px] h-[34px] px-4 text-center leading-[34px] rounded-[30px] bg-[#9A9A9A] text-white text-sm font-bold cursor-pointer transition-colors duration-200;
}

.radio-selected {
  /* Visually indicate the selected button */
  @apply bg-gray-700 shadow-md;
}
</style>