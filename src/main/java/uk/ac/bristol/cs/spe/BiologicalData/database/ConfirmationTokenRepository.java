package uk.ac.bristol.cs.spe.BiologicalData.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long> {
    @Query(value = "SELECT * FROM confirmationtoken WHERE confirmation_token = :e", nativeQuery = true)
    ConfirmationToken getConfirmationTokenByToken(@Param("e") String token);
}
