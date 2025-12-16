package ca.mcgill.esce321.flightManagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import ca.mcgill.esce321.flightManagement.controller.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.SeatResponse;
import ca.mcgill.esce321.flightManagement.model.Flight;
import ca.mcgill.esce321.flightManagement.model.Seat;
import ca.mcgill.esce321.flightManagement.repo.SeatRepository;
import ca.mcgill.esce321.flightManagement.repo.FlightRepository;

import jakarta.transaction.Transactional;
// import jakarta.validation.Valid;


@Service
@Validated
public class SeatServiceImpl {


    // use constructor instead of autowired.. see owner
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;


    public SeatServiceImpl(SeatRepository seatRepository, FlightRepository flightRepository) {
        this.seatRepository = seatRepository;
        this.flightRepository = flightRepository;
    }

     @Transactional
    public SeatResponse createSeat(SeatRequestDTO dto) {
        Optional<Flight> f = flightRepository.findById(dto.getFlightId());
        Flight flight = null;
        if (f.isPresent()) {
            flight = f.get();

        }
        Seat seatToCreate = new Seat(dto.getSeatClass(), dto.getPrice(), dto.getSeatNumber(), dto.getSeatStatus(), flight);

        Seat saved = seatRepository.save(seatToCreate);
        return new SeatResponse(
                saved.getSeatId(),
                saved.getFlight().getFlightId(),
                saved.getSeatClass(),
                saved.getPrice(),
                saved.getSeatNumber(),
                saved.getSeatStatus()
                );
    }

    

    public SeatResponse getSeatById(long id) {
        Optional<Seat> s = seatRepository.findById(id);
        if(s.isPresent() && s.get() instanceof Seat seat) {
            return new SeatResponse(seat.getSeatId(), seat.getFlight().getFlightId(), seat.getSeatClass(), seat.getPrice(), seat.getSeatNumber(), seat.getSeatStatus());
        }       
        else {
            throw new IllegalArgumentException("There is no Seat with ID " + id + ".");
        }

    }

    public List<SeatResponse> getSeatsByFlightId(long flightId) {
        List<Seat> seats = seatRepository.findByFlight_FlightId(flightId);

        if (seats.isEmpty()) {
            throw new IllegalArgumentException("No seats found for flight ID " + flightId + ".");
        }

        return seats.stream()
                .map(seat -> new SeatResponse(
                        seat.getSeatId(),
                        seat.getFlight().getFlightId(),
                        seat.getSeatClass(),
                        seat.getPrice(),
                        seat.getSeatNumber(),
                        seat.getSeatStatus()
                ))
                .toList();
    }

   
    public List<SeatResponse> getAllSeats() {
        return seatRepository.findAll().stream()
                .map(f -> new SeatResponse(

            f.getSeatId(),
            f.getFlight().getFlightId(),
            f.getSeatClass(),
            f.getPrice(),
            f.getSeatNumber(),
            f.getSeatStatus()             
                )).collect(Collectors.toList());
    }

    
    @Transactional
    public SeatResponse updateSeat(long id, SeatRequestDTO dto) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);

        if (optionalSeat.isPresent() && optionalSeat.get() instanceof Seat seatToUpdate) {
            // set attributes for seat
            
       
            Optional<Flight> f = flightRepository.findById(dto.getFlightId());
            Flight flight = null;
            if (f.isPresent()) {
                flight = f.get();

            }
            seatToUpdate.setSeatClass(dto.getSeatClass());
            seatToUpdate.setPrice(dto.getPrice());
            seatToUpdate.setSeatNumber(dto.getSeatNumber());
            seatToUpdate.setSeatStatus(dto.getSeatStatus());
            seatToUpdate.setFlight(flight);

        
            Seat updated = seatRepository.save(seatToUpdate);

            return new SeatResponse(
                    updated.getSeatId(),
                    updated.getFlight().getFlightId(),
                    updated.getSeatClass(),
                    updated.getPrice(),
                    updated.getSeatNumber(),
                    updated.getSeatStatus()
            );
    } else {
        throw new IllegalArgumentException("No Seat found with ID " + id);
    }
    }


    @Transactional
    public void deleteSeat(long id) {
        Optional<Seat> optionalSeat = seatRepository.findById(id);
        if (optionalSeat.isPresent() && optionalSeat.get() instanceof Seat seat) {
            seatRepository.delete(seat);
        } else {
            throw new IllegalArgumentException("No Seat found with ID " + id);
        }
    }

 


}