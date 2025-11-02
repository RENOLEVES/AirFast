package ca.mcgill.esce321.flightManagement.Controller; // <- keep consistent with your folders

import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.service.ManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "*")
@Validated
public class ManagerController {

    @Autowired
    private ManagerServiceImpl managerService;

    // CREATE
    @PostMapping("/api/managers/")
    public ResponseEntity<ManagerResponseDTO> create(@RequestBody ManagerRequestDTO request) {
        final ManagerResponseDTO created = managerService.createManager(request);
        // Location header is optional but nice to have:
        return ResponseEntity
                .created(URI.create("/api/managers/" + created.getId()))
                .body(created);
    }

    // READ one
    @GetMapping("/{id}")
    public ManagerResponseDTO getById(@PathVariable("id") long id) {

        return managerService.findManagerById(id);
    }

    // READ all
    @GetMapping
    public List<ManagerResponseDTO> getAll() {
        return managerService.findAllManagers();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ManagerResponseDTO update(@PathVariable("id") long id,
                                     @RequestBody ManagerRequestDTO request) {
        return managerService.updateManager(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        managerService.deleteManager(id);
        return ResponseEntity.noContent().build();
    }

    // Map your service's IllegalArgumentException to HTTP codes
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        // Use 404 if it's “not found”; otherwise 400 is fine.
        String msg = ex.getMessage() == null ? "Bad request" : ex.getMessage();
        HttpStatus status = msg != null && msg.toLowerCase().contains("no manager")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }
}
