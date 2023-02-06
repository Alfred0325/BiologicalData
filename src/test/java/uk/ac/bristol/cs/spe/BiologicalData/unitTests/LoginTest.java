package uk.ac.bristol.cs.spe.BiologicalData.unitTests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.bristol.cs.spe.BiologicalData.Login;

/**
 * unit test for login systems
 */
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    private static final String f = "Login successfully";

    @Test
    public void readStringFromContext() {
        Login testLogin = new Login();

        boolean result = testLogin.compareHashedPassword("lyt@outlook.com",
                "$2a$10$eXze/LR/Nahcvhuru8OBru8FTDa8yCvDh1M1MSrngXW3Z9Zdg7ddS");

        if (result) {
            System.out.println("login successfully");
        }
        else {
            System.out.println("login failed");
        }
    }
}
