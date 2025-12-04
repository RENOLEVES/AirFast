<template>
  <div class="reports-view p-6 h-full flex flex-col">
    <h1 class="text-2xl font-bold text-gray-800 mb-6 border-b pb-3">
      Business Reports & Analytics
    </h1>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6 flex-grow">
      
      <!-- Financial Reports -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <h2 class="text-lg font-semibold text-gray-700 mb-4 flex items-center">
          <i class="fas fa-dollar-sign text-green-500 mr-2"></i>
          Financial Reports
        </h2>
        <div class="space-y-3">
          <button @click="exportRevenue" 
                  class="w-full px-4 py-3 bg-green-50 hover:bg-green-100 text-green-700 rounded-lg text-left transition">
            <i class="fas fa-file-csv mr-2"></i>
            Export Total Revenue Report
          </button>
          <button @click="exportCumulativeRevenue" 
                  class="w-full px-4 py-3 bg-green-50 hover:bg-green-100 text-green-700 rounded-lg text-left transition">
            <i class="fas fa-chart-line mr-2"></i>
            Export Cumulative Revenue History
          </button>
        </div>
      </div>

      <!-- Customer Reports -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <h2 class="text-lg font-semibold text-gray-700 mb-4 flex items-center">
          <i class="fas fa-users text-blue-500 mr-2"></i>
          Customer Reports
        </h2>
        <div class="space-y-3">
          <button @click="exportCustomers" 
                  class="w-full px-4 py-3 bg-blue-50 hover:bg-blue-100 text-blue-700 rounded-lg text-left transition">
            <i class="fas fa-file-csv mr-2"></i>
            Export All Customers (CSV)
          </button>
          <button @click="exportCustomerActivity" 
                  class="w-full px-4 py-3 bg-blue-50 hover:bg-blue-100 text-blue-700 rounded-lg text-left transition">
            <i class="fas fa-chart-bar mr-2"></i>
            Export Customer Activity Report
          </button>
        </div>
      </div>

      <!-- Employee Reports -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <h2 class="text-lg font-semibold text-gray-700 mb-4 flex items-center">
          <i class="fas fa-user-tie text-purple-500 mr-2"></i>
          Employee Reports
        </h2>
        <div class="space-y-3">
          <button @click="exportEmployees" 
                  class="w-full px-4 py-3 bg-purple-50 hover:bg-purple-100 text-purple-700 rounded-lg text-left transition">
            <i class="fas fa-file-csv mr-2"></i>
            Export All Employees (CSV)
          </button>
          <button @click="exportWorkforce" 
                  class="w-full px-4 py-3 bg-purple-50 hover:bg-purple-100 text-purple-700 rounded-lg text-left transition">
            <i class="fas fa-chart-pie mr-2"></i>
            Export Workforce Composition
          </button>
        </div>
      </div>

      <!-- Operations Reports -->
      <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-200">
        <h2 class="text-lg font-semibold text-gray-700 mb-4 flex items-center">
          <i class="fas fa-plane text-indigo-500 mr-2"></i>
          Operations Reports
        </h2>
        <div class="space-y-3">
          <button @click="exportFlights" 
                  class="w-full px-4 py-3 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 rounded-lg text-left transition">
            <i class="fas fa-file-csv mr-2"></i>
            Export All Flights (CSV)
          </button>
          <button @click="exportBookings" 
                  class="w-full px-4 py-3 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 rounded-lg text-left transition">
            <i class="fas fa-ticket-alt mr-2"></i>
            Export All Bookings (CSV)
          </button>
          <button @click="exportSeats" 
                  class="w-full px-4 py-3 bg-indigo-50 hover:bg-indigo-100 text-indigo-700 rounded-lg text-left transition">
            <i class="fas fa-chair mr-2"></i>
            Export Seat Inventory (CSV)
          </button>
        </div>
      </div>

    </div>

    <!-- Success Message -->
    <div v-if="successMessage" 
         class="mt-6 p-4 bg-green-50 border border-green-200 rounded-lg text-green-700">
      <i class="fas fa-check-circle mr-2"></i>
      {{ successMessage }}
    </div>

    <!-- Error Message -->
    <div v-if="errorMessage" 
         class="mt-6 p-4 bg-red-50 border border-red-200 rounded-lg text-red-700">
      <i class="fas fa-exclamation-circle mr-2"></i>
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { ownerAPI } from '@/api/service'

