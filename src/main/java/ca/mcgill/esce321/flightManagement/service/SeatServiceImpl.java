package ca.mcgill.esce321.flightManagement.service;

import jakarta.transaction.Transactional;
// import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.sql.Date;
import java.time.LocalDate;

import ca.mcgill.esce321.flightManagement.repo.BookingRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;
import ca.mcgill.esce321.flightManagement.repo.PersonRepository;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import ca.mcgill.esce321.flightManagement.dto.request.FlightRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.FlightResponseDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.model.Manager;
import ca.mcgill.esce321.flightManagement.model.Person;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.model.SeatClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.model.Seat;
import java.util.List;

public interface SeatServiceImpl {
    Seat createSeat(SeatRequestDTO dto);
    Seat getSeatById(Long id);
    List<Seat> getAllSeats();
    Seat updateSeat(Long id, SeatRequestDTO dto);
    void deleteSeat(Long id);
}




@Service
@Validated
public class SeatServiceImpl {


    // use constructor instead of autowired.. see owner
    private final SeatRepository seatRepository;

    public SeatServiceImpl(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public SeatResponseDTO findSeatById(long id) {
        Optional<Seat> s = seatRepository.findById(id);
        if(s.isPresent() && s.get() instanceof Seat seat) {
            return new SeatResponseDTO(seat.getSeatId(), seat.getFlight().getFlightId(), seat.getSeatClass(), seat.getPrice(), seat.getSeatNumber(), seat.getSeatStatus()); 
        }       
        else {
            throw new IllegalArgumentException("There is no Seat with ID " + id + ".");
        }

    }

   
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


    public void deleteSeat(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSeat'");
    }



}