import { defineStore } from 'pinia';
import axios from 'axios'; // Assuming axios is used

export const useAuthStore = defineStore('auth', {
    // State: the core data (logged-in status, user info, token)
    state: () => ({
        isLoggedIn: false,
        user: null, // Holds user object { id, username, email, etc. }
        authToken: localStorage.getItem('authToken') || null,
    }),

    // Actions: methods that change the state (like logging in/out)
    actions: {
        // 1. Action to handle a successful login
        async setLoginStatus(token, userData) {
            this.isLoggedIn = true;
            this.user = userData;
            this.authToken = token;

            // 2. Store token for persistent login
            localStorage.setItem('authToken', token);

            // Optional: Set default auth header for all subsequent axios requests
            // axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        },

        // 3. Action to handle logout
        logout() {
            this.isLoggedIn = false;
            this.user = null;
            this.authToken = null;
            localStorage.removeItem('authToken');
            // axios.defaults.headers.common['Authorization'] = null;
        }
    },

    // Getters: computed properties based on state
    getters: {
        // Check login status based on token presence
        isAuthenticated: (state) => !!state.authToken,
        // Simplified getter for username
        username: (state) => state.user ? state.user.username : 'Guest'
    }
});