package uk.ac.bristol.cs.spe.BiologicalData.database;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value = "SELECT * FROM user WHERE userEmail = :e", nativeQuery = true)
    User getUserByEmail(@Param("e") String email);

    @Query(value = "SELECT * FROM user WHERE userEmail = :c", nativeQuery = true)
    Collection<User> getUserCollectionByEmail(@Param("c") String email);

    @Query(value = "SELECT * FROM user WHERE userID = :a", nativeQuery = true)
    User getUserById(@Param("a") long userID);

    @Query(value = "SELECT userID FROM user WHERE userEmail = :e", nativeQuery = true)
    Long getUserIDByEmail(@Param("e") String email);

    @Query(value = "SELECT userRole FROM user WHERE userID = :a", nativeQuery = true)
    String getRoleByID(@Param("a") long userID);

    @Modifying
    @Query(value = "UPDATE user SET userEmail = :e WHERE userEmail = :oe", nativeQuery = true)
    void updateUserEmail(@Param("e") String email, @Param("oe") String oldEmail);
}
