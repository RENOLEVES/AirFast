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
    public Manager createManager(String email, String password, String firstName, String lastName) {
        // Date today = Date.valueOf(LocalDate.now());
        
        Manager managerToCreate = new Manager(email, password, firstName, lastName);

        return personRepository.save(managerToCreate);
    }

    public Manager findManagerById(long id) {
        Optional<Person> p =  personRepository.findById(id);
        if(p.isPresent()) {
            return (Manager) p.get();
        }
        else {
            throw new IllegalArgumentException("There is no Manager with ID " + id + ".");
        }
    }

    public List<Manager> findAllManagers() {
        boolean hasManagers = false;
        List<Person> allPersons = personRepository.findAll();
        List<Manager> allManagers = new ArrayList<>();
        for (Person p : allPersons) {
            if (p instanceof Manager) {
                hasManagers = true;
                // Cast Person to Manager
                Manager manager = (Manager) p;
                allManagers.add(manager);
            }
        }
        if(!hasManagers) {
            throw new IllegalArgumentException("There are no Managers in the database.");
        }
        return allManagers;
        
    }

    @Transactional
    public Manager updateManager(long id, Manager manager) {
        // return personRepository.save(manager);
        Optional<Person> managerToUpdate = personRepository.findById(id);
        if(managerToUpdate.isPresent()) {
            Manager foundManagerToUpdate =  (Manager) managerToUpdate.get();
            foundManagerToUpdate.setEmail(manager.getEmail());
            foundManagerToUpdate.setFirstName(manager.getFirstName());
            foundManagerToUpdate.setLastName(manager.getLastName());
            foundManagerToUpdate.setPassword(manager.getPassword());

            return (Manager) personRepository.save(foundManagerToUpdate);
        }
        else {
            throw new IllegalArgumentException("There is no Manager with ID " + id + ".");
        }


      
    }

    public void deleteManager(Manager manager) {
        personRepository.delete(manager);
    }



}