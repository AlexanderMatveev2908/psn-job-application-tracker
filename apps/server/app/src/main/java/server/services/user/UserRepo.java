package server.services.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import server.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

    // @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery =
    // true)
    // User findUserByEmail(@Param("email") String email);

    // @Query("SELECT u FROM User u WHERE u.email = :email")
    // User findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM users WHERE email = ?1", nativeQuery = true)
    User findUserByEmail(String email);
}