package ca.mcgill.esce321.flightManagement.repo;

import ca.mcgill.esce321.flightManagement.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);
    Optional<Token> findByPerson(Long id);
}