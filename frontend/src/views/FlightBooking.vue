<template>
  <div class="airfast-root">
    <nav :class="{ 'scrolled': isScrolled }" id="mainNav">
      <a href="#" class="nav-logo">
        <iconify-icon icon="lucide:send" class="logo-accent"></iconify-icon>
        <span>AirFast</span>
      </a>

      <div class="nav-actions">
        <div v-if="isLoggedIn" class="user-menu">
          <button @click="toggleDropdown" class="user-btn">
            <iconify-icon icon="lucide:user-circle"></iconify-icon>
            <span>{{ username }}</span>
            <iconify-icon :icon="isDropdownOpen ? 'lucide:chevron-up' : 'lucide:chevron-down'" class="chevron"></iconify-icon>
          </button>

          <div v-if="isDropdownOpen" class="dropdown-menu">
            <div class="dropdown-header">
              <p class="dropdown-name">{{ username }}</p>
              <p class="dropdown-points">Points: {{ userPoints }}</p>
            </div>
            <button @click="handleProfileClick" class="dropdown-item">
              <iconify-icon icon="lucide:user"></iconify-icon>
              My Profile
            </button>
            <button @click="handleViewBookingsClick" class="dropdown-item">
              <iconify-icon icon="lucide:ticket"></iconify-icon>
              View Bookings
            </button>
            <button @click="logOutUser" class="dropdown-item logout">
              <iconify-icon icon="lucide:log-out"></iconify-icon>
              Sign Out
            </button>
          </div>
        </div>
        <template v-else>
          <button @click="$emit('navigate', 'UserSignin')" class="nav-signin">Sign In</button>
          <button @click="$emit('navigate', 'UserSignup')" class="nav-cta">Sign Up</button>
        </template>
      </div>
    </nav>

    <section class="hero">
      <div class="hero-left">
        <div class="hero-tag">
          <iconify-icon icon="lucide:sparkles"></iconify-icon>
          New routes just landed
        </div>
        <h1><b>Travel without<br/>the <em>turbulence</em></b></h1>
        <p class="hero-desc">Search, compare, and book flights in seconds. No hidden fees, no endless scrolling — just smooth journeys from door to door.</p>
        <div class="hero-actions">
          <a href="#" class="btn-primary">
            <iconify-icon icon="lucide:search"></iconify-icon>
            Search Flights
          </a>
          <button class="btn-ghost">
            Watch how it works
            <iconify-icon icon="lucide:arrow-right"></iconify-icon>
          </button>
        </div>
        <div class="hero-stats">
          <div class="stat-item" data-reveal data-delay="1">
            <span class="stat-num">340+</span>
            <span class="stat-label">Destinations</span>
          </div>
          <div class="stat-item" data-reveal data-delay="2">
            <span class="stat-num">2.4M</span>
            <span class="stat-label">Passengers / yr</span>
          </div>
          <div class="stat-item" data-reveal data-delay="3">
            <span class="stat-num">98%</span>
            <span class="stat-label">On-time rate</span>
          </div>
        </div>
      </div>

      <div class="hero-right">
        <div class="photo-stack">
          <div class="photo-main">
            <img src="https://images.pexels.com/photos/31369788/pexels-photo-31369788.jpeg?auto=compress&cs=tinysrgb&w=900&h=600&dpr=1&fit=crop" alt="Airplane" loading="eager"/>
          </div>
          <div class="float-card float-card-dest">
            <div class="dest-icon">
              <iconify-icon icon="lucide:map-pin"></iconify-icon>
            </div>
            <div class="dest-info">
              <strong>Tokyo, Japan</strong>
              <span>12h 45m · Non-stop</span>
            </div>
          </div>
          <div class="float-card float-card-price">
            <div class="price-label">Starting from</div>
            <div class="price-val">$486 <span>/ person</span></div>
          </div>
        </div>
      </div>
    </section>

    <div class="search-strip">
      <div class="search-strip-inner">
        <div class="search-field">
          <label>From</label>
          <div class="input-row">
            <iconify-icon icon="lucide:plane-takeoff"></iconify-icon>
            <input
                v-model="searchParams.departureLocation"
                type="text"
                placeholder="Montreal, QC"
            />
          </div>
        </div>

        <div class="search-field">
          <label>To</label>
          <div class="input-row">
            <iconify-icon icon="lucide:plane-landing"></iconify-icon>
            <input
                v-model="searchParams.arrivalLocation"
                type="text"
                placeholder="Where to?"
            />
          </div>
        </div>

        <div class="search-field">
          <label>Departure Date</label>
          <div class="input-row">
            <iconify-icon icon="lucide:calendar"></iconify-icon>
            <input
                v-model="searchParams.departureDate"
                type="date"
                placeholder="Pick a date"
            />
          </div>
        </div>

        <div class="search-field">
          <label>Return Date</label>
          <div class="input-row">
            <iconify-icon icon="lucide:calendar"></iconify-icon>
            <input
                v-model="searchParams.returnDate"
                type="date"
                placeholder="Pick a date"
            />
          </div>
        </div>

        <button @click="searchFlights" class="btn-search">
          <iconify-icon icon="lucide:search"></iconify-icon>
          Search
        </button>
      </div>
    </div>

    <div class="ticker-wrap">
      <div class="ticker">
        <div class="ticker-group" v-for="n in 2" :key="n">
          <div v-for="flight in tickerFlights" :key="flight.route" class="ticker-item">
            <iconify-icon icon="lucide:plane"></iconify-icon>
            {{ flight.route }} <span class="sep">·</span> {{ flight.price }}
          </div>
        </div>
      </div>
    </div>

    <section class="section">
      <!-- No Results Message -->
      <div v-if="filteredFlights.length === 0 && isSearchActive" class="no-results">
        <iconify-icon icon="lucide:plane-off" class="no-results-icon"></iconify-icon>
        <h3>No flights found</h3>
        <p>Try adjusting your search criteria</p>
        <button @click="resetSearch" class="btn-reset">
          <iconify-icon icon="lucide:refresh-cw"></iconify-icon>
          Show All Flights
        </button>
      </div>

      <!-- Flight Results -->
      <div v-else>
        <div class="section-head" data-reveal>
          <h2>
            <span v-if="isSearchActive">Search Results</span>
            <span v-else>Available<br/><em>flights</em></span>
          </h2>
          <div class="results-info">
            <iconify-icon icon="lucide:info"></iconify-icon>
            Found {{ filteredFlights.length }} flight{{ filteredFlights.length !== 1 ? 's' : '' }}
          </div>
        </div>

        <div class="flights-grid">
          <div
              v-for="(flight, idx) in filteredFlights"
              :key="flight.id"
              class="flight-card"
              data-reveal
              :data-delay="idx + 1"
          >
            <div class="flight-header">
              <div class="flight-route">
                <iconify-icon icon="lucide:plane"></iconify-icon>
                <span>{{ flight.route }}</span>
              </div>
              <div class="flight-status" :class="flight.status?.toLowerCase()">
                {{ flight.status || 'SCHEDULED' }}
              </div>
            </div>

            <div class="flight-details">
              <div class="flight-time">
                <div class="time-block">
                  <span class="time">{{ flight.departureTime }}</span>
                  <span class="city">{{ flight.departureCity }}</span>
                </div>
                <div class="flight-duration">
                  <iconify-icon icon="lucide:arrow-right"></iconify-icon>
                </div>
                <div class="time-block">
                  <span class="time">{{ flight.arrivalTime }}</span>
                  <span class="city">{{ flight.arrivalCity }}</span>
                </div>
              </div>

              <div class="flight-info">
                <div class="info-item">
                  <iconify-icon icon="lucide:calendar"></iconify-icon>
                  <span>{{ flight.departureDate }}</span>
                </div>
                <div class="info-item">
                  <iconify-icon icon="lucide:hash"></iconify-icon>
                  <span>{{ flight.flightNumber }}</span>
                </div>
                <div class="info-item">
                  <iconify-icon icon="lucide:users"></iconify-icon>
                  <span>{{ flight.remainingSeats }} seats left</span>
                </div>
              </div>
            </div>

            <div class="flight-footer">
              <div class="flight-price">
                <span class="price-label">From</span>
                <span class="price-value">{{ flight.price }}</span>
              </div>
              <button @click="handleBook(flight)" class="btn-book">
                <iconify-icon icon="lucide:ticket"></iconify-icon>
                Book Now
              </button>
            </div>
          </div>
        </div>
      </div>
    </section>

    <footer>
      <div class="footer-inner">
        <div class="footer-brand">
          <div class="nav-logo">
            <iconify-icon icon="lucide:send" style="color:var(--coral);font-size:20px;"></iconify-icon>
            <span style="color:#fff">AirFast</span>
          </div>
          <p>Smooth journeys for modern travellers. Search, book, and fly — without the noise.</p>
        </div>
        <div v-for="col in footerCols" :key="col.title" class="footer-col">
          <h4>{{ col.title }}</h4>
          <ul>
            <li v-for="link in col.links" :key="link"><a href="#">{{ link }}</a></li>
          </ul>
        </div>
      </div>
      <div class="footer-bottom">
        <span>© 2026 AirFast. All rights reserved.</span>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, inject } from 'vue';
