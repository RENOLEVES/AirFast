// Frontend/src/api/service.js
// This wraps your axios client with convenient methods for each endpoint

// Frontend/src/api/service.js
import { api } from './client'  // Must exist!

/**
 * Customer API calls
 */
export const customerAPI = {
  // Get all customers (Owner view)
  getAllCustomers: async () => {
    const response = await api.get('/owners/view/customer')
    return response.data
  },

  // Get customer by ID
  getCustomerById: async (id) => {
    const response = await api.get(`/customers/${id}`)
    return response.data
  },

  // Create new customer
  createCustomer: async (customerData) => {
    const response = await api.post('/customers', customerData)
    return response.data
  },

  // Update customer
  updateCustomer: async (id, customerData) => {
    const response = await api.put(`/customers/${id}`, customerData)
    return response.data
  },

  // Delete customer
  deleteCustomer: async (id) => {
    const response = await api.delete(`/customers/${id}`)
    return response.data
  }
}

/**
 * Employee API calls
 */
export const employeeAPI = {
  // Get all employees (Owner view)
  getAllEmployees: async () => {
    const response = await api.get('/owners/view/employee')
    return response.data
  },

  // Get employee by ID
  getEmployeeById: async (id) => {
    const response = await api.get(`/employees/${id}`)
    return response.data
  },

  // Create new employee
  createEmployee: async (employeeData) => {
    const response = await api.post('/employees', employeeData)
    return response.data
  },

  // Update employee
  updateEmployee: async (id, employeeData) => {
    const response = await api.put(`/employees/${id}`, employeeData)
    return response.data
  },

  // Delete employee
  deleteEmployee: async (id) => {
    const response = await api.delete(`/employees/${id}`)
    return response.data
  }
}

/**
 * Flight API calls
 */
export const flightAPI = {
  // Search flights by origin, destination, date
  searchFlights: async (origin, destination, date) => {
    const response = await api.get('/flights/search', {
      params: { origin, destination, date }
    })
    return response.data
  },

  // Get all flights
  getAllFlights: async () => {
    const response = await api.get('/flights')
    return response.data
  },

  // Get flight by ID
  getFlightById: async (id) => {
    const response = await api.get(`/flights/${id}`)
    return response.data
  },

  // Create new flight
  createFlight: async (flightData) => {
    const response = await api.post('/flights', flightData)
    return response.data
  },

  // Update flight
  updateFlight: async (id, flightData) => {
    const response = await api.put(`/flights/${id}`, flightData)
    return response.data
  },

  // Delete flight
  deleteFlight: async (id) => {
    const response = await api.delete(`/flights/${id}`)
    return response.data
  }
}

/**
 * Booking API calls
 */
export const bookingAPI = {
  // Get bookings by customer ID
  getBookingsByCustomer: async (customerId) => {
    const response = await api.get(`/bookings/byCustomer/${customerId}`)
    return response.data
  },

  // Get all bookings
  getAllBookings: async () => {
    const response = await api.get('/bookings')
    return response.data
  },

  // Create booking
  createBooking: async (bookingData) => {
    const response = await api.post('/bookings', bookingData)
    return response.data
  },

  // Update booking (e.g., change status)
  updateBooking: async (id, bookingData) => {
    const response = await api.put(`/bookings/${id}`, bookingData)
    return response.data
  },

  // Cancel booking
  cancelBooking: async (id) => {
    const response = await api.delete(`/bookings/${id}`)
    return response.data
  }
}

/**
 * Seat API calls
 */
export const seatAPI = {
  // Get seats by flight ID
  getSeatsByFlight: async (flightId) => {
    const response = await api.get(`/seats/byFlight/${flightId}`)
    return response.data
  },

  // Get all seats
  getAllSeats: async () => {
    const response = await api.get('/seats')
    return response.data
  },

  // Create seat
  createSeat: async (seatData) => {
    const response = await api.post('/seats', seatData)
    return response.data
  },

  // Update seat
  updateSeat: async (id, seatData) => {
    const response = await api.put(`/seats/${id}`, seatData)
    return response.data
  },

  // Delete seat
  deleteSeat: async (id) => {
    const response = await api.delete(`/seats/${id}`)
    return response.data
  }
}

// Export all API modules
export default {
  customer: customerAPI,
  employee: employeeAPI,
  flight: flightAPI,
  booking: bookingAPI,
  seat: seatAPI
}