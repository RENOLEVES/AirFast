<template>
  <div class="bg-white overflow-clip relative min-h-screen flex items-center justify-center p-8">
    <div class="relative w-[658px]">
      <div class="bg-white rounded-[10px] shadow-[0px_0px_8px_0px_rgba(194,198,204,0.6)] p-12">
        <p class="font-bold text-[18px] text-[#484848] mb-6">
          Signup
        </p>
        
        <div class="h-[1px] w-full bg-[#c2c6cc] mb-8"></div>
        
        <form @submit.prevent="handleSignup" class="space-y-6">
          <div>
            <input
              v-model="signupData.firstName"
              type="text"
              placeholder="First Name"
              class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
              required
            />
          </div>
          
          <div>
            <input
              v-model="signupData.lastName"
              type="text"
              placeholder="Last Name"
              class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
              required
            />
          </div>
          
          <div>
            <input
              v-model="signupData.email"
              type="email"
              placeholder="Email"
              class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
              required
            />
          </div>
          
          <div>
            <input
              v-model="signupData.password"
              type="password"
              placeholder="Password"
              class="w-full bg-white border border-[#c2c6cc] rounded-[30px] px-8 py-4 text-[14px] text-[#484848] outline-none focus:border-[#484848]"
              required
            />
          </div>
          
          <button
            type="submit"
            class="bg-[#484848] text-white font-bold text-[15px] px-12 py-4 rounded-full hover:bg-opacity-90 transition"
          >
            Create Account
          </button>
        </form>
        
        <p class="text-center text-[14px] text-[#9a9a9a] mt-6">
          Already have an account? 
          <button
            @click="$emit('navigate', 'UserSignin')"
            class="text-[#484848] font-semibold hover:underline"
          >
            Sign In
          </button>
        </p>
        
        <button
          @click="$emit('navigate', 'HomePage')"
          class="mt-6 text-[14px] text-[#9a9a9a] hover:text-[#484848] font-semibold"
        >
          ← Back to Home
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['navigate'])

const signupData = ref({
  firstName: '',
  lastName: '',
  email: '',
  password: ''
})

const handleSignup = () => {
  console.log('Signing up...', signupData.value)
  // Add your signup logic here
  alert('Account created successfully!')
  emit('navigate', 'UserSignin')
}
</script>

