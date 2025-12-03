<template>
  <div class="dashboard-view p-6 bg-gray-100 min-h-screen">
    <h1 class="text-3xl font-bold text-gray-800 mb-6 border-b pb-2">
      AirFast Dashboard
    </h1>

    <div v-if="loading" class="text-center py-10 text-indigo-500 font-semibold text-lg">
      Fetching dashboard data...
    </div>

    <div v-else-if="error" class="text-center py-10 bg-red-50 border border-red-200 p-6 rounded-lg">
      <h3 class="text-red-600 font-bold mb-2">Error Loading Data</h3>
      <p class="text-red-500">{{ error }}</p>
      <button @click="fetchDashboardData" class="mt-4 px-4 py-2 bg-indigo-500 text-white rounded hover:bg-indigo-600">Try Again</button>
    </div>

    <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-6">

      <div class="lg:col-span-1 space-y-6">

        <div class="metric-card bg-white shadow-xl rounded-xl p-6 border-l-4 border-indigo-500">
          <p class="text-sm font-semibold text-gray-500 uppercase tracking-wider mb-2">
            Total Revenue
          </p>
          <span class="text-4xl font-extrabold text-indigo-700">
            {{ formattedTotalRevenue }}
          </span>
          <p class="text-xs text-gray-400 mt-2">
            Total Customers: **{{ totalCustomerCount.toLocaleString() }}**
          </p>
        </div>

        <div class="chart-card bg-white shadow-xl rounded-xl p-6">
          <h2 class="text-xl font-semibold text-gray-800 mb-4">
            Total Workforce Composition ({{ totalEmployees.toLocaleString() }})
          </h2>
          <div class="pie-chart-wrapper **h-64**">
            <EmployeePieChart :employee-data="employeeData" />
          </div>
        </div>
        </div>

      <div class="lg:col-span-2">
        <div class="chart-container bg-white shadow-xl rounded-xl p-6">
          <div class="lg:col-span-2">
            <div class="chart-container bg-white shadow-xl rounded-xl p-6">
              <h2 class="text-xl font-semibold text-gray-800 mb-4">
                Sales vs. Price Scatter Chart
              </h2>
              <SalesScatterChart :scatter-points="salesData" class="h-96"/>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script>
import EmployeePieChart from "../components/EmployeePieChart.vue";
export default {
  name: 'DashboardView',
  components: {
    EmployeePieChart
  },
  data() {
    return {
      loading: true,
      error: null,

      // Separate API URLs
      revenueApiUrl: 'http://localhost:8080/api/owners/view/revenue',
      metricsApiUrl: 'http://localhost:8080/api/owners/view/totalEmployeeCount',

      // Data variables
      totalRevenue: 0,
      totalCustomerCount: 0,
      employeeData: {
        pilot: 0,
        flightAttendant: 0,
        manager: 0,
      }
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
        // 1. Define the fetch operations as promises
        const revenuePromise = fetch(this.revenueApiUrl).then(res => {
          if (!res.ok) throw new Error(`Revenue fetch failed: ${res.status}`);
          return res.json();
        });

        const metricsPromise = fetch(this.metricsApiUrl).then(res => {
          if (!res.ok) throw new Error(`Metrics fetch failed: ${res.status}`);
          return res.json();
        });

        // 2. Execute both promises concurrently using Promise.all
        const [revenueData, metricsArray] = await Promise.all([revenuePromise, metricsPromise]);

        // --- Mapping Revenue Data ---
        // ASSUMPTION: The revenue endpoint returns a simple number, or an object with a 'totalRevenue' key.
        this.totalRevenue = typeof revenueData === 'number' ? revenueData : revenueData.totalRevenue || 0;

        // --- Mapping Metrics Array Data ---
        // List.of(totalCount, totalCustomerCount, totalPilotCount, totalFlightAttendantCount, totalManagerCount);

        // If the array structure has changed, adjust the indices:
        // Index 0: totalCount (this is often the number of employees, but here we assume totalCustomerCount for better metric pairing)
        // Index 1: totalCustomerCount
        // Index 2: totalPilotCount
        // Index 3: totalFlightAttendantCount
        // Index 4: totalManagerCount

        this.totalCustomerCount = metricsArray[1] || 0;

        this.employeeData = {
          pilot: metricsArray[2] || 0,
          flightAttendant: metricsArray[3] || 0,
          manager: metricsArray[4] || 0,
        };

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