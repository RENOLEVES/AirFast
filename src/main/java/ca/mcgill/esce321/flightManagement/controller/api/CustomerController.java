package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.controller.request.CustomerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponse;
import ca.mcgill.esce321.flightManagement.dto.response.CustomerResponse;
import ca.mcgill.esce321.flightManagement.service.CustomerServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "http://localhost:5173")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    /**
     * Create a new customer
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createPilot(@RequestBody CustomerRequestDTO request) {
        CustomerResponse created = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(created);
    }

    /**
     * Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        CustomerResponse customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * Get all customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> dtos = customerService.findAllCustomers();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update customer information
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequestDTO request) {
        CustomerResponse updated = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a customer by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all bookings for a customer
     */
    @GetMapping("/booking/{customerId}")
    public ResponseEntity<List<BookingResponse>> getAllBookingsForCustomer(@PathVariable Long customerId) {
        List<BookingResponse> dtos = customerService.findBookingsByCustomerId(customerId);
        return ResponseEntity.ok(dtos);
    }

    /**
     * Handle bad or missing data gracefully
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}

