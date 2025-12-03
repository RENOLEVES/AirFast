<template>
  <div class="view-all-employees p-4 h-full flex flex-col">

    <div class="employee-header flex items-center bg-gray-100 p-3 rounded-t-lg shadow-sm border-b border-gray-300 font-semibold text-gray-600 sticky top-0 z-10">
      <div class="column w-1/12">ID</div>
      <div class="column w-3/12">Name</div>
      <div class="column w-4/12">Email</div>
      <div class="column w-2/12">Title</div>
    </div>

    <div class="employee-list overflow-y-auto flex-grow rounded-b-lg border-x border-b border-gray-200">
      <div
          v-for="employee in employees"
          :key="employee.id"
          class="employee-row flex items-center border-t border-gray-200 bg-white hover:bg-indigo-50 transition-colors duration-150 cursor-pointer"
          @click="viewEmployeeDetails(employee)"
      >
        <div class="column w-1/12 font-bold text-lg text-indigo-700">
          {{ employee.id }}
        </div>

        <div class="column w-3/12 font-medium text-gray-800">
          {{ employee.firstName }} {{ employee.lastName }}
        </div>

        <div class="column w-4/12 text-sm text-gray-500 min-w-0 break-words">
          {{ employee.email }}
        </div>

        <div class="column w-2/12">
          <span :class="getTitleClasses(employee.title)" class="px-3 py-1 text-xs leading-5 font-semibold rounded-full">
            {{ employee.title }}
          </span>
        </div>
      </div>
    </div>

    <div class="text-center py-6 mt-4 text-gray-500 border-t pt-6">
      Showing {{ employees.length }} results.
    </div>
  </div>
</template>

<script>
export default {
  name: 'ViewEmployees',
  data() {
    return {
      employees: [],
      loading: false,
      error: null,
      apiUrl: 'http://localhost:8080/api/owners/view/employee',
    };
  },
  methods: {
    async fetchEmployees() {
      // ... (Same fetching logic as before)
      this.loading = true;
      this.error = null;

      try {
        const response = await fetch(this.apiUrl);

        if (!response.ok) {
          throw new Error(`Server returned status: ${response.status}`);
        }

        const data = await response.json();

        // Simulate some titles for badge colors (if your API doesn't provide them)
        this.employees = data.map(emp => ({
          ...emp,
          title: emp.title || (emp.id % 3 === 0 ? 'Manager' : (emp.id % 2 === 0 ? 'Associate' : 'Senior Analyst'))
        }));

      } catch (e) {
        console.error('Fetch error:', e);
        this.error = e.message || 'The backend service is unavailable.';
      } finally {
        this.loading = false;
      }
    },

    getTitleClasses(title) {
      const t = title ? title.toLowerCase() : '';
      if (t.includes('manager')) {
        return 'bg-blue-100 text-blue-800';
      } else if (t.includes('flight attendant')) {
        return 'bg-yellow-100 text-yellow-800';
      } else if (t.includes('pilot')) {
        return 'bg-red-100 text-red-800';
      }
      return 'bg-gray-200 text-gray-800';
    },

    viewEmployeeDetails(employee) {
      // Add logic here to navigate to an employee details page or show a modal
      console.log('Viewing details for:', employee.id);
      // Example: this.$router.push({ name: 'EmployeeDetails', params: { id: employee.id } });
    }
  },
  mounted() {
    this.fetchEmployees();
  },
};
</script>

<style scoped>
.employee-row {
  /* Mimic SeatRow styles */
  padding: 12px 16px;
  min-height: 50px;
  width: 100%;
}

.employee-header {
  padding: 12px 16px; /* Match row padding */
}

.column {
  /* Mimic SeatCard column styles */
  padding: 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>