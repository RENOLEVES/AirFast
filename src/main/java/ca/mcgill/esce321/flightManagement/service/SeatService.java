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
import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDto;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponseDto;
import ca.mcgill.esce321.flightManagement.model.Seat;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;



@Service
@Validated
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    // public Manager createManager(Manager manager) {
    //     return (Manager) personRepository.save(manager);
    // }

    @Transactional
    public SeatResponseDto createSeat(SeatRequestDto dto) {
        // Date today = Date.valueOf(LocalDate.now());
        
        Seat seatToCreate = new Seat(dto.getSeatClass(), dto.getSeatNumber(), dto.getFlight());
        Seat saved = seatRepository.save(seatToCreate);
        return new SeatResponseDto(saved);
    }

    public SeatResponseDto findSeatById(long id) {
        Optional<Seat> p =  seatRepository.findById(id);
        if(p.isPresent() && p.get() instanceof Seat seat) {
            return new SeatResponseDto(seat);
        }
        else {
            throw new IllegalArgumentException("There is no Seat with ID " + id + ".");
        }
    }

    // return Dto list
    public List<SeatResponseDto> findAllManagers() {
        List<Seat> allSeats = seatRepository.findAll();
        if(allSeats.isEmpty()) {
            throw new IllegalArgumentException("There are no Seats in the database.");
        }
        return allSeats;
        
    }

    @Transactional
public SeatResponseDto updateManager(long id, SeatRequestDto dto) {
    Optional<Seat> optionalSeat = seatRepository.findById(id);

    if (optionalSeat.isPresent() && optionalSeat.get() instanceof Seat seatToUpdate) {
        seatToUpdate.setSeatClass(dto.getSeatClass());
        seatToUpdate.setSeatNumber(dto.getSeatNumber());
        seatToUpdate.setFlight(dto.getFlight());
        // seatToUpdate.setPassword(dto.getPassword());

        Seat updated = seatRepository.save(seatToUpdate);
        return new SeatResponseDto(updated);
    } else {
        throw new IllegalArgumentException("No Seat found with ID " + id);
    }
}

// why use the Optional<>
     // DELETE
    public void deleteSeat(long id) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent() && optionalSeat.get() instanceof Seat seat) {
            seatRepository.delete(seat);
        } else {
            throw new IllegalArgumentException("No Seat found with ID " + id);
        }
    }



}