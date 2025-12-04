<template>
  <div class="dashboard-view flex flex-col h-full">
    <h1 class="text-2xl font-bold text-gray-800 mb-4 border-b pb-2">
      AirFast Dashboard
    </h1>

    <!-- Loading -->
    <div
      v-if="loading"
      class="flex-grow flex items-center justify-center text-indigo-500 font-semibold text-lg"
    >
      Fetching dashboard data...
    </div>

    <!-- Error -->
    <div
      v-else-if="error"
      class="flex-grow flex flex-col items-center justify-center bg-red-50 border border-red-200 p-6 rounded-lg"
    >
      <h3 class="text-red-600 font-bold mb-2">Error Loading Data</h3>
      <p class="text-red-500">{{ error }}</p>
      <button
        @click="fetchDashboardData"
        class="mt-4 px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600"
      >
        Try Again
      </button>
    </div>

    <!-- Content -->
    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6 flex-grow">
      <!-- Left column -->
      <div class="lg:col-span-1 space-y-6">
        <div class="bg-white shadow-sm rounded-xl p-6 border-l-4 border-indigo-500">
          <p class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-2">
            Total Revenue
          </p>
          <span class="text-3xl font-extrabold text-indigo-700">
            {{ formattedTotalRevenue }}
          </span>
          <p class="text-xs text-gray-500 mt-2">
            Total Customers: {{ totalCustomerCount.toLocaleString() }}
          </p>
        </div>

        <div class="bg-white shadow-sm rounded-xl p-6">
          <h2 class="text-xs font-semibold text-gray-500 uppercase tracking-wider mb-2">
            Total Workforce Composition
          </h2>
          <div class="h-64">
            <EmployeePieChart :employee-data="employeeData" />
          </div>
        </div>
      </div>

      <!-- Right column -->
      <div class="lg:col-span-2">
        <div class="bg-white shadow-sm rounded-xl p-6 h-full flex flex-col">
          <h2 class="text-lg font-semibold text-gray-800 mb-4">
            Cumulative Revenue Over Time
          </h2>
          <div class="flex-grow h-72">
            <RevenueLineChart :revenue-data="cumulativeRevenueData" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import EmployeePieChart from "../components/EmployeePieChart.vue";
import RevenueLineChart  from "../components/RevenueLineChart.vue";

export default {
  name: 'DashboardView',
  components: {
    EmployeePieChart,
    RevenueLineChart,
  },
  data() {
    return {
      loading: true,
      error: null,
      revenueApiUrl: 'http://localhost:8080/api/owners/view/revenue',
      metricsApiUrl: 'http://localhost:8080/api/owners/view/totalEmployeeCount',
      cumulativeRevenueApiUrl: 'http://localhost:8080/api/owners/view/cumulativeRevenue',

      totalRevenue: 0,
      totalCustomerCount: 0,
      employeeData: {
        pilot: 0,
        flightAttendant: 0,
        manager: 0,
      },
      cumulativeRevenueData: []
    };
  },
  computed: {
    formattedTotalRevenue() {
      return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
        minimumFractionDigits: 2
      }).format(this.totalRevenue);
    },
    totalEmployees() {
      return this.employeeData.pilot + this.employeeData.flightAttendant + this.employeeData.manager;
    }
  },
  mounted() {
    this.fetchDashboardData();
  },
  methods: {
    async fetchDashboardData() {
      this.loading = true;
      this.error = null;

      try {
        const revenuePromise = fetch(this.revenueApiUrl).then(res => {
          if (!res.ok) throw new Error(`Revenue fetch failed: ${res.status}`);
          return res.json();
        });

        const metricsPromise = fetch(this.metricsApiUrl).then(res => {
          if (!res.ok) throw new Error(`Metrics fetch failed: ${res.status}`);
          return res.json();
        });

        const cumulativeRevenuePromise = fetch(this.cumulativeRevenueApiUrl).then(res => {
          if (!res.ok) throw new Error(`Cumulative Revenue fetch failed: ${res.status}`);
          return res.json();
        });

        const [revenueData, metricsArray, cumulativeRevenueData] = await Promise.all([
          revenuePromise,
          metricsPromise,
          cumulativeRevenuePromise
        ]);

        this.totalRevenue = typeof revenueData === 'number' ? revenueData : revenueData.totalRevenue || 0;

        // --- Mapping Metrics Array Data ---
        this.totalCustomerCount = metricsArray[1] || 0;

        this.employeeData = {
          pilot: metricsArray[2] || 0,
          flightAttendant: metricsArray[3] || 0,
          manager: metricsArray[4] || 0,
        };

        this.cumulativeRevenueData = cumulativeRevenueData;

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'One or more dashboard services are unavailable.';
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>