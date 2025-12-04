# ECSE 321 – Airline Management System

## 📌 Project Overview
A modern flight booking application built with Vue.js 3 and Tailwind CSS, implementing a professional Figma wireframe design.
This project is developed as part of **ECSE 321: Introduction to Software Engineering** at McGill University.  
The scope is to build a **web-based airline system** that allows:
- Customers to sign up, browse, and book flights (with 5% overbooking, rewards, cancellation/refunds).  
- Pilots and flight attendants to view their assigned flights.  
- Booking managers to create, edit, cancel flights and assign crew.  
- The owner to manage employees, promotions/policies, and review statistics.  

All stakeholders interact with the system through a web frontend; persistence is handled by PostgreSQL with JPA/Hibernate.

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

## Getting Started

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

Dependencies are already installed! The project is ready to run.

### Run the Backend

Ensure your application.properties PostgresSQL password matches that in the local database installation.

To run the backend:
```bash
./gradlew bootRun
```


### Development

Go into the Frontend folder:
```bash
cd Frontend
```

The full path of the project should be 

"group-project-group-8\FlightManagement\Frontend"

Ensure any frontend packages are installed by running:

```bash
npm install
```

Start the development server:

```bash
npm run dev
```

The application will be available at **http://localhost:5173**
The application talks to backend at base API URL: **http://localhost:8080/api**

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



## Deliverables & Links to Their Documentation 
- [Deliverable 1: Project Report](https://github.com/McGill-ECSE321-Fall2025/group-project-group-8/wiki/Deliverable-1:-Project-Report)
- [Deliverable 1: Technical Report](https://github.com/McGill-ECSE321-Fall2025/group-project-group-8/wiki/Deliverable-1:-Technical-Report)

- [Deliverable 2: Project Report](https://github.com/McGill-ECSE321-Fall2025/group-project-group-8/wiki/Deliverable-2:-Project-Report)
- [Deliverable 2: Technical Report](https://github.com/McGill-ECSE321-Fall2025/group-project-group-8/wiki/Deliverable-2:-Technical-Report)

- Deliverable 3 Report (to be linked)
- Final Presentation Notes (presentation to be linked)

## Team Members

| Name        | Role                  | Deliverable 1 Effort (hrs) | Deliverable 2 Effort (hrs) | Deliverable 3 Effort (hrs) | Presentation Effort (hrs) | Total (hrs) |
|-------------|----------------------|----------------------------|----------------------------|----------------------------|----------------------------|-------------|
| Toufic | Project Manager and Scrum Master    | 8                          | 25                          | …                          | …                          | …           |
| Eric | Backend & Testing Lead	  | 8                          | 25                          | …                          | …                          | …           |
| Marshall    | UML & Architecture Lead        | 8                          | 18-20                          | …                          | …                          | …           |
| Vincent    | Requirements & Documentation Lead       | 8                          | 20                          | …                          | …                          | …           |
| Lince    | Database & Integration Lead   | 8                          | 16-18                          | …                          | …                          | …           |

> Detailed per-meeting minutes and time logs are on the Wiki (see below).

## How We Work

- **Process:** Lightweight Scrum/Kanban on GitHub Projects  
- **Branches:** `main` (protected), `develop`, `feature/<scope>`  
- **Issues:** All work tracked as Issues with labels + milestones; every commit references an Issue ID.  
- **PRs:** Required for merging to `develop`/`main`; checks must pass.

## Tech Stack
- Backend: Java 21, Spring Boot, JPA/Hibernate, JoCoCo
- Database: PostgreSQL (via Docker/Testcontainers)  
- Frontend:
   -   Vue.js 3 - Progressive JavaScript framework with Composition API
   -   Vite - Next generation frontend tooling
-   Tailwind CSS - Utility-first CSS framework (already configured)
-   Montserrat Font - Google Fonts
- CI/CD: Docker (used in some instances in build compile help & DB)

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






