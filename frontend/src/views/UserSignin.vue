<template>
  <div class="signin-root">
    <nav class="auth-nav">
      <a href="#" class="nav-logo" @click.prevent="$emit('navigate', 'FlightBooking')">
        <iconify-icon icon="lucide:send" class="logo-accent"></iconify-icon>
        <span>AirFast</span>
      </a>
      <button class="nav-back" @click="$emit('navigate', 'FlightBooking')">
        <iconify-icon icon="lucide:arrow-left"></iconify-icon>
        Back to Home
      </button>
    </nav>

    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <div class="auth-icon">
            <iconify-icon icon="lucide:user-check"></iconify-icon>
          </div>
          <h1>Welcome Back</h1>
          <p>Sign in to continue your journey</p>
        </div>

        <form @submit.prevent="handleSignin" class="auth-form">
          <div class="form-group">
            <label>
              <iconify-icon icon="lucide:mail"></iconify-icon>
              Email Address
            </label>
            <input
                v-model="signinData.email"
                type="email"
                placeholder="your.email@example.com"
                required
            />
          </div>

          <div class="form-group">
            <label>
              <iconify-icon icon="lucide:lock"></iconify-icon>
              Password
            </label>
            <input
                v-model="signinData.password"
                type="password"
                placeholder="Enter your password"
                required
            />
          </div>

          <button type="submit" class="btn-submit">
            <iconify-icon icon="lucide:log-in"></iconify-icon>
            Sign In
          </button>
        </form>

        <div class="auth-footer">
          <p>Don't have an account?</p>
          <button @click="$emit('navigate', 'UserSignup')" class="link-switch">
            Create one now
            <iconify-icon icon="lucide:arrow-right"></iconify-icon>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'

const emit = defineEmits(['navigate', 'user-authenticated'])

const signinData = ref({
  email: '',
  password: ''
})

const handleSignin = async () => {
  try {
    const endpoint = 'http://localhost:8080/api/persons/login';

    const response = await axios.post(
        endpoint,
        {
          email: signinData.value.email,
          password: signinData.value.password
        }
    );

    const userData = response.data;
    let userRole = 'CUSTOMER';

    if (userData.title) {
      const titleUpper = userData.title.toUpperCase();
      if (titleUpper === 'OWNER') {
        userRole = 'OWNER';
      }
      if (titleUpper === 'MANAGER') {
        userRole = 'MANAGER';
      }
    }

    let targetPage;
    if (userRole === 'OWNER') {
      targetPage = 'OwnerHomePage';
    } else if (userRole === 'MANAGER') {
      targetPage = 'ManagerDashboard';
    } else {
      targetPage = 'FlightBooking';
    }

    emit('user-authenticated', {
      id: userData.id,
      username: userData.firstName || userData.email.split('@')[0] || 'User',
      points: userData.points
    });

    alert(`Welcome back! Signed in successfully as ${userRole}.`);
    emit('navigate', targetPage);

  } catch (error) {
    console.error("Login error:", error);
    if (error.response) {
      alert("Login failed: Invalid username or password combination.");
    } else {
      alert("Cannot reach server. Please check your connection.");
    }
  }
}
</script>

<style>
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
  -webkit-font-smoothing: antialiased;
}

body::before {
  content: ''; position: fixed; inset: 0; z-index: 999; pointer-events: none;
  opacity: .035; background-size: 300px 300px;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 512 512' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='.75' numOctaves='4' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)'/%3E%3C/svg%3E");
}

h1, h2 { font-family: 'Playfair Display', serif; }
</style>

<style scoped>
.signin-root { min-height: 100vh; background: var(--sand); }

.auth-nav {
  position: fixed; top: 0; left: 0; right: 0; z-index: 100;
  display: flex; align-items: center; justify-content: space-between;
  padding: 1.25rem 2.5rem; background: rgba(244,240,235,.85);
  backdrop-filter: blur(14px); border-bottom: 1px solid rgba(0,0,0,.06);
}

