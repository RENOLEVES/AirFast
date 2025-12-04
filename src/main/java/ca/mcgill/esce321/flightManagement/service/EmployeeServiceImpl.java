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

        if (personRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A person with this email already exists.");
        }

        Employee eToCreate;

        String normalizedTitle = dto.getTitle().toLowerCase().trim();

        if ("manager".equals(normalizedTitle)) {
            eToCreate = new Manager(dto.getEmail(), dto.getPassword(),
                    dto.getFirstName(), dto.getLastName());
        } else if ("pilot".equals(normalizedTitle)) {
            eToCreate = new Pilot(dto.getEmail(), dto.getPassword(),
                    dto.getFirstName(), dto.getLastName());
        } else if ("flight attendant".equals(normalizedTitle)) {
            eToCreate = new FlightAttendant(dto.getEmail(), dto.getPassword(),
                    dto.getFirstName(), dto.getLastName());
        } else {
            throw new IllegalArgumentException("Invalid employee title: " + dto.getTitle() +
                    ". Must be Manager, Pilot, or Flight Attendant.");
        }

        Employee saved = personRepository.save(eToCreate);

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
        if (dto == null) {
            throw new IllegalArgumentException("Request body (dto) must not be null");
        }

        Person existingPerson = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + id));

        if (!(existingPerson instanceof Employee employeeToUpdate)) {
            throw new IllegalArgumentException("The ID " + id + " belongs to a non-employee type.");
        }

        if (dto instanceof ManagerRequestDTO manager) {
            return managerService.updateManager(id, manager);
        } else if (dto instanceof PilotRequestDTO pilot) {
            return pilotService.updatePilot(id, pilot);
        } else if (dto instanceof FlightAttendantRequestDTO fa) {
            return flightAttendantService.updateFlightAttendant(id, fa);
        } else {
            if (dto.getEmail() != null) employeeToUpdate.setEmail(dto.getEmail());
            if (dto.getFirstName() != null) employeeToUpdate.setFirstName(dto.getFirstName());
            if (dto.getLastName() != null) employeeToUpdate.setLastName(dto.getLastName());
            if (dto.getTitle() != null && !dto.getTitle().isBlank()) employeeToUpdate.setTitle(dto.getTitle());
            Employee saved = personRepository.save(employeeToUpdate);
            return convertToResponseDto(saved);
        }
    }

    // example helper - adjust to your DTO fields
    private EmployeeResponseDTO convertToResponseDto(Employee e) {
        return new EmployeeResponseDTO(e.getId(), e.getEmail(),e.getPassword(),e.getFirstName(), e.getLastName(), e.getTitle());
    }


    public void deleteEmployee(Long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Employee employee) {
            personRepository.delete(employee);
        } else {
            throw new IllegalArgumentException("No Owner found with ID " + id);
        }
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
