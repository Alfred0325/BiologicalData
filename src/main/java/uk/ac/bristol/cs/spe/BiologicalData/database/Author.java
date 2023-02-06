package uk.ac.bristol.cs.spe.BiologicalData.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representing the information about the author who has written a given transcription in the database.
 */
@Entity
@Table(name="author")
public class Author {
    @Id @GeneratedValue @Column(name="authorID") @Getter @Setter
    Long authorID;

    //The column name has to be all lowercase otherwise Spring will try to parse it with underscores
    @Column(name="authorforename") @Getter @Setter
    String authorForename;

    @Column(name="authorsurname") @Getter @Setter
    String authorSurname;

    @Column(name="authortitle") @Getter @Setter
    int authorTitle;

    public Author() {

    }

    public Author(String forename, String surname, int title) {
        authorForename = forename;
        authorSurname = surname;
        authorTitle = title;
    }

    public String titleToString(){
        switch(authorTitle){
            case 0:
                return "Dr.";
            case 1:
                return "Prof.";
            case 2:
                return "Mr.";
            case 3:
                return "Mrs.";
            case 4:
                return "Miss";
            case 5:
                return "Master";
            default:
                return "Who?";
        }
    }
}
