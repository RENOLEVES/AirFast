import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import VCalendar from 'v-calendar';
import 'v-calendar/dist/style.css'; // Don't forget the required CSS!

const app = createApp(App);

// 2. Use the plugin to install it globally
app.use(VCalendar, {});

app.mount('#app');