import axios from 'axios';

const emit = defineEmits(['navigate']);
const navigate = inject('navigate', null);

const isScrolled = ref(false);

// API setup
const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Search parameters
const searchParams = ref({
  departureLocation: '',
  arrivalLocation: '',
  departureDate: '',
  returnDate: ''
});

// User authentication state
const isLoggedIn = ref(false);
const customerId = ref(null);
const username = ref('Traveler');
const userPoints = ref(0);
const isDropdownOpen = ref(false);

// Flight data
const flights = ref([]);
const isSearchActive = ref(false);
const isLoading = ref(false);

// Dummy flights data as fallback
const dummyFlights = [
  {
    id: 1,
    route: 'Montreal to Tokyo',
    departureCity: 'Montreal',
    departureTime: '8:30 AM',
    arrivalCity: 'Tokyo',
    arrivalTime: '2:45 PM',
    remainingSeats: 45,
    class: 'ECONOMY',
    price: '$486 CAD',
    dateRange: '2026-02-15 to 2026-02-16',
    departureDate: '2026-02-15',
    returnDate: '2026-02-16',
    status: 'SCHEDULED',
    flightNumber: 'AF101'
  },
  {
    id: 2,
    route: 'Montreal to London',
    departureCity: 'Montreal',
    departureTime: '10:15 AM',
    arrivalCity: 'London',
    arrivalTime: '10:30 PM',
    remainingSeats: 32,
    class: 'ECONOMY',
    price: '$548 CAD',
    dateRange: '2026-02-16 to 2026-02-17',
    departureDate: '2026-02-16',
    returnDate: '2026-02-17',
    status: 'SCHEDULED',
    flightNumber: 'AF102'
  },
  {
    id: 3,
    route: 'Montreal to Paris',
    departureCity: 'Montreal',
    departureTime: '11:45 AM',
    arrivalCity: 'Paris',
    arrivalTime: '11:15 PM',
    remainingSeats: 28,
    class: 'ECONOMY',
    price: '$421 CAD',
    dateRange: '2026-02-18 to 2026-02-19',
    departureDate: '2026-02-18',
    returnDate: '2026-02-19',
    status: 'SCHEDULED',
    flightNumber: 'AF103'
  },
  {
    id: 4,
    route: 'Montreal to Miami',
    departureCity: 'Montreal',
    departureTime: '6:00 AM',
    arrivalCity: 'Miami',
    arrivalTime: '10:30 AM',
    remainingSeats: 58,
    class: 'ECONOMY',
    price: '$189 CAD',
    dateRange: '2026-02-20 to 2026-02-20',
    departureDate: '2026-02-20',
    returnDate: '2026-02-20',
    status: 'SCHEDULED',
    flightNumber: 'AF104'
  },
  {
    id: 5,
    route: 'Montreal to Rome',
    departureCity: 'Montreal',
    departureTime: '9:20 AM',
    arrivalCity: 'Rome',
    arrivalTime: '10:45 PM',
    remainingSeats: 22,
    class: 'ECONOMY',
    price: '$502 CAD',
    dateRange: '2026-02-22 to 2026-02-23',
    departureDate: '2026-02-22',
    returnDate: '2026-02-23',
    status: 'SCHEDULED',
    flightNumber: 'AF105'
  },
  {
    id: 6,
    route: 'Montreal to Los Angeles',
    departureCity: 'Montreal',
    departureTime: '7:15 AM',
    arrivalCity: 'Los Angeles',
    arrivalTime: '10:45 AM',
    remainingSeats: 42,
    class: 'ECONOMY',
    price: '$312 CAD',
    dateRange: '2026-02-25 to 2026-02-25',
    departureDate: '2026-02-25',
    returnDate: '2026-02-25',
    status: 'SCHEDULED',
    flightNumber: 'AF106'
  },
  {
    id: 7,
    route: 'Montreal to Dubai',
    departureCity: 'Montreal',
    departureTime: '1:30 PM',
    arrivalCity: 'Dubai',
    arrivalTime: '8:45 AM',
    remainingSeats: 18,
    class: 'ECONOMY',
    price: '$679 CAD',
    dateRange: '2026-02-28 to 2026-03-01',
    departureDate: '2026-02-28',
    returnDate: '2026-03-01',
    status: 'SCHEDULED',
    flightNumber: 'AF107'
  },
  {
    id: 8,
    route: 'Montreal to Barcelona',
    departureCity: 'Montreal',
    departureTime: '3:45 PM',
    arrivalCity: 'Barcelona',
    arrivalTime: '5:30 AM',
    remainingSeats: 35,
    class: 'ECONOMY',
    price: '$456 CAD',
    dateRange: '2026-03-02 to 2026-03-03',
    departureDate: '2026-03-02',
    returnDate: '2026-03-03',
    status: 'SCHEDULED',
    flightNumber: 'AF108'
  }
];

