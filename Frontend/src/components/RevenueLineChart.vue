<template>
  <Line :data="chartData" :options="chartOptions" />
</template>

<script>
// Assuming you are using Vue 3 and vue-chartjs
// 2. Changed Scatter to Line
import { Line } from 'vue-chartjs';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  LinearScale,
  PointElement,
  CategoryScale,
  LineElement // 3. Added LineElement for Line Chart
} from 'chart.js';

// 4. Register necessary Chart.js elements for a Line plot
ChartJS.register(Title, Tooltip, Legend, LinearScale, PointElement, CategoryScale, LineElement);

export default {
  // 5. Renamed component
  name: 'RevenueLineChart',
  // 6. Changed Scatter to Line
  components: { Line },
  props: {
    // 7. Updated prop name and expected format: [{date: string, cumulativeRevenue: number}, ...]
    revenueData: {
      type: Array,
      required: false,
      // The default array is empty, which is fine
      default: () => []
    }
  },
  computed: {
    chartData() {
      // 8. Map the API data structure to Chart.js 'labels' and 'data'
      return {
        // Use the 'date' property for the X-axis labels
        labels: this.revenueData.map(item => item.date),
        datasets: [
          {
            // 9. Updated label
            label: 'Cumulative Revenue',
            backgroundColor: '#41B883', // Color for the line/points
            borderColor: '#41B883',
            // Use the 'cumulativeRevenue' property for the Y-axis data
            data: this.revenueData.map(item => item.cumulativeRevenue),
            tension: 0.4, // Optional: gives the line a slight curve
            fill: false // Optional: do not fill the area under the line
          }
        ]
      };
    },
    chartOptions() {
      // 10. Updated chart options
      return {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          x: {
            // Changed from 'linear' to 'category' for discrete date labels
            type: 'category',
            title: {
              display: true,
              text: 'Date' // 11. Updated X-axis title
            }
          },
          y: {
            title: {
              display: true,
              text: 'Cumulative Revenue (USD)' // 12. Updated Y-axis title
            },
            // Good practice for cumulative data: ensure the Y-axis starts at zero
            beginAtZero: true
          }
        }
      };
    }
  }
};
</script>