    package ca.mcgill.esce321.flightManagement.controller.api;


    import ca.mcgill.esce321.flightManagement.controller.request.CustomerRequestDTO;
    import ca.mcgill.esce321.flightManagement.controller.request.PersonRequestDTO;
    import ca.mcgill.esce321.flightManagement.dto.response.*;
    import ca.mcgill.esce321.flightManagement.service.PersonServiceImpl;
    import ca.mcgill.esce321.flightManagement.service.CustomerServiceImpl;



    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/persons")
    @CrossOrigin(origins = "http://localhost:5173")
    @Validated

    public class PersonController {
        @Autowired
        private PersonServiceImpl personService;

        @Autowired
        private CustomerServiceImpl customerService;


        @PostMapping("/login")
        public ResponseEntity<PersonResponse> login(@RequestBody PersonRequestDTO request) {
            return ResponseEntity.ok(personService.login(request));
        }

        @PostMapping("/signup")
        public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequestDTO request) {
            CustomerResponse created = customerService.createCustomer(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                             .body(created);
        }
    }

