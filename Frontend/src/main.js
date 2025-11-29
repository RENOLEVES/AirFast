import { createApp } from 'vue'
import './style.css'
import App from './App.vue'

import VCalendar from 'v-calendar';
import 'v-calendar/dist/style.css';
import PrimeVue from 'primevue/config';
import Aura from '@primeuix/themes/aura';

const app = createApp(App);

app.use(PrimeVue, {
    theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darModeSelector: 'system',
            cssLayer: false,
        }
    }
})


app.mount('#app');

