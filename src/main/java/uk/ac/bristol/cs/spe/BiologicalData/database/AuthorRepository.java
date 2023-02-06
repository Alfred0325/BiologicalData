package uk.ac.bristol.cs.spe.BiologicalData.database;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface AuthorRepository extends CrudRepository<Author, Long> {

    @Query(value = "SELECT * FROM author WHERE authorid = :a", nativeQuery = true)
    Author getAuthorByID(@Param("a") long id);
}
