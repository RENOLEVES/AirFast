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
  </div>
</template>

<script>
import ManagerSidebar from '../components/ManagerSlideBar.vue';
import AllFlights from './AllFlights.vue';
import ViewCustomers from './ViewCustomers.vue';
import ViewEmployees from './ViewEmployees.vue';
import ViewBookings from "./ViewBookings.vue";


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
    AllFlights,
    Book: DummyView,
    ManageEmployees: DummyView,
    ManageSeats: DummyView,
    ManageBookings: DummyView,
    ViewCustomers,
    ViewEmployees,
    ViewBookings,
    ViewSeats: DummyView,
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
    displayViewName() {
      return this.currentView.replace(/([A-Z])/g, ' $1').trim();
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