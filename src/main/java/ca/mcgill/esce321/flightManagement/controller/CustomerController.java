package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.CustomerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.CustomerResponseDTO;
import ca.mcgill.esce321.flightManagement.service.CustomerServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerServiceImpl customerService;

    /**
     * Create a new customer
     */
    @PostMapping
    public ResponseEntity<CustomerResponseDTO> createPilot(@RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO created = customerService.createCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(created);
    }

    /**
     * Get customer by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.findCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    /**
     * Get all customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<CustomerResponseDTO> dtos = customerService.findAllCustomers();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update customer information
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerRequestDTO request) {
        CustomerResponseDTO updated = customerService.updateCustomer(id, request);
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

