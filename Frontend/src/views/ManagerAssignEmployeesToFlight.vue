<template>
  <div class="flight-assignment-container w-[1366px] h-[1801px] p-20 bg-white rounded-2xl overflow-y-auto font-['Montserrat']">

    <header class="mb-12">
      <h1 class="text-[#484848] text-[38px] font-extrabold">
        Assign Pilots and Flight Attendants to Flight {{ flightNumber }}
      </h1>
      </header>

    <div class="employee-grid grid grid-cols-3 gap-x-8 gap-y-6">
      <div 
        v-for="employee in availableEmployees" 
        :key="employee.id"
        class="employee-card"
        :class="{ 'bg-gray-100': !employee.isAssigned, 'bg-green-100 border-green-400': employee.isAssigned }"
      >
        <div class="employee-info p-5">
          <h2 class="text-[#484848] text-base font-bold mb-0.5">{{ employee.name }}</h2>
          <p class="text-[#9A9A9A] text-sm font-medium">{{ employee.role }}</p>
          <p class="text-[rgba(154,154,154,0.81)] text-xs font-medium">{{ employee.employeeId }}</p>
          </div>

        <button
          @click="toggleAssignment(employee)"
          :disabled="employee.isAssigned"
          :class="['assign-button', { 'assigned-btn': employee.isAssigned, 'unassigned-btn': !employee.isAssigned }]"
        >
          {{ employee.isAssigned ? 'Assigned' : 'Assign To Flight' }}
        </button>
      </div>
    </div>

    <hr class="mt-16 mb-4">

    <div class="text-center mt-10 text-[#484848] text-base font-bold">
      Paginations or Load on scroll...
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

// The flight number is likely passed as a prop, but mocked here for context
const flightNumber = ref('AC130');

// Mock Data for Available Employees
const mockEmployees = [
  { id: 1, name: 'Vincent He', role: 'Pilot', email: 'vincenthe@gmail.com', employeeId: '123123144', isAssigned: false },
  { id: 2, name: 'Emily Clark', role: 'Pilot', email: 'emilyclark@gmail.com', employeeId: '123123145', isAssigned: false },
  { id: 3, name: 'John Smith', role: 'Flight Attendant', email: 'johnsmith@gmail.com', employeeId: '43242344441', isAssigned: false },
  { id: 4, name: 'Sarah Lee', role: 'Pilot', email: 'sarahlee@gmail.com', employeeId: '123123146', isAssigned: false },
  { id: 5, name: 'David Chen', role: 'Pilot', email: 'davidchen@gmail.com', employeeId: '123123147', isAssigned: false },
  { id: 6, name: 'Jessica Alba', role: 'Flight Attendant', email: 'jalba@gmail.com', employeeId: '43242344442', isAssigned: false },
  { id: 7, name: 'Michael B.', role: 'Pilot', email: 'michaelb@gmail.com', employeeId: '123123148', isAssigned: false },
  { id: 8, name: 'Laura King', role: 'Pilot', email: 'lauraking@gmail.com', employeeId: '123123149', isAssigned: false },
  { id: 9, name: 'Robert Jones', role: 'Flight Attendant', email: 'robertj@gmail.com', employeeId: '43242344443', isAssigned: false },
  // Adding one assigned employee to demonstrate the 'Assigned' state
  { id: 10, name: 'Assigned Crew', role: 'Pilot', email: 'assigned@gmail.com', employeeId: '123123150', isAssigned: true },
];

const availableEmployees = ref(mockEmployees);

/**
 * Toggles the assignment status of an employee.
 * In a real application, this would trigger an API call.
 * @param {object} employee The employee object to update.
 */
const toggleAssignment = (employee) => {
  if (!employee.isAssigned) {
    // Simulate API call success
    console.log(`Assigning employee ${employee.name} (${employee.role}) to Flight ${flightNumber.value}`);
    employee.isAssigned = true;
    
    // Optional: Alert or notification of success
    // alert(`${employee.name} has been assigned.`);
  } else {
    // Logic for unassigning, if needed (not in original HTML but good practice)
    console.log(`Employee ${employee.name} is already assigned.`);
  }
};
</script>

<style scoped>
/* Scoped styles using Tailwind to replicate the HTML inline styles */

.employee-card {
  @apply relative w-[316px] h-20 rounded-lg shadow-sm flex justify-between items-center transition-all duration-300;
  /* Original background: #EFF0F2 */
  background: #EFF0F2;
}

.employee-info {
  @apply w-3/5 overflow-hidden; /* Takes up left portion */
}

.assign-button {
  @apply h-[54px] w-[155px] rounded-[30px] text-white text-[15px] font-bold absolute right-[-20px] shadow-lg transition-colors duration-300;
}

.unassigned-btn {
  /* Style for 'Assign To Flight' button */
  @apply bg-[#9A9A9A] hover:bg-gray-700;
}

.assigned-btn {
  /* Style for when the employee is already assigned (disabled state) */
  @apply bg-green-600 cursor-not-allowed opacity-90;
}

/* Override the default background color for the assigned employee card */
.employee-card.bg-green-100 {
    background-color: #e6ffe6; /* Light green for assigned employees */
}
</style>