package ca.mcgill.ecse321.eventregistration.service;

import ca.mcgill.ecse321.eventregistration.dto.PersonCreationDto;
import ca.mcgill.ecse321.eventregistration.exception.EventRegistrationException;
import ca.mcgill.ecse321.eventregistration.model.Person;
import ca.mcgill.ecse321.eventregistration.repository.PersonRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.esce321.flightManagement.repo.PersonRepository;

@Service
@Validated
public class ManagerService {
    //TODO: implement CRUD methods

    @Autowired
    private PersonRepository personRepository;

    @Transactional
    public Manager createManager(@Valid PersonCreationDto personCreationDto) {
        Date today = Date.valueOf(LocalDate.now());
        Manager newPerson = new Manager(
                personCreationDto.getName(),
                personCreationDto.getEmail(),
                personCreationDto.getPassword(),
                today
        );
        return personRepository.save(newPerson);
    }

    public Manager findPersonById(int id) {
        Manager personFromDB = personRepository.findPersonById(id);
        if (personFromDB == null) {
            throw new EventRegistrationException(HttpStatus.NOT_FOUND, "No person found with id " + id);
        }
        return personFromDB;
    }


}