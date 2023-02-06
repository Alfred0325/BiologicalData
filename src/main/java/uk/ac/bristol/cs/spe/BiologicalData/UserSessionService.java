package uk.ac.bristol.cs.spe.BiologicalData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import javax.servlet.http.HttpSession;

@Controller
public class UserSessionService {

    public static Long getUserID(UserRepository userRepo, HttpSession session){
        String email = (String) session.getAttribute("User");
        return userRepo.getUserIDByEmail(email);
    }

    public static String getUserEmail(HttpSession session){
        String email = (String) session.getAttribute("User");
        return email;
    }
}
