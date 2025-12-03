package ca.mcgill.esce321.flightManagement.service;
import ca.mcgill.esce321.flightManagement.dto.request.*;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Validated
public class EmployeeServiceImpl{

    private final PersonRepository personRepository;
    private ManagerServiceImpl managerService;
    private PilotServiceImpl pilotService;
    private FlightAttendantServiceImpl flightAttendantService;


    public EmployeeServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<EmployeeResponseDTO> findAllEmployees() {
        List<Person> allPeople = personRepository.findAll();

        return allPeople.stream()
                .filter(person -> person instanceof Employee)
                .map(person -> (Employee) person)
                .map(this::convertEmployee)
                .collect(Collectors.toList());
    }


    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {

        // 1. Validation (Highly Recommended)
        // Check if a person with the email already exists before proceeding
        if (personRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A person with this email already exists.");
        }

        // 2. Determine the concrete subclass based on the title
        Employee eToCreate;

        // Normalize the title input for comparison
        String normalizedTitle = dto.getTitle().toLowerCase().trim();

        // Instantiate the correct concrete class
        if ("manager".equals(normalizedTitle)) {
            // Assuming Manager constructor accepts the same arguments as Employee
            eToCreate = new Manager(dto.getEmail(), dto.getPassword(),
                    dto.getFirstName(), dto.getLastName());
        } else if ("pilot".equals(normalizedTitle)) {
            eToCreate = new Pilot(dto.getEmail(), dto.getPassword(),
                    dto.getFirstName(), dto.getLastName());
        } else if ("flight attendant".equals(normalizedTitle)) {
            eToCreate = new FlightAttendant(dto.getEmail(), dto.getPassword(),
                    dto.getFirstName(), dto.getLastName());
        } else {
            // If the title doesn't match any known subclass
            throw new IllegalArgumentException("Invalid employee title: " + dto.getTitle() +
                    ". Must be Manager, Pilot, or Flight Attendant.");
        }

        // 3. Save the entity
        Employee saved = personRepository.save(eToCreate);

        // 4. Return the Response DTO (unchanged)
        return new EmployeeResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getTitle()
        );
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {

        // 1. Fetch the existing Person entity by ID
        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));

        // 2. Validate and Cast to Employee
        if (!(existingPerson instanceof Employee employeeToUpdate)) {
            throw new IllegalArgumentException("The ID " + id + " belongs to a non-employee type.");
        }

        String existingRole = employeeToUpdate.getTitle().toLowerCase().trim();
        String newRole = dto.getTitle().toLowerCase().trim();

        // If the title is provided in the DTO, it must match the existing entity's role
        if (dto.getTitle() != null && !newRole.equals(existingRole)) {
            throw new IllegalArgumentException("Cannot change employee type (e.g., Pilot to Manager) using this update method. The existing role is " + existingRole + ".");
        }

        EmployeeResponseDTO updatedEmployee;
        if (dto instanceof ManagerRequestDTO manager) {
            updatedEmployee = managerService.updateManager(id, manager);
        } else if (dto instanceof PilotRequestDTO pilot) {
            updatedEmployee = pilotService.updatePilot(id, pilot);
        } else if (dto instanceof FlightAttendantRequestDTO flightAttendant) {
            updatedEmployee = flightAttendantService.updateFlightAttendant(id, flightAttendant);
        } else {
            throw new IllegalStateException("Unknown employee subtype encountered for ID: " + id);
        }

        // 4. Convert and return the Response DTO
        return updatedEmployee;
    }

    private EmployeeResponseDTO convertEmployee(Employee e) {
        String employeeTitle = e.getClass().getSimpleName();

        return new EmployeeResponseDTO(
                e.getId(),
                e.getEmail(),
                e.getPassword(),
                e.getFirstName(),
                e.getLastName(),
                employeeTitle
        );
    }
}
