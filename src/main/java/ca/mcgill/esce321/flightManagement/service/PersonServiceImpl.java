package ca.mcgill.esce321.flightManagement.service;
import ca.mcgill.esce321.flightManagement.dto.request.PersonRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.CustomerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.EmployeeResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.OwnerResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.PersonResponseDTO;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.model.Customer;
import ca.mcgill.esce321.flightManagement.model.Owner;
import ca.mcgill.esce321.flightManagement.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import java.util.Optional;


@Service
@Validated
public class PersonServiceImpl{

    private final PersonRepository personRepository;


    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponseDTO login(PersonRequestDTO request) {

    // Correct: this returns Person, not Optional<Person>
    Person person = personRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Email not found"));

    if (!person.getPassword().equals(request.getPassword())) {
        throw new IllegalArgumentException("Incorrect password");
    }

    // Detect subtype
    if (person instanceof Customer customer) {
        return convertCustomer(customer);
    } 
    else if (person instanceof Employee employee) {
        return convertEmployee(employee);
    } 
    else if (person instanceof Owner owner) {
        return convertOwner(owner);
    }

    throw new IllegalStateException("Unknown person type");
}


    // Converters:

    private CustomerResponseDTO convertCustomer(Customer c) {
        CustomerResponseDTO dto = new CustomerResponseDTO(
                c.getId(), c.getEmail(), c.getPassword(),
                c.getFirstName(), c.getLastName(),
                c.getMembershipNumber(), c.getPoints(), c.getTimeInFlight()
        );
        dto.setTotalBookings(c.getBookings().size());
        return dto;
    }

    private EmployeeResponseDTO convertEmployee(Employee e) {
        return new EmployeeResponseDTO(
                e.getId(), e.getEmail(), e.getPassword(),
                e.getFirstName(), e.getLastName()
        );
    }

    private OwnerResponseDTO convertOwner(Owner o) {
        OwnerResponseDTO dto = new OwnerResponseDTO(
                o.getId(), o.getEmail(), o.getPassword(),
                o.getFirstName(), o.getLastName()
        );

        
        // dto.setTotalRevenue(o.calculateTotalRevenue());
        // also set customerIds, flightIds, etc.
        return dto;
    }



    
}