// User authentication functions
const logInUser = (userData) => {
  isLoggedIn.value = true;
  customerId.value = userData.id;
  username.value = userData.username;
  userPoints.value = userData.points || 0;
  localStorage.setItem('flightUserSession', JSON.stringify({
    id: userData.id,
    username: userData.username,
    points: userData.points
  }));
  console.log(`User ${username.value} logged in. Customer ID: ${customerId.value}`);
};

const logOutUser = () => {
  isLoggedIn.value = false;
  customerId.value = null;
  isDropdownOpen.value = false;
  username.value = 'Customer';
  userPoints.value = 0;
  localStorage.removeItem('flightUserSession');
};

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value;
};

const handleProfileClick = () => {
  isDropdownOpen.value = false;
  alert('Navigating to My Profile (Feature not yet implemented)');
};

const handleViewBookingsClick = () => {
  isDropdownOpen.value = false;
  if (customerId.value) {
    emit('navigate', 'BookedFlights', { id: customerId.value });
  } else {
    alert('Please sign in to view your bookings.');
  }
};

// Helper functions
const formatDate = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toISOString().split('T')[0];
};

const formatTime = (dateTime) => {
  if (!dateTime) return '';
  const date = new Date(dateTime);
  return date.toLocaleTimeString('en-US', {
    hour: 'numeric',
    minute: '2-digit',
    hour12: true
  });
};

