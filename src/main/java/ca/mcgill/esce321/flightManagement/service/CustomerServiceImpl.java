package ca.mcgill.esce321.flightManagement.service;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.esce321.flightManagement.dto.request.CustomerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.CustomerResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Customer;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.Person;

import ca.mcgill.esce321.flightManagement.repo.FlightRepository;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.Optional;

// public interface CustomerService {
//     Customer createCustomer(CustomerRequestDTO dto); V
//     Customer getCustomerById(Long id); V
//     List<Customer> getAllCustomers(); V
//     Customer updateCustomer(Long id, CustomerRequestDTO dto); V
//     void deleteCustomer(Long id); V
// }

@Service
@Validated
public class CustomerServiceImpl {
    //CRUD
    private final PersonRepository personRepository;
    private final FlightRepository flightRepository;


     CustomerServiceImpl(PersonRepository personRepository, FlightRepository flightRepository){
        this.personRepository = personRepository;
        this.flightRepository = flightRepository;
    }


      public CustomerResponseDTO creatCustomer(CustomerRequestDTO dto) {

        Customer customerToCreate = new Customer(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getMembershipNumber(), dto.getPoints(), dto.getTimeInFlight());
        Customer saved = personRepository.save(customerToCreate);
     
        return new CustomerResponseDTO(
                saved.getId(),
                saved.getEmail(),
                saved.getPassword(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getMembershipNumber(),
                saved.getPoints(),
                saved.getTimeInFlight()
                );
    }

    public CustomerResponseDTO getCustomerById(long id) {
        Optional<Person> p = personRepository.findById(id);

     
        if (p.isPresent() && p.get() instanceof Customer customer) {

            return new CustomerResponseDTO(
                    customer.getId(),
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getMembershipNumber(),
                    customer.getPoints(),
                    customer.getTimeInFlight()
            );
        } else {
            throw new IllegalArgumentException("There is no Customer with ID " + id + ".");
        }
    }

     public List<CustomerResponseDTO> getAllCustomers() {
        List<Person> allPersons = personRepository.findAll();
        List<CustomerResponseDTO> allCustomers = new ArrayList<>();
        for (Person p : allPersons) {
            if (p instanceof Customer customer) {

                allCustomers.add(new CustomerResponseDTO(
                    customer.getId(),
                    customer.getEmail(),
                    customer.getPassword(),
                    customer.getFirstName(),
                    customer.getLastName(),
                    customer.getMembershipNumber(),
                    customer.getPoints(),
                    customer.getTimeInFlight()));
            }
        }
        if(allCustomers.isEmpty()) {
            throw new IllegalArgumentException("There are no Customers in the database.");
        }
        return allCustomers;
    }

    public CustomerResponseDTO updatePilot(long id, CustomerRequestDTO dto) {
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Customer customerToUpdate) {
            customerToUpdate.setEmail(dto.getEmail());
            customerToUpdate.setFirstName(dto.getFirstName());
            customerToUpdate.setLastName(dto.getLastName());
            customerToUpdate.setPassword(dto.getPassword());
            customerToUpdate.setMembershipNumber(dto.getMembershipNumber());
            customerToUpdate.setPoints(dto.getPoints());
            customerToUpdate.setTimeInFlight(dto.getTimeInFlight());

            personRepository.save(customerToUpdate);

            return new CustomerResponseDTO(
                    customerToUpdate.getId(),
                    customerToUpdate.getEmail(),
                    customerToUpdate.getPassword(),
                    customerToUpdate.getFirstName(),
                    customerToUpdate.getLastName(),
                    customerToUpdate.getMembershipNumber(),
                    customerToUpdate.getPoints(),
                    customerToUpdate.getTimeInFlight());
           
        } else {
            throw new IllegalArgumentException("No Customer found with ID " + id);
        }
    }

    public void deleteCustomer(long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Customer customer) {
            personRepository.delete(customer);
        } else {
            throw new IllegalArgumentException("No Customer found with ID " + id);
        }
    }












}

