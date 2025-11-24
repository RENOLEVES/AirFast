# Flight Booking System

A modern flight booking application built with Vue.js 3 and Tailwind CSS, implementing a professional Figma wireframe design.

## Features

### Core Functionality
- ✈️ **Flight Search & Booking** - Search for flights by departure/arrival locations and dates
- 📋 **View Booked Flights** - Manage and cancel your flight reservations
- 🔐 **User Authentication** - Sign up and sign in functionality
- 💳 **Payment Processing** - Secure payment interface for flight bookings
- 📱 **Responsive Design** - Works on all device sizes

### Flight Information Display
Each flight card shows:
- Route (e.g., Montreal to Toronto)
- Departure and arrival times
- Available seats count
- Flight class (Economy/Business)
- Price in CAD
- Travel date range
- Quick book button

### Pages Included
1. **Home Page** - Welcome screen with navigation to book flights or view bookings
2. **Flight Booking** - Search and browse available flights with filtering
3. **Booked Flights** - View all your booked flights with cancel option
4. **User Signup** - Create a new account
5. **User Signin** - Login to existing account
6. **Booking Payment** - Complete payment for flight booking

## Tech Stack

- **Vue.js 3** - Progressive JavaScript framework with Composition API
- **Vite** - Next generation frontend tooling
- **Tailwind CSS** - Utility-first CSS framework (already configured)
- **Montserrat Font** - Google Fonts

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

Dependencies are already installed! The project is ready to run.

### Development

Start the development server:

```bash
npm run dev
```

The application will be available at **http://localhost:5173**

### Build

Build for production:

```bash
npm run build
```

### Preview Production Build

```bash
npm run preview
```

## Project Structure

```
src/
├── views/
│   ├── HomePage.vue          # Welcome page with navigation
│   ├── FlightBooking.vue     # Flight search and listing
│   ├── BookedFlights.vue     # User's booked flights
│   ├── UserSignup.vue        # User registration
│   ├── UserSignin.vue        # User login
│   └── BookingPayment.vue    # Payment page
├── components/
│   └── FlightCard.vue        # Reusable flight card component
├── App.vue                    # Main app component with routing
├── main.js                    # App entry point
└── style.css                  # Global styles with Tailwind
```

## Features to Implement (Backend Integration)

The current implementation includes the complete UI with mock data. To make it fully functional, you'll need to:

1. **API Integration**
   - Connect to a flight booking API
   - Implement real-time flight data fetching
   - Add authentication endpoints

2. **State Management**
   - Add Pinia or Vuex for global state
   - Manage user session
   - Store booking information

3. **Database**
   - User accounts
   - Flight bookings
   - Payment records

4. **Payment Gateway**
   - Integrate Stripe, PayPal, or other payment processors
   - Handle secure transactions

## Design System

### Colors
- **Primary**: `#484848` - Main text and buttons
- **Secondary**: `#9a9a9a` - Secondary text
- **Light Gray**: `#e0e2e6` - Backgrounds and placeholders
- **Border**: `#c2c6cc` - Input borders and dividers
- **White**: `#ffffff` - Backgrounds

### Typography
- **Font Family**: Montserrat
- **Headings**: ExtraBold (38px), Bold (18px)
- **Body**: SemiBold (14px-18px), Medium (14px)

## Navigation

The app uses a simple component-based navigation system. To navigate between views, emit the `navigate` event with the target view name:

```javascript
$emit('navigate', 'FlightBooking')
```

Available views:
- `HomePage`
- `FlightBooking`
- `BookedFlights`
- `UserSignup`
- `UserSignin`
- `BookingPayment`

## Sample Data

The application includes sample flight data:
- Routes between Montreal and Toronto
- Various times and classes (Economy/Business)
- Different price points ($1000-$2000 CAD)
- Seat availability information

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License - feel free to use this project for learning or commercial purposes.

## Credits

- Design: Figma Wireframe Design Community
- Built with Vue.js 3 and Tailwind CSS
- Font: Google Fonts (Montserrat)
