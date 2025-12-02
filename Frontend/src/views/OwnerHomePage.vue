<template>
  <div class="owner-dashboard min-h-screen bg-gray-100 flex h-screen">

    <ManagerSidebar @navigate="changeView" />

    <main class="flex-1 p-6 md:p-10">

      <header class="mb-8 p-4 bg-white rounded-lg shadow-md flex justify-between items-center">
        <h1 class="text-3xl font-extrabold text-zinc-700">Welcome Owner 👋</h1>
        <div class="text-gray-500">
          Page 1 of 5 (Current View: {{ displayViewName }})
        </div>
      </header>

      <section class="p-6 bg-white rounded-lg shadow-xl min-h-[60vh]">
        <h2 v-if="currentView !== 'AllFlights'" class="text-2xl font-semibold mb-4 capitalize text-indigo-600">
          {{ displayViewName }}
        </h2>

        <component :is="currentViewComponent"></component>

      </section>

    </main>
  </div>
</template>

<script>
import ManagerSidebar from '../components/ManagerSlideBar.vue';
import AllFlights from './AllFlights.vue'; // The first layout you provided
import ViewCustomers from './ViewCustomers.vue';

// Placeholder components for other views
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
      // Set the default view to the AllFlights component
      currentView: 'AllFlights',
    };
  },
  computed: {
    // Dynamically select the component to render based on currentView string
    currentViewComponent() {
      // For sidebar navigation, we match the event payload to the component name
      return this.currentView in this.$options.components ? this.currentView : DummyView;
    },
    // Helper to format the current view name for display
    displayViewName() {
      return this.currentView.replace(/([A-Z])/g, ' $1').trim();
    },
  },
  methods: {
    // Method to switch the current view (triggered by buttons or sidebar)
    changeView(view) {
      // Logic to map sidebar events ('flights', 'customers') to component names ('AllFlights', 'ViewCustomers')
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
        // For buttons that match the component name directly
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