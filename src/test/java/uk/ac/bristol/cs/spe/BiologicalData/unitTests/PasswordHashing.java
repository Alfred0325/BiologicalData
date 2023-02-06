package uk.ac.bristol.cs.spe.BiologicalData.unitTests;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.bristol.cs.spe.BiologicalData.Signup;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * test for the sign up function
 */
@SpringBootTest
public class PasswordHashing {

    @Test
    public void HashPassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        try {
            Signup mockSignup = new Signup("Dan", "dan@abc.com", "password");

            String hashedPassword = mockSignup.getHashedPassword();

            System.out.println("origin: " + mockSignup.getPassword());
            System.out.println("hashed: " + hashedPassword);
        }catch(Exception e) {
            System.out.println("hashing failed");
        }

    }
}
