package ca.mcgill.esce321.flightManagement.service;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.esce321.flightManagement.controller.request.CustomerRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.BookingResponse;
import ca.mcgill.esce321.flightManagement.dto.response.CustomerResponse;
import ca.mcgill.esce321.flightManagement.model.*;
import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class CustomerServiceImpl {
    private final PersonRepository personRepository;
    private final BookingRepository bookingRepository;


    CustomerServiceImpl(PersonRepository personRepository, BookingRepository bookingRepository){
        this.personRepository = personRepository;
        this.bookingRepository = bookingRepository;
    }

    // ---------- CREATE ----------
    public CustomerResponse createCustomer(CustomerRequestDTO dto) {

        Customer customerToCreate = new Customer(dto.getEmail(), dto.getPassword(), dto.getFirstName(), dto.getLastName(), dto.getMembershipNumber(), 0, 0);
        Customer saved = personRepository.save(customerToCreate);

        return new CustomerResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getFirstName(),
                saved.getLastName(),
                saved.getMembershipNumber(),
                saved.getPoints(),
                saved.getTimeInFlight()
                );
    }

    // ---------- READ ONE ----------
    public CustomerResponse findCustomerById(long id) {
        Optional<Person> p = personRepository.findById(id);

        if (p.isPresent() && p.get() instanceof Customer customer) {

            return new CustomerResponse(
                    customer.getId(),
                    customer.getEmail(),
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

    // ---------- READ ALL ----------
     public List<CustomerResponse> findAllCustomers() {
        List<Person> allPersons = personRepository.findAll();
         System.out.println(allPersons);
        List<CustomerResponse> allCustomers = new ArrayList<>();
        for (Person p : allPersons) {
            if (p instanceof Customer customer) {

                allCustomers.add(new CustomerResponse(
                    customer.getId(),
                    customer.getEmail(),
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

    public List<BookingResponse> findBookingsByCustomerId(Long customerId) {
        List<Booking> bookings = bookingRepository.findAllByCustomer_Id(customerId);

        return bookings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // ---------- UPDATE ----------
    public CustomerResponse updateCustomer(long id, CustomerRequestDTO dto) {
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

            return new CustomerResponse(
                    customerToUpdate.getId(),
                    customerToUpdate.getEmail(),
                    customerToUpdate.getFirstName(),
                    customerToUpdate.getLastName(),
                    customerToUpdate.getMembershipNumber(),
                    customerToUpdate.getPoints(),
                    customerToUpdate.getTimeInFlight());
           
        } else {
            throw new IllegalArgumentException("No Customer found with ID " + id);
        }
    }

    // ---------- DELETE ----------
    public void deleteCustomer(long id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent() && optionalPerson.get() instanceof Customer customer) {
            personRepository.delete(customer);
        } else {
            throw new IllegalArgumentException("No Customer found with ID " + id);
        }
    }

    // ---------- helpers ----------
    private BookingResponse convertToDto(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getCustomer().getId(),
                booking.getSeat().getSeatId(),
                booking.getBookingDate(),
                booking.getPaymentStatus(),
                booking.getBookingStatus()
        );
    }

}
