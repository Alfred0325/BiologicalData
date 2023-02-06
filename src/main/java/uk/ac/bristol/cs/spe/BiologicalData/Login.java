package uk.ac.bristol.cs.spe.BiologicalData;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Class used to represent and store the data give when a user tries to log in. Captures data from the login form.
 */
public class Login {

    @Getter @Setter
    private String email;
    @Getter @Setter
    private String password;

    @Autowired
    UserRepository userRepo;

    /**
     * Returns the hash of the password stored in this object. To be used for authentication.
     * @return the hashed password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public String getHashedPassword() throws NoSuchAlgorithmException, InvalidKeySpecException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        return encodedPassword;
    }

    public boolean compareHashedPassword(String username, String password) {
        User u = userRepo.getUserByEmail(username);
        if(u.getPassword().equals(password))
            return true;
        return false;
    }

}
