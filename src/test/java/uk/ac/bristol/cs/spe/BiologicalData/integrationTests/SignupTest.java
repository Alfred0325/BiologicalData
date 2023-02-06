package uk.ac.bristol.cs.spe.BiologicalData.integrationTests;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.bristol.cs.spe.BiologicalData.Signup;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;
import uk.ac.bristol.cs.spe.BiologicalData.unitTests.DBContentStorageTest;
import uk.ac.bristol.cs.spe.BiologicalData.unitTests.PasswordHashing;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * testing the flow of signing up
 */
@SpringBootTest
public class SignupTest {

    public void hashingpassword() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PasswordHashing passwordHashing = new PasswordHashing();
        passwordHashing.HashPassword();
    }

    public void storeFeatureInTheDB() throws InvalidKeySpecException, NoSuchAlgorithmException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://biodb-3.camdnuwsjxkm.us-east-1.rds.amazonaws.com/biologicalData";
        String username = "admin";
        String password = "verylongpassword";
        Connection connection = DriverManager.getConnection(url, username, password);
        UserRepository repository = null;

        Signup mockSignup = new Signup("Dan", "dan@abc.com", "DanisbetterthanSimon");
        User mockUser = new User(mockSignup.getEmail(), mockSignup.getHashedPassword());
        repository.save(mockUser);

    }

    @Test
    public void combinedSignupTest() {
        try {
            hashingpassword();
            storeFeatureInTheDB();

        }catch (Exception e) {
            System.out.println("Signup Failed");
        }

    }
}
