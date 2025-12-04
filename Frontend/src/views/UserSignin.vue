<template>
  <div class="bg-gradient-to-br from-blue-50 via-white to-indigo-50 min-h-screen flex items-center justify-center p-8">
    <div class="w-full max-w-md">
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <!-- Header -->
        <div class="text-center mb-8">
          <div class="inline-block bg-gradient-to-br from-blue-500 to-indigo-600 p-4 rounded-full mb-4">
            <i class="fas fa-sign-in-alt text-3xl text-white"></i>
          </div>
          <h2 class="font-bold text-[28px] text-[#484848]">
            Welcome Back
          </h2>
          <p class="text-[#9a9a9a] mt-2">Sign in to your account</p>
        </div>

        <div class="h-[1px] w-full bg-gray-200 mb-8"></div>

        <form @submit.prevent="handleSignin" class="space-y-5">
          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-envelope mr-2 text-blue-500"></i>Email Address
            </label>
            <input
                v-model="signinData.email"
                type="email"
                placeholder="john.doe@example.com"
                class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                required
            />
          </div>

          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-lock mr-2 text-blue-500"></i>Password
            </label>
            <input
                v-model="signinData.password"
                type="password"
                placeholder="••••••••"
                class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
                required
            />
          </div>

          <!-- Customer Sign In Button -->
          <button
              type="submit"
              class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white font-bold text-[15px] px-12 py-4 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition-all duration-300 shadow-md hover:shadow-lg"
          >
            <i class="fas fa-user mr-2"></i>Sign In as Customer
          </button>

          <!-- Manager Sign In Button -->
          <button
              @click.prevent="handleManagerSignin"
              type="button"
              class="w-full bg-gradient-to-r from-green-500 to-emerald-600 text-white font-bold text-[15px] px-12 py-4 rounded-lg hover:from-green-600 hover:to-emerald-700 transition-all duration-300 shadow-md hover:shadow-lg"
          >
            <i class="fas fa-user-cog mr-2"></i>Sign In as Manager
          </button>

          <!-- Owner Sign In Button -->
          <button
              @click.prevent="handleOwnerSignin"
              type="button"
              class="w-full bg-gradient-to-r from-purple-500 to-pink-600 text-white font-bold text-[15px] px-12 py-4 rounded-lg hover:from-purple-600 hover:to-pink-700 transition-all duration-300 shadow-md hover:shadow-lg"
          >
            <i class="fas fa-crown mr-2"></i>Sign In as Owner
          </button>

        </form>

        <p class="text-center text-[14px] text-[#9a9a9a] mt-6">
          Don't have an account?
          <button
              @click="$emit('navigate', 'UserSignup')"
              class="text-blue-600 font-semibold hover:underline ml-1"
          >
            Sign Up
          </button>
        </p>

        <button
            @click="$emit('navigate', 'FlightBooking')"
            class="mt-6 w-full text-[14px] text-[#9a9a9a] hover:text-[#484848] font-semibold transition"
        >
          <i class="fas fa-arrow-left mr-2"></i>Back to Home
        </button>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue'

const emit = defineEmits(['navigate', 'user-authenticated'])

const signinData = ref({
  email: '',
  password: ''
})

const performLogin = (roleOverride = null) => {
  // Skip backend - just route based on button clicked
  const userRole = roleOverride || 'CUSTOMER'
  
  let targetPage
  if (userRole === 'OWNER') {
    targetPage = 'OwnerHomePage'
  } else if (userRole === 'MANAGER') {
    targetPage = 'ManagerDashboard'
  } else {
    targetPage = 'FlightBooking'
  }

  emit('user-authenticated', {
    id: 1,
    username: signinData.value.email.split('@')[0] || 'User',
    points: 0,
    role: userRole
  })

  emit('navigate', targetPage)
}

const handleSignin = () => {
  performLogin('CUSTOMER')
}

const handleManagerSignin = () => {
  performLogin('MANAGER')
}

const handleOwnerSignin = () => {
  performLogin('OWNER')
}
</script>