package ca.mcgill.esce321.flightManagement.service;
import ca.mcgill.esce321.flightManagement.dto.request.PersonRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;

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
    else if (person instanceof Manager manager) {
        return convertManager(manager);
    } else if (person instanceof Pilot pilot) {
        return convertPilot(pilot);
    } else if (person instanceof FlightAttendant flightAttendant) {
        return convertFlightAttendant(flightAttendant);
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

    private PersonResponseDTO convertManager(Manager m) {
        return new ManagerResponseDTO(
                m.getId(), m.getEmail(), m.getPassword(),
                m.getFirstName(), m.getLastName()
        );
    }

    private PersonResponseDTO convertPilot(Pilot p) {
        return new PilotResponseDTO(
                p.getId(), p.getEmail(), p.getPassword(),
                p.getFirstName(), p.getLastName()
        );
    }

    private PersonResponseDTO convertFlightAttendant(FlightAttendant f) {
        return new FlightAttendantResponseDTO(
                f.getId(), f.getEmail(), f.getPassword(),
                f.getFirstName(), f.getLastName()
        );
    }

    private OwnerResponseDTO convertOwner(Owner o) {
        OwnerResponseDTO dto = new OwnerResponseDTO(
                o.getId(), o.getEmail(), o.getPassword(),
                o.getFirstName(), o.getLastName()
        );
        return dto;
    }



    
}