// Fetch all flights on mount
const fetchAllFlights = async () => {
  isLoading.value = true;
  try {
    const response = await api.get('/api/flights');
    flights.value = response.data.map(flight => ({
      id: flight.flightId,
      route: `${flight.departLocation} to ${flight.arrivalLocation}`,
      departureCity: flight.departLocation,
      departureTime: formatTime(flight.departTime),
      arrivalCity: flight.arrivalLocation,
      arrivalTime: formatTime(flight.arrivalTime),
      remainingSeats: flight.seatsRemaining,
      class: 'ECONOMY',
      price: `$${flight.price || 1000} CAD`,
      dateRange: `${formatDate(flight.departTime)} to ${formatDate(flight.arrivalTime)}`,
      departureDate: formatDate(flight.departTime),
      returnDate: formatDate(flight.arrivalTime),
      status: flight.status,
      flightNumber: flight.flightNumber
    }));
    isSearchActive.value = false;
    console.log('Fetched flights from API:', flights.value);
  } catch (error) {
    console.error('Failed to fetch flights from API:', error);
    console.log('Loading dummy flights as fallback...');
    // Use dummy flights as fallback
    flights.value = [...dummyFlights];
  } finally {
    isLoading.value = false;
  }
};

// Search flights
const searchFlights = async () => {
  try {
    const searchData = {
      departureLocation: searchParams.value.departureLocation,
      arrivalLocation: searchParams.value.arrivalLocation,
      departTimeStr: searchParams.value.departureDate,
      arrivalTimeStr: searchParams.value.returnDate || searchParams.value.departureDate
    };

    console.log('Searching with params:', searchData);

    const response = await api.post('/api/flights/search', searchData);

    flights.value = response.data.map(flight => ({
      id: flight.flightId,
      route: `${flight.departLocation} to ${flight.arrivalLocation}`,
      departureCity: flight.departLocation,
      departureTime: formatTime(flight.departTime),
      arrivalCity: flight.arrivalLocation,
      arrivalTime: formatTime(flight.arrivalTime),
      remainingSeats: flight.seatsRemaining,
      class: 'ECONOMY',
      price: `$${flight.price || 1000} CAD`,
      dateRange: `${formatDate(flight.departTime)} to ${formatDate(flight.arrivalTime)}`,
      departureDate: formatDate(flight.departTime),
      returnDate: formatDate(flight.arrivalTime),
      status: flight.status,
      flightNumber: flight.flightNumber
    }));

    isSearchActive.value = true;
    console.log('Search results from API:', flights.value);
  } catch (error) {
    console.error('Search failed:', error);
    console.log('Using dummy flights for search results...');

    // Filter dummy flights based on search criteria
    let filteredDummy = [...dummyFlights];

    if (searchParams.value.departureLocation) {
      filteredDummy = filteredDummy.filter(flight =>
          flight.departureCity.toLowerCase().includes(searchParams.value.departureLocation.toLowerCase())
      );
    }

    if (searchParams.value.arrivalLocation) {
      filteredDummy = filteredDummy.filter(flight =>
          flight.arrivalCity.toLowerCase().includes(searchParams.value.arrivalLocation.toLowerCase())
      );
    }

    if (searchParams.value.departureDate) {
      filteredDummy = filteredDummy.filter(flight =>
          flight.departureDate === searchParams.value.departureDate
      );
    }

    if (searchParams.value.returnDate) {
      filteredDummy = filteredDummy.filter(flight =>
          flight.returnDate === searchParams.value.returnDate
      );
    }

    flights.value = filteredDummy;
    isSearchActive.value = true;
    console.log('Filtered dummy flights:', flights.value);
  }
};

// Filtered flights
const filteredFlights = computed(() => {
  if (!isSearchActive.value) {
    return flights.value;
  }

  return flights.value.filter(flight => {
    const matchesDeparture = !searchParams.value.departureLocation ||
        flight.departureCity.toLowerCase().includes(searchParams.value.departureLocation.toLowerCase());

    const matchesArrival = !searchParams.value.arrivalLocation ||
        flight.arrivalCity.toLowerCase().includes(searchParams.value.arrivalLocation.toLowerCase());

    const matchesDepartureDate = !searchParams.value.departureDate ||
        flight.departureDate === searchParams.value.departureDate;

    const matchesReturnDate = !searchParams.value.returnDate ||
        flight.returnDate === searchParams.value.returnDate;

    return matchesDeparture && matchesArrival && matchesDepartureDate && matchesReturnDate;
  });
});

// Reset search
const resetSearch = () => {
  searchParams.value = {
    departureLocation: '',
    arrivalLocation: '',
    departureDate: '',
    returnDate: ''
  };
  isSearchActive.value = false;
  fetchAllFlights();
};

