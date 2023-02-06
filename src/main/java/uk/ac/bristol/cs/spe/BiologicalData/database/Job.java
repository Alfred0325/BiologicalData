package uk.ac.bristol.cs.spe.BiologicalData.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Class representing the information about a given job which links images, text and users.
 */
@Entity
@Table(name="job")
public class Job {
    @Id @GeneratedValue @Column(name="jobID") @Getter @Setter
    Long jobID;

    @ManyToOne @JoinColumn(name="userID") @Getter @Setter
    User userID;

    @ManyToOne @JoinColumn(name="pageID") @Getter @Setter
    Page pageID;

    @Column(name="datesubmitted") @Getter @Setter
    Timestamp dateSubmitted;

    @Column(name="jobfile") @Getter @Setter
    String jobFile;

    public Job() {

    }

    //Not sure yet how constructors work with foreign keys
    public Job(User userID, Page pageID, Timestamp date, String file) {
        dateSubmitted = date;
        jobFile = file;
        this.pageID = pageID;
        this.userID = userID;
    }

}
