<template>
  <Pie :data="chartData" :options="chartOptions" />
</template>

<script>
// Assuming you are using Vue 3 and vue-chartjs
import { Pie } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale } from 'chart.js';

// Register necessary Chart.js elements
ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale);

export default {
  name: 'EmployeePieChart',
  components: { Pie },
  props: {
    employeeData: {
      type: Object,
      required: true,
      default: () => ({ pilot: 0, flightAttendant: 0, manager: 0 })
    }
  },
  computed: {
    chartData() {
      // Extract counts from the prop
      const { pilot, flightAttendant, manager } = this.employeeData;

      return {
        labels: ['Pilots', 'Flight Attendants', 'Managers'],
        datasets: [
          {
            backgroundColor: ['#36A2EB', '#FF6384', '#FFCE56'], // Blue, Red/Pink, Yellow
            data: [pilot, flightAttendant, manager]
          }
        ]
      };
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'right',
          },
          title: {
            display: false,
          }
        }
      };
    }
  }
};
</script>