// Handle booking
const handleBook = (flight) => {
  console.log('Booking flight:', flight);
  if (!isLoggedIn.value) {
    alert('Please sign in to book flights.');
    emit('navigate', 'UserSignin');
    return;
  }

  if (navigate) {
    navigate('BookingPayment', {
      flightDetails: flight,
      customerId: customerId.value
    });
  } else {
    emit('navigate', 'BookingPayment', {
      flightDetails: flight,
      customerId: customerId.value
    });
  }
};

const tickerFlights = [
  { route: 'YUL → LAX', price: '$312' }, { route: 'YUL → LHR', price: '$548' },
  { route: 'YUL → CDG', price: '$421' }, { route: 'YUL → NRT', price: '$679' },
  { route: 'YUL → MIA', price: '$189' }, { route: 'YUL → FCO', price: '$502' }
];

const footerCols = [
  { title: 'Company', links: ['About Us', 'Careers', 'Press', 'Blog'] },
  { title: 'Support', links: ['Help Center', 'Contact Us', 'Policy'] },
  { title: 'Legal', links: ['Privacy', 'Terms', 'Cookie Policy'] }
];

let observer;

const onScroll = () => {
  isScrolled.value = window.scrollY > 40;
};

onMounted(async () => {
  // Restore user session
  const sessionData = localStorage.getItem('flightUserSession');
  if (sessionData) {
    const userData = JSON.parse(sessionData);
    logInUser(userData);
  }

  // Fetch flights
  await fetchAllFlights();

  // Setup scroll listener
  window.addEventListener('scroll', onScroll);

  // Setup intersection observer for animations
  observer = new IntersectionObserver((entries) => {
    entries.forEach(e => {
      if (e.isIntersecting) {
        e.target.classList.add('visible');
        observer.unobserve(e.target);
      }
    });
  }, { threshold: 0.15, rootMargin: '0px 0px -40px 0px' });

  document.querySelectorAll('[data-reveal]').forEach(el => observer.observe(el));
});

onUnmounted(() => {
  window.removeEventListener('scroll', onScroll);
  if (observer) observer.disconnect();
});

// Expose methods for parent components
defineExpose({
  logInUser,
  logOutUser
});
</script>

<style>
/* 1. GLOBAL RESETS - Crucial for layout matching */
:root {
  --ink: #0f1117; --ink-soft: #3a3d45; --sand: #f4f0eb; --sand-dark: #e8e2d9;
  --cream: #faf8f5; --sky: #d6eaf8; --sky-deep: #1a6fa3; --sky-accent: #0d4f7c;
  --coral: #e8604c; --coral-light: #f08070; --text-light: #6b6e78;
}

*, *::before, *::after { margin: 0; padding: 0; box-sizing: border-box; }

body {
  font-family: 'DM Sans', sans-serif;
  background: var(--sand);
  color: var(--ink);
  overflow-x: hidden;
  -webkit-font-smoothing: antialiased;
}

/* Page Noise */
body::before {
  content: ''; position: fixed; inset: 0; z-index: 999; pointer-events: none;
  opacity: .035; background-size: 300px 300px;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 512 512' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.75' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E");
}

h1, h2, h3 { font-family: 'Playfair Display', serif; }
</style>

<style scoped>
/* 2. COMPONENT SPECIFIC STYLES */
.airfast-root { width: 100%; position: relative; }

nav {
  position: fixed; top: 0; left: 0; right: 0; z-index: 100;
  display: flex; align-items: center; justify-content: space-between;
  padding: 1.25rem 2.5rem; background: rgba(244,240,235,.85);
  backdrop-filter: blur(14px); border-bottom: 1px solid rgba(0,0,0,.06);
  transition: padding .35s ease;
}
nav.scrolled { padding: .75rem 2.5rem; }

.logo-accent { color: var(--coral); font-size: 22px; }
.nav-logo { display: flex; align-items: center; gap: .5rem; text-decoration: none; color: var(--ink); }
.nav-logo span { font-family: 'Playfair Display', serif; font-weight: 700; font-size: 1.35rem; }

.nav-links { display: flex; gap: 2rem; list-style: none; }
.nav-links a { text-decoration: none; color: var(--ink-soft); font-size: .875rem; font-weight: 500; text-transform: uppercase; }

.nav-actions { display: flex; gap: 1rem; align-items: center; }

.user-menu { position: relative; }

.user-btn {
  display: flex; align-items: center; gap: .5rem;
  background: transparent; border: 1.5px solid var(--ink-soft);
  color: var(--ink); padding: .6rem 1.2rem; border-radius: 6px;
  cursor: pointer; font-weight: 500; font-size: .875rem;
  transition: all .3s;
}

.user-btn:hover {
  background: var(--ink-soft); color: #fff;
}

.user-btn .chevron {
  font-size: 14px;
}

.dropdown-menu {
  position: absolute; right: 0; top: calc(100% + 0.5rem);
  background: #fff; border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0,0,0,.15);
  min-width: 200px; overflow: hidden; z-index: 50;
}

