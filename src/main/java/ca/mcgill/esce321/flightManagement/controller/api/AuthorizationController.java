package ca.mcgill.esce321.flightManagement.controller.api;

import ca.mcgill.esce321.flightManagement.controller.request.LoginRequestDTO;
import ca.mcgill.esce321.flightManagement.dto.response.Response;
import ca.mcgill.esce321.flightManagement.repo.TokenRepository;
import ca.mcgill.esce321.flightManagement.service.PersonServiceImpl;
import ca.mcgill.esce321.flightManagement.service.token.CookieServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthorizationController {

    private final PersonServiceImpl personService;
    private final CookieServiceImpl cookieService;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthorizationController(PersonServiceImpl personService,
                                   CookieServiceImpl cookieService,
                                   TokenRepository tokenRepository) {
        this.personService = personService;
        this.cookieService = cookieService;
        this.tokenRepository = tokenRepository;
    }

    @GetMapping("/user")
    @CrossOrigin(origins = "*")
    public Response<?> getPerson() {

        // getAuthenticatedPerson
        return Response.ok().setPayload("TODO");
    }


    // TODO: rewrite person login service
//    @PostMapping("/signin")
//    public Response<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
//        personService.login(loginRequest, response);
//        return Response.ok().setPayload("TODO");
//    }

    // TODO: implement person signOut service
//    @GetMapping("/signout")
//    public Response<?> signOut(HttpServletRequest request, HttpServletResponse response) {
//        personService.signOut(request, response);
//        return Response.ok().setPayload("TODO");
//    }

    // TODO: implement person signup service
//    @PostMapping("/signup")
//    public Response<?> signUp(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) {
//    }

    // TODO： 1. read cuurent Token, 2. locate Token in database, 3. check the expiration 4. verify Token
//    @PostMapping("/authorized")
//    public Response<?> confirmUserAccount(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
//    }

    // TODO: 1. locate Person in database, 2. resend Token (generate a new TOken)
//    @GetMapping("/resend_token")
//    public Response<?> resendVerificationToken() {
//    }

    // TODO: 1. locate Person in database, 2. forgotPassword(email)
//    @GetMapping("/forgot_password")
//    public Response<?> forgotPassword(@RequestParam String username) {
//    }

    // TODO: 1. locate Person in database, 2. Email service, 3. check if the TOken is expired, 4. verify password reset token
//    @GetMapping("/reset_password")
//    public Response<?> resetPassword(@RequestParam String token) {
//    }

//    @PutMapping("/update_password")
//    public Response<?> setNewPassword(@RequestParam String token, @RequestBody PasswordChangeRequest passwordChangeRequest) {
//    }
}
