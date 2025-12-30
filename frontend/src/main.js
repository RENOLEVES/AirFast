import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import VCalendar from 'v-calendar';
import 'v-calendar/dist/style.css'; // Don't forget the required CSS!
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

const app = createApp(App);

app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: 'system',
            cssLayer: false
        }
    }
});

// 2. Use the plugin to install it globally
app.use(VCalendar, {});

app.mount('#app');

