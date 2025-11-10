package ca.mcgill.esce321.flightManagement.service;
import ca.mcgill.esce321.flightManagement.dto.request.FlightAttendantRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightAttendantResponseDTO;
import ca.mcgill.esce321.flightManagement.model.FlightAttendant;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightAttendantServiceImpl {

    private final PersonRepository flightAttendantRepo;

    public FlightAttendantServiceImpl(PersonRepository flightAttendantRepo) {
        this.flightAttendantRepo = flightAttendantRepo;
    }

    // ---------- CREATE ----------
    @Transactional
    public FlightAttendantResponseDTO createFlightAttendant(FlightAttendantRequestDTO d) {
        require(d.getEmail(), "email");
        require(d.getFirstName(), "firstName");
        require(d.getLastName(), "lastName");
        require(d.getPassword(), "password");

        FlightAttendant fa = new FlightAttendant();
        fa.setEmail(d.getEmail());
        fa.setFirstName(d.getFirstName());
        fa.setLastName(d.getLastName());
        fa.setPassword(d.getPassword());

        // Add any extra fields from your DTO if present
        // e.g. fa.setLanguages(d.languages());

        FlightAttendant saved = (FlightAttendant) flightAttendantRepo.save(fa);
        return toResponseDTO(saved);
    }

    // ---------- READ ONE ----------
    public Person getFlightAttendantById(Long id) {
        return flightAttendantRepo.findById(id)
                .orElseThrow(() -> notFound(id));
    }

    // ---------- READ ALL ----------
    public List<Person> getAllFlightAttendants() {
        return flightAttendantRepo.findAll();
    }

    // ---------- UPDATE ----------
    @Transactional
    public FlightAttendantResponseDTO updateFlightAttendant(Long id, FlightAttendantRequestDTO d) {
        FlightAttendant fa = (FlightAttendant) flightAttendantRepo.findById(id)
                .orElseThrow(() -> notFound(id));

        if (d.getEmail() != null && !d.getEmail().isBlank()) fa.setEmail(d.getEmail());
        if (d.getFirstName() != null) fa.setFirstName(d.getFirstName());
        if (d.getLastName() != null) fa.setLastName(d.getLastName());
        if (d.getPassword() != null && !d.getPassword().isBlank()) fa.setPassword(d.getPassword());

        // Update any custom fields here if needed

        FlightAttendant saved = (FlightAttendant) flightAttendantRepo.save(fa);
        return toResponseDTO(saved);
    }

    // ---------- DELETE ----------
    @Transactional
    public void deleteFlightAttendant(Long id) {
        FlightAttendant fa = (FlightAttendant) flightAttendantRepo.findById(id)
                .orElseThrow(() -> notFound(id));
        flightAttendantRepo.delete(fa);
    }

    // ---------- helpers ----------
    private static void require(String s, String field) {
        if (s == null || s.isBlank())
            throw new IllegalArgumentException(field + " is required");
    }
    private static IllegalArgumentException notFound(Long id) {
        return new IllegalArgumentException("FlightAttendant " + id + " not found");
    }

    private static FlightAttendantResponseDTO toResponseDTO(FlightAttendant fa) {
        FlightAttendantResponseDTO dto = new FlightAttendantResponseDTO();
        dto.setId(fa.getId());
        dto.setEmail(fa.getEmail());
        dto.setFirstName(fa.getFirstName());
        dto.setLastName(fa.getLastName());
        // Set other fields as needed
        return dto;
}
}

