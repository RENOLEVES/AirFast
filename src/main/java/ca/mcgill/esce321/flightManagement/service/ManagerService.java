package ca.mcgill.esce321.flightManagement.service;

// import ca.mcgill.ecse321.eventregistration.dto.PersonCreationDto;
// import ca.mcgill.ecse321.flightManagement.exception.FlightManagementException;

import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;



// import ca.mcgill.ecse321.flightManagement.repo.PersonRepository;
import jakarta.transaction.Transactional;
// import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.dto.response.ManagerResponseDto;
import ca.mcgill.esce321.flightManagement.dto.request.ManagerRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;



@Service
@Validated
public class ManagerService {

    @Autowired
    private PersonRepository personRepository;

    // public Manager createManager(Manager manager) {
    //     return (Manager) personRepository.save(manager);
    // }

    @Transactional
    public ManagerResponseDto createManager(ManagerRequestDto dto) {
        // Date today = Date.valueOf(LocalDate.now());
        
        Manager managerToCreate = new Manager(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName());
        Manager saved = personRepository.save(managerToCreate);
        return new ManagerResponseDto(saved);
    }

    public ManagerResponseDto findManagerById(long id) {
        Optional<Person> p =  personRepository.findById(id);
        if(p.isPresent() && p.get() instanceof Manager manager) {
            return new ManagerResponseDto(manager);
        }
        else {
            throw new IllegalArgumentException("There is no Manager with ID " + id + ".");
        }
    }

    public List<ManagerResponseDto> findAllManagers() {
        List<Person> allPersons = personRepository.findAll();
        List<ManagerResponseDto> allManagers = new ArrayList<>();

        for (Person p : allPersons) {
            if (p instanceof Manager manager) {
                allManagers.add(new ManagerResponseDto(manager));
            }
        }
        if(allManagers.isEmpty()) {
            throw new IllegalArgumentException("There are no Managers in the database.");
        }
        return allManagers;
        
    }

    @Transactional
public ManagerResponseDto updateManager(long id, ManagerRequestDto dto) {
    Optional<Person> optionalPerson = personRepository.findById(id);

    if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager managerToUpdate) {
        managerToUpdate.setEmail(dto.getEmail());
        managerToUpdate.setFirstName(dto.getFirstName());
        managerToUpdate.setLastName(dto.getLastName());
        managerToUpdate.setPassword(dto.getPassword());

        Manager updated = personRepository.save(managerToUpdate);
        return new ManagerResponseDto(updated);
    } else {
        throw new IllegalArgumentException("No Manager found with ID " + id);
    }
}

     // DELETE
    public void deleteManager(long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Manager manager) {
            personRepository.delete(manager);
        } else {
            throw new IllegalArgumentException("No Manager found with ID " + id);
        }
    }



}