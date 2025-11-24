    package ca.mcgill.esce321.flightManagement.controller;



    import ca.mcgill.esce321.flightManagement.dto.request.PersonRequestDTO;
    import ca.mcgill.esce321.flightManagement.dto.response.*;
    import ca.mcgill.esce321.flightManagement.service.PersonServiceImpl;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;

    import java.net.URI;
    import java.util.List;

    @RestController
    @RequestMapping("/api/persons")
    @CrossOrigin(origins = "*")
    @Validated

    public class PersonController {
        @Autowired
        private PersonServiceImpl personService;


        @PostMapping("/login")
        public ResponseEntity<PersonResponseDTO> login(@RequestBody PersonRequestDTO request) {
            return ResponseEntity.ok(personService.login(request));
        }


    }