export default {
  name: 'Reports',
  data() {
    return {
      successMessage: '',
      errorMessage: '',
    };
  },
  methods: {
    clearMessages() {
      this.successMessage = '';
      this.errorMessage = '';
    },

    showSuccess(message) {
      this.clearMessages();
      this.successMessage = message;
      setTimeout(() => this.successMessage = '', 3000);
    },

    showError(message) {
      this.clearMessages();
      this.errorMessage = message;
      setTimeout(() => this.errorMessage = '', 5000);
    },

    downloadCSV(data, filename) {
      const blob = new Blob([data], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      link.href = URL.createObjectURL(blob);
      link.download = filename;
      link.click();
      URL.revokeObjectURL(link.href);
    },

    async exportCustomers() {
      try {
        const customers = await ownerAPI.getAllCustomers();
        const csv = this.convertToCSV(customers, ['id', 'firstName', 'lastName', 'email', 'membershipNumber', 'points', 'timeInFlight']);
        this.downloadCSV(csv, `customers_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Customers exported successfully!');
      } catch (e) {
        this.showError('Failed to export customers');
      }
    },

    async exportEmployees() {
      try {
        const employees = await ownerAPI.getAllEmployees();
        const csv = this.convertToCSV(employees, ['id', 'firstName', 'lastName', 'email', 'title', 'salary']);
        this.downloadCSV(csv, `employees_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Employees exported successfully!');
      } catch (e) {
        this.showError('Failed to export employees');
      }
    },

    async exportFlights() {
      try {
        const flights = await ownerAPI.getAllFlights();
        const csv = this.convertToCSV(flights, ['flightId', 'flightNumber', 'departLocation', 'arrivalLocation', 'departTime', 'arrivalTime', 'capacity', 'seatsRemaining', 'status']);
        this.downloadCSV(csv, `flights_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Flights exported successfully!');
      } catch (e) {
        this.showError('Failed to export flights');
      }
    },

    async exportBookings() {
      try {
        const bookings = await ownerAPI.getAllBookings();
        const csv = this.convertToCSV(bookings, ['bookingId', 'customerId', 'seatId', 'bookingDate', 'paymentStatus', 'bookingStatus']);
        this.downloadCSV(csv, `bookings_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Bookings exported successfully!');
      } catch (e) {
        this.showError('Failed to export bookings');
      }
    },

    async exportSeats() {
      try {
        const seats = await ownerAPI.getAllSeats();
        const csv = this.convertToCSV(seats, ['seatId', 'flightId', 'seatNumber', 'seatClass', 'price', 'seatStatus']);
        this.downloadCSV(csv, `seats_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Seats exported successfully!');
      } catch (e) {
        this.showError('Failed to export seats');
      }
    },

    async exportRevenue() {
      try {
        const revenue = await ownerAPI.getTotalRevenue();
        const csv = `Total Revenue\n${revenue}`;
        this.downloadCSV(csv, `total_revenue_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Revenue exported successfully!');
      } catch (e) {
        this.showError('Failed to export revenue');
      }
    },

    async exportCumulativeRevenue() {
      try {
        const data = await ownerAPI.getCumulativeRevenue();
        const csv = this.convertToCSV(data, ['date', 'revenue']);
        this.downloadCSV(csv, `cumulative_revenue_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Cumulative revenue exported successfully!');
      } catch (e) {
        this.showError('Failed to export cumulative revenue');
      }
    },

    async exportCustomerActivity() {
      try {
        const customers = await ownerAPI.getAllCustomers();
        const csv = this.convertToCSV(customers, ['id', 'firstName', 'lastName', 'points', 'timeInFlight', 'membershipNumber']);
        this.downloadCSV(csv, `customer_activity_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Customer activity exported successfully!');
      } catch (e) {
        this.showError('Failed to export customer activity');
      }
    },

    async exportWorkforce() {
      try {
        const metrics = await ownerAPI.getEmployeeMetrics();
        const csv = `Employee Type,Count\nPilots,${metrics[2]}\nFlight Attendants,${metrics[3]}\nManagers,${metrics[4]}`;
        this.downloadCSV(csv, `workforce_composition_${new Date().toISOString().split('T')[0]}.csv`);
        this.showSuccess('Workforce composition exported successfully!');
      } catch (e) {
        this.showError('Failed to export workforce composition');
      }
    },

    convertToCSV(data, fields) {
      if (!data || data.length === 0) return '';
      
      const header = fields.join(',');
      const rows = data.map(item => 
        fields.map(field => {
          const value = item[field];
          return typeof value === 'string' && value.includes(',') 
            ? `"${value}"` 
            : value;
        }).join(',')
      );
      
      return [header, ...rows].join('\n');
    }
  }
};
</script>