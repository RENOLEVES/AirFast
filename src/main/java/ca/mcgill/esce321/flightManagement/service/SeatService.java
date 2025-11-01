package ca.mcgill.esce321.flightManagement.service;





// import ca.mcgill.ecse321.eventregistration.dto.PersonCreationDto;
// import ca.mcgill.ecse321.flightManagement.exception.FlightManagementException;

// import ca.mcgill.esce321.flightManagement.model.Manager;
// import ca.mcgill.esce321.flightManagement.model.Person;



// import ca.mcgill.ecse321.flightManagement.repo.PersonRepository;
import jakarta.transaction.Transactional;
// import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
// import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDto;
import ca.mcgill.esce321.flightManagement.Dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;
// import ca.mcgill.esce321.flightManagement.dto.response.SeatResponseDto;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.model.SeatClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;


// package ca.mcgill.esce321.flightManagement.service.implementation;

// import ca.mcgill.esce321.flightManagement.dto.response.*;
// import ca.mcgill.esce321.flightManagement.model.*;
// import ca.mcgill.esce321.flightManagement.repo.*;
// import ca.mcgill.esce321.flightManagement.service.OwnerService;
// import org.springframework.stereotype.Service;

// import java.util.List;
// import java.util.stream.Collectors;



@Service
@Validated
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public SeatResponseDTO findSeatById(long id) {
        Optional<Seat> s = seatRepository.findById(id);
        if(s.isPresent() && s.get() instanceof Seat seat) {
            // Long seatId, Long flightId, Long ownerId,
            //                SeatClass seatClass, double price, String seatNumber, SeatStatus seatStatus
            return new SeatResponseDTO(seat.getSeatId(), seat.getFlight().getFlightId(), seat.getSeatClass(), seat.getPrice(), seat.getSeatNumber(), seat.getSeatStatus());

            //??? what to do here

        //     Optional<Person> p =  personRepository.findById(id);
        // if(p.isPresent() && p.get() instanceof Manager manager) {
        //     return new ManagerResponseDto(manager);
        }
        
        else {
            throw new IllegalArgumentException("There is no Seat with ID " + id + ".");
        }

    }

    //     public ManagerResponseDto findManagerById(long id) {
    //     Optional<Person> p =  personRepository.findById(id);
    //     if(p.isPresent() && p.get() instanceof Manager manager) {
    //         return new ManagerResponseDto(manager);
    //     }
    //     else {
    //         throw new IllegalArgumentException("There is no Manager with ID " + id + ".");
    //     }
    // }


   
    public List<SeatResponseDTO> viewAllSeats() {
        return seatRepository.findAll().stream()
                .map(f -> new SeatResponseDTO(

            f.getSeatId(),
            f.getFlight().getFlightId(),
            f.getSeatClass(),
            f.getPrice(),
            f.getSeatNumber(),
            f.getSeatStatus()             
                )).collect(Collectors.toList());
    }

    
    //  this.seatId = seatId;
    //     this.flightId = flightId;
    //     this.seatClass = seatClass;
    //     this.price = price;
    //     this.seatNumber = seatNumber;
    //     this.seatStatus = seatStatus;

}