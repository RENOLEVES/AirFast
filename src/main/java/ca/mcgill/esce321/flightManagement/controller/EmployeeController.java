package ca.mcgill.esce321.flightManagement.controller; // <- keep consistent with your folders

import ca.mcgill.esce321.flightManagement.dto.request.EmployeeRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.service.EmployeeServiceImpl;
import ca.mcgill.esce321.flightManagement.service.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeServiceImpl employeeService;

    // CREATE
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> create(@RequestBody ManagerRequestDTO request) {
        final EmployeeResponseDTO created = employeeService.createEmployee(request);
        // Location header is optional but nice to have:
        return ResponseEntity
                .created(URI.create("/api/employees/" + created.getId()))
                .body(created);
    }

    // READ all
    @GetMapping
    public List<EmployeeResponseDTO> getAll() {
        return employeeService.findAllEmployees();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable("id") long id,
                                                     @RequestBody EmployeeRequestDTO request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    // Map your service's IllegalArgumentException to HTTP codes
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        // Use 404 if it's “not found”; otherwise 400 is fine.
        String msg = ex.getMessage() == null ? "Bad request" : ex.getMessage();
        HttpStatus status = msg != null && msg.toLowerCase().contains("no seat")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}