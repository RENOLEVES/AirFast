package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.controller.request.OwnerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.service.OwnerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class OwnerController {

    @Autowired
    private OwnerServiceImpl ownerService;

    // CREATE
    @PostMapping
    public ResponseEntity<OwnerResponse> create(@RequestBody OwnerRequestDTO request) {
        final OwnerResponse created = ownerService.createOwner(request);
        // Location header is optional but nice to have:
        return ResponseEntity
                .created(URI.create("/api/owners/" + created.getId()))
                .body(created);
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<OwnerResponse>> getOwner() {
        List<OwnerResponse> owners = ownerService.findOwner();
        return ResponseEntity.ok(owners);
    }

    @GetMapping("/view/cumulativeRevenue")
    public ResponseEntity<List<RevenuePointResponse>> getCumulativeRevenue(){
        return ResponseEntity.ok(ownerService.calculateCumulativeRevenueHistory());
    }

    // READ one
    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponse> getById(@PathVariable("id") long id) {

        return ResponseEntity.ok(ownerService.findOwnerById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponse> update(@PathVariable("id") long id,
                                                @RequestBody OwnerRequestDTO request) {
        return ResponseEntity.ok(ownerService.updateOwner(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/view/totalEmployeeCount")
    public ResponseEntity<List<Integer>> viewTotalEmployeeCount() {

        return ResponseEntity.ok(ownerService.viewTotalEmployeeCount());
    }

    @GetMapping("/view/salary/{id}")
    public ResponseEntity<Double> viewSalary(@PathVariable("id") long id) {
        return ResponseEntity.ok(ownerService.viewSalary(id));
    }

    @GetMapping("/view/revenue")
    public ResponseEntity<Double> viewRevenue() {
        return ResponseEntity.ok(ownerService.calculateTotalRevenue());
    }

    @GetMapping("/view/customer")
    public ResponseEntity<List<CustomerResponse>> viewCustomer() {
        return ResponseEntity.ok(ownerService.viewAllCustomers());
    }

    @GetMapping("/view/employee")
    public ResponseEntity<List<EmployeeResponse>> viewEmployee() {
        return ResponseEntity.ok(ownerService.viewAllEmployees());
    }

    @GetMapping("/view/flight")
    public ResponseEntity<List<FlightResponse>> viewFlight() {
        return ResponseEntity.ok(ownerService.viewAllFlights());
    }

    @GetMapping("/view/seat")
    public ResponseEntity<List<SeatResponse>> viewSeat() {
        return ResponseEntity.ok(ownerService.viewAllSeats());
    }

    @GetMapping("/view/booking")
    public ResponseEntity<List<BookingResponse>> viewBooking() {
        return ResponseEntity.ok(ownerService.viewAllBookings());
    }

    // Map your service's IllegalArgumentException to HTTP codes
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        // Use 404 if it's “not found”; otherwise 400 is fine.
        String msg = ex.getMessage() == null ? "Bad request" : ex.getMessage();
        HttpStatus status = msg != null && msg.toLowerCase().contains("no owner")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}