.dropdown-header {
  padding: 1rem 1.25rem; border-bottom: 1px solid var(--sand-dark);
  background: var(--cream);
}

.dropdown-name {
  font-size: .9rem; font-weight: 600; color: var(--ink);
  margin-bottom: .25rem;
}

.dropdown-points {
  font-size: .75rem; color: var(--sky-deep); font-weight: 600;
}

.dropdown-item {
  width: 100%; display: flex; align-items: center; gap: .75rem;
  padding: .9rem 1.25rem; background: transparent; border: none;
  color: var(--ink); font-size: .875rem; font-weight: 500;
  cursor: pointer; transition: background .2s; text-align: left;
}

.dropdown-item:hover {
  background: var(--cream);
}

.dropdown-item.logout {
  border-top: 1px solid var(--sand-dark); color: var(--coral);
}

.dropdown-item.logout:hover {
  background: rgba(232,96,76,.1);
}

.dropdown-item iconify-icon {
  font-size: 16px;
}

.nav-signin {
  background: transparent;
  color: var(--ink);
  border: 1.5px solid var(--ink-soft);
  padding: .6rem 1.4rem;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all .3s;
}

.nav-signin:hover {
  background: var(--ink-soft);
  color: #fff;
}

.nav-cta { background: var(--ink); color: #fff; border: none; padding: .6rem 1.4rem; border-radius: 6px; cursor: pointer; font-weight: 500; transition: all .3s; }

.nav-cta:hover {
  background: var(--ink-soft);
}

.hero { min-height: 100vh; display: grid; grid-template-columns: 1fr 1.1fr; padding-top: 72px; }
.hero-left { display: flex; flex-direction: column; justify-content: center; padding: 6rem 5rem; }

.hero-tag {
  display: inline-flex; align-items: center; gap: .5rem; background: var(--sky);
  padding: .5rem 1rem; border-radius: 20px; font-size: .8rem; font-weight: 500;
  color: var(--sky-accent); width: fit-content; margin-bottom: 1.5rem;
}

.hero-left h1 { font-size: clamp(2.6rem, 5.5vw, 4.2rem); line-height: 1.1; margin-bottom: 1.5rem; }
.hero-left h1 em { font-style: italic; color: var(--sky-deep); }

.hero-desc {
  font-size: 1.1rem; line-height: 1.6; color: var(--text-light);
  margin-bottom: 2rem; max-width: 520px;
}

.hero-actions {
  display: flex; gap: 1rem; margin-bottom: 3rem;
}

.btn-primary {
  display: inline-flex; align-items: center; gap: .5rem;
  background: var(--coral); color: #fff; padding: .9rem 1.8rem;
  border-radius: 8px; text-decoration: none; font-weight: 600;
  transition: background .3s;
}
.btn-primary:hover { background: var(--coral-light); }

.btn-ghost {
  display: inline-flex; align-items: center; gap: .5rem;
  background: transparent; border: 1.5px solid var(--ink-soft);
  color: var(--ink); padding: .85rem 1.6rem; border-radius: 8px;
  cursor: pointer; font-weight: 500; transition: all .3s;
}
.btn-ghost:hover { background: var(--ink); color: #fff; }

.hero-stats {
  display: flex; gap: 3rem;
}

.stat-item {
  display: flex; flex-direction: column; gap: .25rem;
}

.stat-num {
  font-size: 2rem; font-weight: 700; color: var(--ink);
  font-family: 'Playfair Display', serif;
}

.stat-label {
  font-size: .8rem; color: var(--text-light); text-transform: uppercase;
  letter-spacing: .5px;
}

.hero-right { position: relative; background: linear-gradient(145deg, var(--sky), var(--sand-dark)); overflow: hidden; }

/* Photo Stack & Cards */
.photo-stack { position: relative; width: 100%; height: 100%; }

.photo-main {
  position: absolute; top: 12%; left: 8%; width: 72%; height: 60%;
  border-radius: 18px; overflow: hidden; box-shadow: 0 24px 60px rgba(0,0,0,.18);
}
.photo-main img { width: 100%; height: 100%; object-fit: cover; }

.float-card {
  position: absolute; background: #fff; border-radius: 14px;
  padding: 1rem 1.25rem; box-shadow: 0 12px 40px rgba(0,0,0,.12); z-index: 3;
}

.float-card-dest {
  bottom: 18%; right: 6%; display: flex; align-items: center; gap: .75rem;
}

.dest-icon {
  width: 38px; height: 38px; background: var(--sky); border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  color: var(--sky-deep); font-size: 18px;
}

.dest-info {
  display: flex; flex-direction: column; gap: .15rem;
}

.dest-info strong {
  font-size: .95rem; color: var(--ink);
}

.dest-info span {
  font-size: .75rem; color: var(--text-light);
}

.float-card-price {
  top: 14%; right: 10%; text-align: center;
}

.price-label {
  font-size: .7rem; color: var(--text-light); text-transform: uppercase;
  letter-spacing: .5px; margin-bottom: .25rem;
}

.price-val {
  font-size: 1.8rem; font-weight: 700; color: var(--coral);
  font-family: 'Playfair Display', serif;
}

.price-val span {
  font-size: .75rem; font-weight: 400; color: var(--text-light);
}

/* Search Strip */
.search-strip {
  background: #fff; padding: 2rem 5rem;
  box-shadow: 0 4px 20px rgba(0,0,0,.08);
}

.search-strip-inner {
  max-width: 1100px; margin: 0 auto;
  display: grid; grid-template-columns: repeat(4, 1fr) auto;
  gap: 1.5rem; align-items: end;
}

.search-field {
  display: flex; flex-direction: column; gap: .5rem;
}

.search-field label {
  font-size: .75rem; font-weight: 600; text-transform: uppercase;
  color: var(--text-light); letter-spacing: .5px;
}

.input-row {
  display: flex; align-items: center; gap: .75rem;
  border: 1.5px solid var(--sand-dark); border-radius: 8px;
  padding: .75rem 1rem; background: var(--cream);
  transition: border-color .3s;
}

.input-row:focus-within {
  border-color: var(--sky-deep);
}

.input-row iconify-icon {
  color: var(--text-light); font-size: 18px;
}

.input-row input {
  border: none; background: transparent; outline: none;
  font-size: .95rem; color: var(--ink); width: 100%;
}

.btn-search {
  display: flex; align-items: center; justify-content: center; gap: .5rem;
  background: var(--sky-deep); color: #fff; border: none;
  padding: .9rem 2rem; border-radius: 8px; cursor: pointer;
  font-weight: 600; transition: background .3s;
}

.btn-search:hover {
  background: var(--sky-accent);
}

/* Ticker Animation */
.ticker-wrap {
  overflow: hidden; background: var(--ink); padding: 1.5rem 0;
}

.ticker {
  display: flex; gap: 3rem; white-space: nowrap;
  animation: ticker-scroll 30s linear infinite;
}

.ticker-group {
  display: flex; gap: 3rem;
}

.ticker-item {
  display: flex; align-items: center; gap: .75rem;
  color: rgba(255,255,255,.55); font-size: .78rem;
  text-transform: uppercase; letter-spacing: .5px;
}

.ticker-item iconify-icon {
  font-size: 14px;
}

.sep {
  opacity: .3;
}

@keyframes ticker-scroll {
  0% { transform: translateX(0); }
  100% { transform: translateX(-50%); }
}

/* Reveal Animations */
[data-reveal] {
  opacity: 0; transform: translateY(28px);
  transition: all .7s cubic-bezier(.4,0,.2,1);
}
[data-reveal].visible {
  opacity: 1; transform: translateY(0);
}
[data-delay="1"] { transition-delay: .12s; }
[data-delay="2"] { transition-delay: .24s; }
[data-delay="3"] { transition-delay: .36s; }

/* Section */
.section {
  padding: 6rem 5rem;
}

.section-head {
  display: flex; justify-content: space-between; align-items: flex-start;
  margin-bottom: 3rem; max-width: 1100px; margin-left: auto; margin-right: auto;
}

.section-head h2 {
  font-size: clamp(2.2rem, 4vw, 3.2rem); line-height: 1.1;
}

.section-head h2 em {
  font-style: italic; color: var(--sky-deep);
}

.section-head a {
  display: flex; align-items: center; gap: .5rem;
  color: var(--ink-soft); text-decoration: none; font-size: .9rem;
  font-weight: 500; margin-top: .5rem; transition: color .3s;
}

.section-head a:hover {
  color: var(--coral);
}

/* Destination Grid */
.dest-grid {
  display: grid; grid-template-columns: 1.2fr 1fr 1fr;
  grid-template-rows: 260px 240px; gap: 1rem;
  max-width: 1100px; margin: 0 auto;
}

.dest-card {
  border-radius: 16px; overflow: hidden; position: relative;
  cursor: pointer;
}

.dest-card img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform .5s;
}

.dest-card:hover img {
  transform: scale(1.06);
}

.dest-card--tall {
  grid-row: span 2;
}

.overlay {
  position: absolute; inset: 0;
  background: linear-gradient(to top, rgba(0,0,0,.7), transparent);
  display: flex; flex-direction: column; justify-content: flex-end;
  padding: 1.5rem; color: #fff;
}

.dest-name {
  font-size: 1.5rem; font-weight: 700; margin-bottom: .25rem;
  font-family: 'Playfair Display', serif;
}

.dest-country {
  font-size: .85rem; opacity: .8; margin-bottom: .75rem;
}

.dest-price {
  font-size: .85rem; opacity: .9;
}

.dest-price strong {
  font-size: 1.1rem; color: var(--coral-light);
}

/* Flight Results */
.no-results {
  text-align: center; padding: 5rem 2rem;
  background: var(--cream); border-radius: 16px;
  max-width: 600px; margin: 0 auto;
}

.no-results-icon {
  font-size: 4rem; color: var(--text-light); margin-bottom: 1.5rem;
}

.no-results h3 {
  font-size: 1.8rem; color: var(--ink); margin-bottom: .75rem;
  font-family: 'Playfair Display', serif;
}

.no-results p {
  font-size: 1rem; color: var(--text-light); margin-bottom: 2rem;
}

.btn-reset {
  display: inline-flex; align-items: center; gap: .5rem;
  background: var(--sky-deep); color: #fff; border: none;
  padding: .9rem 1.8rem; border-radius: 8px; cursor: pointer;
  font-weight: 600; transition: all .3s;
}

.btn-reset:hover {
  background: var(--sky-accent); transform: translateY(-2px);
}

.results-info {
  display: flex; align-items: center; gap: .5rem;
  color: var(--text-light); font-size: .9rem;
  padding: .6rem 1.2rem; background: var(--cream);
  border-radius: 8px; margin-top: .5rem;
}

.flights-grid {
  display: grid; grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 1.5rem; max-width: 1100px; margin: 0 auto;
}

.flight-card {
  background: #fff; border-radius: 16px; padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0,0,0,.08);
  transition: all .3s; border: 1px solid var(--sand-dark);
}

.flight-card:hover {
  transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,.12);
}

