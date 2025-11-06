package ca.mcgill.esce321.flightManagement.Controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import ca.mcgill.esce321.flightManagement.Dto.request.ManagerRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.request.SeatRequestDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.ManagerResponseDTO;
import ca.mcgill.esce321.flightManagement.Dto.response.SeatResponseDTO;
import ca.mcgill.esce321.flightManagement.service.SeatServiceImpl;



// import ca.mcgill.ecse321.eventregistration.dto.PersonResponseDto;
// import ca.mcgill.ecse321.eventregistration.model.Person;
// import ca.mcgill.ecse321.eventregistration.service.PersonService;


@RestController
@RequestMapping("/api/seats")
@Validated
@CrossOrigin(origins = "*")



public class SeatController {

    @Autowired
    private SeatServiceImpl seatService;

    // READ one
    @GetMapping("/{id}")
    public SeatResponseDTO getById(@PathVariable("id") long id) {

        return seatService.findSeatById(id);
    }

    
    // READ all
    @GetMapping
    public List<SeatResponseDTO> getAll() {
        return seatService.viewAllSeats();
    }

    // Map your service's IllegalArgumentException to HTTP codes
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArg(IllegalArgumentException ex) {
        // Use 404 if it's “not found”; otherwise 400 is fine.
        String msg = ex.getMessage() == null ? "Bad request" : ex.getMessage();
        HttpStatus status = msg != null && msg.toLowerCase().contains("no Seat")
                ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(msg);
    }  
}
