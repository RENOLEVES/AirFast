package ca.mcgill.esce321.flightManagement.service;
import ca.mcgill.esce321.flightManagement.controller.request.PersonRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.*;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Service
@Validated
public class PersonServiceImpl{

    private final PersonRepository personRepository;


    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonResponse login(PersonRequestDTO request) {

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

    private CustomerResponse convertCustomer(Customer c) {
        CustomerResponse dto = new CustomerResponse(
                c.getId(), c.getEmail(),
                c.getFirstName(), c.getLastName(),
                c.getMembershipNumber(), c.getPoints(), c.getTimeInFlight()
        );
        dto.setTotalBookings(c.getBookings().size());
        return dto;
    }

    private PersonResponse convertManager(Manager m) {
        return new ManagerResponse(
                m.getId(), m.getEmail(),
                m.getFirstName(), m.getLastName()
        );
    }

    private PersonResponse convertPilot(Pilot p) {
        return new PilotResponse(
                p.getId(), p.getEmail(),
                p.getFirstName(), p.getLastName()
        );
    }

    private PersonResponse convertFlightAttendant(FlightAttendant f) {
        return new FlightAttendantResponse(
                f.getId(), f.getEmail(),
                f.getFirstName(), f.getLastName()
        );
    }

    private OwnerResponse convertOwner(Owner o) {
        OwnerResponse dto = new OwnerResponse(
                o.getId(), o.getEmail(),
                o.getFirstName(), o.getLastName()
        );
        return dto;
    }



    
}
