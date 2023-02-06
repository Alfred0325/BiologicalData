package uk.ac.bristol.cs.spe.BiologicalData.database;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface PageRepository extends CrudRepository<Page, Long> {

    @Modifying
    @Query(value = "UPDATE page SET datewritten = :d WHERE pagefile = :f", nativeQuery = true)
    void updateDate(@Param("d") Date d, @Param("f") String f);

    @Modifying
    @Query(value = "UPDATE page SET authorid = :a WHERE pagefile = :f", nativeQuery = true)
    void updateAuthor(@Param("a") Long a, @Param("f") String f);

    @Query(value = "SELECT * FROM page WHERE pagefile = :f", nativeQuery = true)
    Page getPageByName(@Param("f") String f);

    @Query(value = "SELECT * FROM page WHERE authorid = :a", nativeQuery = true)
    List<Page> getPagesByAuthor(@Param("a") int authorID);

    @Query(value = "SELECT * FROM page", nativeQuery = true)
    List<Page> getAll();
}
