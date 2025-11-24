<template>
  <div class="bg-gradient-to-br from-blue-50 via-white to-indigo-50 min-h-screen flex items-center justify-center p-8">
    <div class="w-full max-w-md">
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <!-- Header -->
        <div class="text-center mb-8">
          <div class="inline-block bg-gradient-to-br from-blue-500 to-indigo-600 p-4 rounded-full mb-4">
            <i class="fas fa-user-plus text-3xl text-white"></i>
          </div>
          <h2 class="font-bold text-[28px] text-[#484848]">
            Create Account
          </h2>
          <p class="text-[#9a9a9a] mt-2">Join us and start booking flights</p>
        </div>
        
        <div class="h-[1px] w-full bg-gray-200 mb-8"></div>
        
        <form @submit.prevent="handleSignup" class="space-y-5">
          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-user mr-2 text-blue-500"></i>First Name
            </label>
            <input
              v-model="signupData.firstName"
              type="text"
              placeholder="John"
              class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
              required
            />
          </div>
          
          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-user mr-2 text-blue-500"></i>Last Name
            </label>
            <input
              v-model="signupData.lastName"
              type="text"
              placeholder="Doe"
              class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
              required
            />
          </div>
          
          <div>
            <label class="block text-sm font-semibold text-[#484848] mb-2">
              <i class="fas fa-envelope mr-2 text-blue-500"></i>Email Address
            </label>
            <input
              v-model="signupData.email"
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
              v-model="signupData.password"
              type="password"
              placeholder="••••••••"
              class="w-full bg-white border border-gray-300 rounded-lg px-4 py-3 text-[14px] text-[#484848] outline-none focus:border-blue-500 focus:ring-2 focus:ring-blue-100 transition"
              required
            />
          </div>
          
          <button
            type="submit"
            class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white font-bold text-[15px] px-12 py-4 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition-all duration-300 shadow-md hover:shadow-lg"
          >
            <i class="fas fa-user-plus mr-2"></i>Create Account
          </button>
        </form>
        
        <p class="text-center text-[14px] text-[#9a9a9a] mt-6">
          Already have an account? 
          <button
            @click="$emit('navigate', 'UserSignin')"
            class="text-blue-600 font-semibold hover:underline ml-1"
          >
            Sign In
          </button>
        </p>
        
        <button
          @click="$emit('navigate', 'HomePage')"
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
import axios from 'axios'

const emit = defineEmits(['navigate'])

const signupData = ref({
  email: '',
  password: '',
  firstName: '',
  lastName: '',
  membershipNumber:''
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
        membershipNumber:''
      }
    )

    console.log("signup success:", response.data)

    alert("signed up successfully!")

    // redirect to home page
    emit('navigate', 'HomePage')

  } catch (error) {
      console.error("Login error:", error)

      if (error.response) {
        const msg = error.response.data?.message
                   || error.response.data?.error
                   || "Invalid credentials"
        alert("Login failed: " + msg)
      } else {
        alert("Cannot reach backend at localhost:8080")
      }
    }
}
</script>