.logo-accent { color: var(--coral); font-size: 22px; }

.nav-logo {
  display: flex; align-items: center; gap: .5rem;
  text-decoration: none; color: var(--ink); cursor: pointer;
}

.nav-logo span {
  font-family: 'Playfair Display', serif; font-weight: 700; font-size: 1.35rem;
}

.nav-back {
  display: flex; align-items: center; gap: .5rem;
  background: transparent; border: 1.5px solid var(--ink-soft);
  color: var(--ink); padding: .6rem 1.2rem; border-radius: 6px;
  cursor: pointer; font-weight: 500; font-size: .875rem;
  transition: all .3s;
}

.nav-back:hover {
  background: var(--ink); color: #fff;
}

.auth-container {
  min-height: 100vh; display: grid;
  padding-top: 72px;
}

.auth-card {
  display: flex; flex-direction: column; justify-content: center;
  padding: 4rem 5rem; background: var(--cream);
}

.auth-header {
  text-align: center; margin-bottom: 3rem;
}

.auth-icon {
  width: 64px; height: 64px; margin: 0 auto 1.5rem;
  background: linear-gradient(135deg, var(--coral), var(--coral-light));
  border-radius: 16px; display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 28px; box-shadow: 0 8px 24px rgba(232,96,76,.25);
}

.auth-header h1 {
  font-size: 2.5rem; color: var(--ink); margin-bottom: .5rem;
  line-height: 1.2;
}

.auth-header p {
  font-size: 1rem; color: var(--text-light);
}

.auth-form {
  max-width: 420px; margin: 0 auto; width: 100%;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: flex; align-items: center; gap: .5rem;
  font-size: .875rem; font-weight: 600; color: var(--ink-soft);
  margin-bottom: .5rem; text-transform: uppercase; letter-spacing: .5px;
}

.form-group label iconify-icon {
  color: var(--coral); font-size: 16px;
}

.form-group input {
  width: 100%; padding: .9rem 1.2rem; border-radius: 8px;
  border: 1.5px solid var(--sand-dark); background: #fff;
  font-size: .95rem; color: var(--ink); outline: none;
  transition: all .3s;
}

.form-group input:focus {
  border-color: var(--coral); box-shadow: 0 0 0 3px rgba(232,96,76,.1);
}

.btn-submit {
  width: 100%; margin-top: 1rem;
  display: flex; align-items: center; justify-content: center; gap: .5rem;
  background: var(--coral); color: #fff; border: none;
  padding: 1rem 2rem; border-radius: 8px; font-size: 1rem;
  font-weight: 600; cursor: pointer; transition: all .3s;
  box-shadow: 0 4px 12px rgba(232,96,76,.3);
}

.btn-submit:hover {
  background: var(--coral-light); transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(232,96,76,.4);
}

.auth-footer {
  text-align: center; margin-top: 2rem; padding-top: 2rem;
  border-top: 1px solid var(--sand-dark);
}

.auth-footer p {
  font-size: .9rem; color: var(--text-light); margin-bottom: .5rem;
}

.link-switch {
  display: inline-flex; align-items: center; gap: .5rem;
  background: transparent; border: none; color: var(--coral);
  font-size: .95rem; font-weight: 600; cursor: pointer;
  transition: gap .3s;
}

.link-switch:hover {
  gap: .75rem;
}

.visual-content h2 {
  font-size: 3rem; line-height: 1.1; color: var(--ink);
  margin-bottom: 1.5rem;
}

.visual-content h2 em {
  font-style: italic; color: var(--sky-deep);
}

.visual-content > p {
  font-size: 1.1rem; line-height: 1.6; color: var(--text-light);
  margin-bottom: 2.5rem;
}

.feature-item iconify-icon {
  color: var(--coral); font-size: 22px;
}

.feature-item span {
  font-size: .95rem; color: var(--ink); font-weight: 500;
}

@media (max-width: 1024px) {
  .auth-container { grid-template-columns: 1fr; }
}
</style>