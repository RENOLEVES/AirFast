<template>
  <div>
    <div class="fixed top-0 w-full bg-white shadow-md z-50 p-4 mx-auto pl-20">
      <div class="flex justify-between items-center w-full">
        <div>
          <h1 class="font-extrabold text-[20px] text-[#484848]">
            <i class="fas fa-plane text-blue-500"></i>Air Fast Manager
          </h1>
          <p class="text-[#9a9a9a]">Operations & Employee Management</p>
        </div>
        <div class="relative pr-[100px]">
          <button @click="toggleDropdown" class="flex items-center space-x-2 text-[#484848] font-semibold text-lg p-2 rounded-full hover:bg-gray-200 transition">
            <i class="fas fa-user-circle text-2xl text-blue-600"></i>
            <span class="hidden sm:inline">Manager</span>
            <i class="fas" :class="isDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </button>
          <div v-if="isDropdownOpen" class="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-lg shadow-xl z-10 overflow-hidden">
            <button @click="$emit('navigate', 'FlightBooking')" class="w-full text-left px-4 py-3 text-red-500 hover:bg-red-50 transition block font-medium">
              <i class="fas fa-sign-out-alt mr-2"></i>Sign Out
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="min-h-screen bg-gray-100 flex h-screen w-full overflow-hidden pt-20">
      <ManagerSidebar @navigate="changeView" />
      <main class="flex-grow p-6 md:p-10 flex flex-col h-full">
        <section class="p-6 bg-white rounded-lg shadow-xl h-full flex flex-col overflow-hidden">
          <component :is="currentViewComponent"></component>
        </section>
      </main>
    </div>
  </div>
</template>

<script>
import ManagerSidebar from '../components/ManagerSidebar.vue';
import ManageFlights from './manager/ManageFlights.vue';
import ManageEmployees from './manager/ManageEmployees.vue';
import ManageBookings from './manager/ManageBookings.vue';
import ManageSeats from './manager/ManageSeats.vue';
import AssignStaff from './manager/AssignStaff.vue';

const DummyView = {
  template: `<div><h2 class="text-xl text-gray-700">Under Construction</h2></div>`
};

export default {
  name: 'ManagerDashboard',
  components: {
    ManagerSidebar,
    ManageFlights,
    ManageEmployees,
    ManageBookings,
    ManageSeats,
    AssignStaff
  },
  data() {
    return {
      username: 'Manager',
      isDropdownOpen: false,
      currentView: 'ManageFlights'
    };
  },
  computed: {
    currentViewComponent() {
      return this.currentView in this.$options.components ? this.currentView : DummyView;
    }
  },
  methods: {
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
    },
    changeView(view) {
      const viewMap = {
        'flights': 'ManageFlights',
        'employees': 'ManageEmployees',
        'bookings': 'ManageBookings',
        'seats': 'ManageSeats',
        'assign': 'AssignStaff'
      };
      this.currentView = viewMap[view] || view;
    }
  }
};
</script>