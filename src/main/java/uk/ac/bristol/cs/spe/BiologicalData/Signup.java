package uk.ac.bristol.cs.spe.BiologicalData;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * Class used to represent and store the data give when a user tries to sign up. Captures data from the signup form.
 */
public class Signup {

    @Getter @Setter
    private String userID;
    @Getter @Setter
    private String username;
    @Getter @Setter
    private String email;
    @Getter @Setter
    @ValidPassword
    private String password;
    @Getter @Setter
    private String comfirmPassword;

    public Signup() {
    }

    public Signup(String username, String email, String password) {
        this.userID = Util.GenUUID();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the hash of the password stored in this object. To be stored in the database.
     * @return the hashed password
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public String getHashedPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        return encodedPassword;
    }

}