.flight-header {
  display: flex; justify-content: space-between; align-items: flex-start;
  margin-bottom: 1.25rem; padding-bottom: 1rem;
  border-bottom: 1px solid var(--sand-dark);
}

.flight-route {
  display: flex; align-items: center; gap: .5rem;
  font-size: .95rem; font-weight: 600; color: var(--ink);
}

.flight-route iconify-icon {
  color: var(--coral); font-size: 18px;
}

.flight-status {
  font-size: .7rem; font-weight: 600; text-transform: uppercase;
  padding: .35rem .75rem; border-radius: 12px;
  background: var(--sky); color: var(--sky-deep);
}

.flight-status.scheduled {
  background: var(--sky); color: var(--sky-deep);
}

.flight-status.delayed {
  background: #fef3cd; color: #856404;
}

.flight-status.cancelled {
  background: #f8d7da; color: #721c24;
}

.flight-details {
  margin-bottom: 1.25rem;
}

.flight-time {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 1rem;
}

.time-block {
  display: flex; flex-direction: column; gap: .25rem;
}

.time {
  font-size: 1.4rem; font-weight: 700; color: var(--ink);
  font-family: 'Playfair Display', serif;
}

.city {
  font-size: .8rem; color: var(--text-light); text-transform: uppercase;
  letter-spacing: .5px;
}

