<template>
  <!--
    FIXED: Changed overflow-y-scroll to overflow-hidden.
    The main container now constrains the height to the screen size (h-screen)
    and prevents external scrolling.
  -->
  <div class="owner-dashboard min-h-screen bg-gray-100 flex h-screen w-full overflow-hidden">

    <ManagerSidebar @navigate="changeView" />

    <main class="flex-1 p-6 md:p-10 flex flex-col h-full">

      <header class="mb-8 p-4 bg-white rounded-lg shadow-md flex justify-between items-center flex-shrink-0">
        <h1 class="text-3xl font-extrabold text-zinc-700">Management System</h1>
      </header>

      <section class="p-6 bg-white rounded-lg shadow-xl h-full flex flex-col overflow-hidden">
        <h2 v-if="currentView !== 'AllFlights'" class="text-2xl font-semibold mb-4 capitalize text-indigo-600 flex-shrink-0">
          {{ displayViewName }}
        </h2>

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
    ViewEmployees: DummyView,
    ViewBookings: DummyView,
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