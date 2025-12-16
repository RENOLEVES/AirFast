package ca.mcgill.esce321.flightManagement.service;

import ca.mcgill.esce321.flightManagement.repo.PersonRepository; // Assuming you have a UserRepository
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonRepository personRepository;

    /**
     * Locates the user based on the username.
     * @param email the email identifying the user whose data is required.
     * @return a fully populated user record (a UserDetails object)
     * @throws UsernameNotFoundException if the user could not be found
     */
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return personRepository.findByEmail(email) // Calls the method you defined!
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}