package uk.ac.bristol.cs.spe.BiologicalData.database;

import lombok.Getter;
import lombok.Setter;
import uk.ac.bristol.cs.spe.BiologicalData.Login;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

@Entity
@Table(name="confirmationtoken")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="token_id")@Getter @Setter
    private long tokenid;

    @Column(name="confirmation_token")@Getter @Setter
    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)@Getter @Setter
    private Date createdDate;

    @Column(name = "user_id")@Getter @Setter
    private long userID;

    public ConfirmationToken() {
    }

    public ConfirmationToken(long userID) {
        this.userID = userID;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    public String getConfirmationToken() {
        return confirmationToken;
    }


}
