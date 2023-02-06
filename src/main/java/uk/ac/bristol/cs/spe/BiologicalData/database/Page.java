package uk.ac.bristol.cs.spe.BiologicalData.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Class representing the information about images stored within the database.
 */
@Entity
@Table(name="page")
public class Page {
    @Id @GeneratedValue @Getter @Setter
    Long pageID;

    @Column(name="pagefile") @Getter @Setter
    String pageFile;

    @ManyToOne @JoinColumn(name="authorID") @Getter @Setter
    Author authorID;

    @Column(name="datewritten") @Getter @Setter
    Date date;

    public Page() {

    }

    public Page(String file) {
        pageFile = file;
    }

}