.flight-duration {
  flex: 1; display: flex; justify-content: center; align-items: center;
  color: var(--text-light);
}

.flight-duration iconify-icon {
  font-size: 24px;
}

.flight-info {
  display: flex; flex-direction: column; gap: .6rem;
  padding: 1rem; background: var(--cream); border-radius: 8px;
}

.info-item {
  display: flex; align-items: center; gap: .6rem;
  font-size: .85rem; color: var(--ink-soft);
}

.info-item iconify-icon {
  color: var(--sky-deep); font-size: 16px;
}

.flight-footer {
  display: flex; justify-content: space-between; align-items: center;
  padding-top: 1.25rem; border-top: 1px solid var(--sand-dark);
}

.flight-price {
  display: flex; flex-direction: column; gap: .25rem;
}

.price-label {
  font-size: .7rem; color: var(--text-light); text-transform: uppercase;
  letter-spacing: .5px;
}

.price-value {
  font-size: 1.5rem; font-weight: 700; color: var(--coral);
  font-family: 'Playfair Display', serif;
}

.btn-book {
  display: flex; align-items: center; gap: .5rem;
  background: var(--coral); color: #fff; border: none;
  padding: .75rem 1.5rem; border-radius: 8px; cursor: pointer;
  font-weight: 600; font-size: .9rem; transition: all .3s;
}

.btn-book:hover {
  background: var(--coral-light); transform: translateX(2px);
}

/* Footer */
footer {
  background: var(--ink); color: rgba(255,255,255,.5);
  padding: 4rem 5rem;
}

.footer-inner {
  display: grid; grid-template-columns: 1.8fr 1fr 1fr 1fr;
  gap: 3rem; max-width: 1100px; margin: 0 auto;
  padding-bottom: 3rem; border-bottom: 1px solid rgba(255,255,255,.1);
}

.footer-brand p {
  margin-top: 1rem; line-height: 1.6; max-width: 320px;
}

.footer-col h4 {
  color: #fff; font-size: .95rem; margin-bottom: 1rem;
  font-weight: 600;
}

.footer-col ul {
  list-style: none; display: flex; flex-direction: column; gap: .75rem;
}

.footer-col a {
  color: rgba(255,255,255,.5); text-decoration: none;
  font-size: .875rem; transition: color .3s;
}

.footer-col a:hover {
  color: var(--coral-light);
}

.footer-bottom {
  max-width: 1100px; margin: 0 auto; padding-top: 2rem;
  text-align: center; font-size: .8rem;
}
</style>