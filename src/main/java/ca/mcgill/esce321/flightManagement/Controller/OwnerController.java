package ca.mcgill.esce321.flightManagement.controller;

import ca.mcgill.esce321.flightManagement.dto.request.OwnerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.OwnerResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Owner;
import ca.mcgill.esce321.flightManagement.service.OwnerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
@CrossOrigin(origins = "*")
public class OwnerController {

    @Autowired
    private OwnerService ownerService;

    /**
     * Create a new Owner
     */
    @PostMapping
    public ResponseEntity<OwnerResponseDTO> createOwner(@RequestBody OwnerRequestDTO request) {
        Owner created = ownerService.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(new OwnerResponseDTO(created));
    }

    /**
     * Get an Owner by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> getOwnerById(@PathVariable Long id) {
        Owner owner = ownerService.getOwnerById(id);
        return ResponseEntity.ok(new OwnerResponseDTO(owner));
    }

    /**
     * Get all Owners
     */
    @GetMapping
    public ResponseEntity<List<OwnerResponseDTO>> getAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        List<OwnerResponseDTO> dtos = owners.stream()
                                            .map(OwnerResponseDTO::new)
                                            .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Update an Owner’s information
     */
    @PutMapping("/{id}")
    public ResponseEntity<OwnerResponseDTO> updateOwner(
            @PathVariable Long id,
            @RequestBody OwnerRequestDTO request) {
        Owner updated = ownerService.updateOwner(id, request);
        return ResponseEntity.ok(new OwnerResponseDTO(updated));
    }

    /**
     * Delete an Owner by ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handle invalid or missing data gracefully
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        String msg = ex.getMessage() == null ? "Invalid request" : ex.getMessage();
        HttpStatus status = msg.toLowerCase().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
