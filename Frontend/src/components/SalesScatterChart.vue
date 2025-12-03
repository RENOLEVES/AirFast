<template>
  <Scatter :data="chartData" :options="chartOptions" />
</template>

<script>
// Assuming you are using Vue 3 and vue-chartjs
import { Scatter } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LinearScale, PointElement, CategoryScale } from 'chart.js';

// Register necessary Chart.js elements for a Scatter plot
ChartJS.register(Title, Tooltip, Legend, LinearScale, PointElement, CategoryScale);

export default {
  name: 'SalesScatterChart',
  components: { Scatter },
  props: {
    // Expecting data in the format: [{x: number, y: number}, ...]
    scatterPoints: {
      type: Array,
      required: false,
      default: () => []
    }
  },
  computed: {
    chartData() {
      return {
        datasets: [
          {
            label: 'Sales Volume vs. Price',
            backgroundColor: '#41B883',
            data: this.scatterPoints
          }
        ]
      };
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            type: 'linear',
            position: 'bottom',
            title: {
              display: true,
              text: 'Ticket Price (USD)'
            }
          },
          y: {
            title: {
              display: true,
              text: 'Tickets Sold (Volume)'
            }
          }
        }
      };
    }
  }
};
</script>