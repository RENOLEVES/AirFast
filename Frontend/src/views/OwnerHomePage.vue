<template>
  <div>
    <div class="fixed top-0 w-full bg-white shadow-md z-50 p-4 mx-auto pl-20">
      <div class="flex justify-between items-center w-full">
        <div>
          <h1 class="font-extrabold text-[20px] text-[#484848]">
            <i class="fas fa-plane text-blue-500"></i>Air Fast
          </h1>
          <p class="text-[#9a9a9a]">Search and book your next journey</p>
        </div>

        <div class="relative">
          <button
              @click="toggleDropdown"
              class="flex items-center space-x-2 text-[#484848] font-semibold text-lg p-2 rounded-full hover:bg-gray-200 transition"
          >
            <i class="fas fa-user-circle text-2xl text-blue-600"></i>
            <span class="hidden sm:inline">{{ username }}</span>
            <i class="fas" :class="isDropdownOpen ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </button>

          <div
              v-if="isDropdownOpen"
              class="absolute right-0 mt-2 w-48 bg-white border border-gray-200 rounded-lg shadow-xl z-10 overflow-hidden"
          >
            <div class="px-4 py-3 border-b border-gray-100">
              <p class="text-sm text-gray-700">Welcome, {{ username }}</p>
            </div>

            <button
                @click="$emit('navigate', 'FlightBooking')"
                class="w-full text-left px-4 py-3 text-red-500 hover:bg-red-50 transition block font-medium border-t border-gray-100"
            >
              <i class="fas fa-sign-out-alt mr-2"></i>Sign Out
            </button>
          </div>
        </div>
      </div>
      </div>
    </div>

    <div class="owner-dashboard min-h-screen bg-gray-100 flex h-screen w-full overflow-hidden pt-20">

      <ManagerSidebar @navigate="changeView" />

      <main class="flex-grow p-6 md:p-10 flex flex-col h-full">

        <section class="p-6 bg-white rounded-lg shadow-xl h-full flex flex-col overflow-hidden">

          <div class="flex-grow h-full overflow-hidden">
            <component :is="currentViewComponent"></component>
          </div>

        </section>

      </main>
    </div>
</template>

<script>
import ManagerSidebar from '../components/ManagerSlideBar.vue';
import AllFlights from './AllFlights.vue';
import ViewCustomers from './ViewCustomers.vue';
import ViewEmployees from './ViewEmployees.vue';
import ViewBookings from "./ViewBookings.vue";
import ViewSeats from "./ViewSeats.vue";
import DashBoard from "./DashBoard.vue";
import {ref} from "vue";



const DummyView = {
  template: `
        <div>
            <h2 class="text-xl text-gray-700">Content for {{ $parent.displayViewName }}</h2>
            <p class="mt-2 text-gray-500">This section is currently under construction. Please select "Manage Flights" to see the integrated layout.</p>
        </div>`
};

export default {
  name: 'OwnerHomePage',
  components: {
    ManagerSidebar,
    ViewSeats,
    AllFlights,
    ViewCustomers,
    ViewEmployees,
    ViewBookings,
    DashBoard
  },
  setup() {
    // Define reactive variables
    const username = ref('Owner');
    const userPoints = ref(0);
    const isDropdownOpen = ref(false);

    // Define method
    const toggleDropdown = () => {
      isDropdownOpen.value = !isDropdownOpen.value;
    };

    // Return them to be accessible in the template and Options API methods
    return {
      username,
      userPoints,
      isDropdownOpen,
      toggleDropdown,
    };
  },
  data() {
    return {
      currentView: 'AllFlights',
    };
  },
  computed: {
    currentViewComponent() {
      return this.currentView in this.$options.components ? this.currentView : DummyView;
    },
  },
  methods: {
    changeView(view) {
      if (view === 'flights') {
        this.currentView = 'AllFlights';
      } else if (view === 'customers') {
        this.currentView = 'ViewCustomers';
      } else if (view === 'employees') {
        this.currentView = 'ViewEmployees';
      } else if (view === 'bookings') {
        this.currentView = 'ViewBookings';
      } else if (view === 'seats') {
        this.currentView = 'ViewSeats';
      } else if (view === 'dashboard'){
        this.currentView = 'DashBoard';
      } else {
        this.currentView = view;
      }
      console.log(`Switched to view: ${this.currentView}`);
    },
  },
};
</script>

<style scoped>
.action-btn {
  @apply bg-indigo-500 text-white font-medium py-3 rounded-lg shadow-md hover:bg-indigo-600 transition duration-150 text-sm;
}
</style>