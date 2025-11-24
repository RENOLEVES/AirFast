/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        'montserrat': ['Montserrat', 'sans-serif'],
      },
      colors: {
        primary: '#484848',
        secondary: '#9a9a9a',
        light: '#eff0f2',
        'light-gray': '#e0e2e6',
        'light-text': '#c2c6cc',
        'banner-text': '#e8eaec',
      },
    },
  },
  plugins: [],
}

