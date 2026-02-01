<template>
  <div class="signup-root">
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
            <iconify-icon icon="lucide:user-plus"></iconify-icon>
          </div>
          <h1>Create Account</h1>
          <p>Join thousands of happy travelers</p>
        </div>

        <form @submit.prevent="handleSignup" class="auth-form">
          <div class="form-row">
            <div class="form-group">
              <label>
                <iconify-icon icon="lucide:user"></iconify-icon>
                First Name
              </label>
              <input
                  v-model="signupData.firstName"
                  type="text"
                  placeholder="John"
                  required
              />
            </div>

            <div class="form-group">
              <label>
                <iconify-icon icon="lucide:user"></iconify-icon>
                Last Name
              </label>
              <input
                  v-model="signupData.lastName"
                  type="text"
                  placeholder="Doe"
                  required
              />
            </div>
          </div>

          <div class="form-group">
            <label>
              <iconify-icon icon="lucide:mail"></iconify-icon>
              Email Address
            </label>
            <input
                v-model="signupData.email"
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
                v-model="signupData.password"
                type="password"
                placeholder="Create a strong password"
                required
            />
          </div>

          <button type="submit" class="btn-submit">
            <iconify-icon icon="lucide:rocket"></iconify-icon>
            Create Account
          </button>
        </form>

        <div class="auth-footer">
          <p>Already have an account?</p>
          <button @click="$emit('navigate', 'UserSignin')" class="link-switch">
            Sign in instead
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

const emit = defineEmits(['navigate'])

const signupData = ref({
  email: '',
  password: '',
  firstName: '',
  lastName: ''
})

const handleSignup = async () => {
  try {
    const response = await axios.post(
        'http://localhost:8080/api/persons/signup',
        {
          email: signupData.value.email,
          password: signupData.value.password,
          firstName: signupData.value.firstName,
          lastName: signupData.value.lastName,
        }
    )

    console.log("Signup success:", response.data)
    alert(`Welcome aboard, ${signupData.value.firstName}! Your account has been created.`)

    //redirect to sign in page
    emit('navigate', 'UserSignin')

  } catch (error) {
    console.error("Signup error:", error)

    if (error.response) {
      const msg = error.response.data?.message
          || error.response.data?.error
          || "Signup failed"
      alert("Signup failed: " + msg)
    } else {
      alert("Cannot reach server. Please check your connection.")
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
.signup-root { min-height: 100vh; background: var(--sand); }

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
  background: linear-gradient(135deg, var(--sky-deep), var(--sky-accent));
  border-radius: 16px; display: flex; align-items: center; justify-content: center;
  color: #fff; font-size: 28px; box-shadow: 0 8px 24px rgba(26,111,163,.25);
}

.auth-header h1 {
  font-size: 2.5rem; color: var(--ink); margin-bottom: .5rem;
  line-height: 1.2;
}

.auth-header p {
  font-size: 1rem; color: var(--text-light);
}

.auth-form {
  max-width: 480px; margin: 0 auto; width: 100%;
}

.form-row {
  display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;
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
  color: var(--sky-deep); font-size: 16px;
}

.form-group input {
  width: 100%; padding: .9rem 1.2rem; border-radius: 8px;
  border: 1.5px solid var(--sand-dark); background: #fff;
  font-size: .95rem; color: var(--ink); outline: none;
  transition: all .3s;
}

.form-group input:focus {
  border-color: var(--sky-deep); box-shadow: 0 0 0 3px rgba(26,111,163,.1);
}

.btn-submit {
  width: 100%; margin-top: 1rem;
  display: flex; align-items: center; justify-content: center; gap: .5rem;
  background: var(--sky-deep); color: #fff; border: none;
  padding: 1rem 2rem; border-radius: 8px; font-size: 1rem;
  font-weight: 600; cursor: pointer; transition: all .3s;
  box-shadow: 0 4px 12px rgba(26,111,163,.3);
}

.btn-submit:hover {
  background: var(--sky-accent); transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(26,111,163,.4);
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
  background: transparent; border: none; color: var(--sky-deep);
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
  font-style: italic; color: var(--coral);
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
  .form-row { grid-template-columns: 1fr; }
}
</style>