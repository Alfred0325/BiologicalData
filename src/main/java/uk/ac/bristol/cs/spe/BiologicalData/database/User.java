package uk.ac.bristol.cs.spe.BiologicalData.database;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing the information about a given user's login information.
 */
@Entity
@Table(name="user")
public class User {
    @Id @GeneratedValue @Column(name="userID") @Getter @Setter
    Long userID;

    @Column(name="useremail") @Getter @Setter
    String userEmail;

    @Column(name="userpwrdhash") @Getter @Setter
    String password;

    @Column(name="userenabled") @Getter @Setter
    int userEnabled = 0;

    @Column(name="userrole") @Getter @Setter
    String userRole = "ROLE_USER";

    public User() {

    }

    public User(String userEmail, String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

}
