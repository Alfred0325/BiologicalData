package uk.ac.bristol.cs.spe.BiologicalData.database;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;

public interface JobRepository extends CrudRepository<Job, Long> {

    @Query(value = "SELECT * FROM job j WHERE j.pageid = :p", nativeQuery = true)
    Collection<Job> getJobByPage(@Param("p") Long p);

    @Query(value = "SELECT * FROM job j WHERE j.jobfile = :f", nativeQuery = true)
    Job getJobByName(@Param("f") String f);

    @Query(value = "SELECT * FROM job j WHERE j.jobid = :id", nativeQuery = true)
    Job getJobByID(@Param("id") Long id);

    @Query(value = "SELECT * FROM job j WHERE j.userid = :uid", nativeQuery = true)
    Collection<Job> getJobByUser(@Param("uid") Long uid);

    @Query(value = "SELECT * FROM job j WHERE j.jobfile = :f and j.pageid = :p", nativeQuery = true)
    Job getJobByNameAndPage(@Param("f") String f, @Param("p") Long p);

    @Modifying
    @Query(value = "UPDATE job j SET datesubmitted = :d WHERE jobid = :id", nativeQuery = true)
    void updateDate(@Param("d") Date d, @Param("id") long jobid);

    @Modifying
    @Query(value = "DELETE FROM job WHERE jobID = :id", nativeQuery = true)
    void deleteByJobID(@Param("id") long jobid);
}
