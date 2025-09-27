package ca.mcgill.esce321.flightManagement.repo;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.esce321.flightManagement.model.User;

public interface UserRepository extends CrudRepository<User, Integer>{
    User findPersonById(int id);
}