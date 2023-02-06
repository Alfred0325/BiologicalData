package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uk.ac.bristol.cs.spe.BiologicalData.Login;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Controller class for managing login capabilities.
 */
@Controller
public class LoginController {

  @Autowired
  UserRepository userRepo;
  
  @GetMapping("/login")
  public String loadLogin(Model model, HttpSession session) {
    if (session.getAttribute("User") == null) {
        model.addAttribute("login", new Login());
        return "login";
    }



    return "redirect:/";
  }
  
  @PostMapping("/login/submit")
  public String submitLogin( @ModelAttribute Login login, Model model, HttpServletRequest request, HttpSession session) throws InvalidKeySpecException, NoSuchAlgorithmException, ServletException {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      model.addAttribute("login", login);
      String hashedPassword = login.getHashedPassword();
      User newUser = userRepo.getUserByEmail(login.getEmail());
      if (newUser == null) {
          model.addAttribute("Errormessage", "This account doesn't exist! Please check again!");
          return "login";
      }
      if (newUser.getUserEnabled() != 1) {
          model.addAttribute("Errormessage", "This account hasn't been activated! Please check your Email!");
          return "login";
      }


      if (encoder.matches(login.getPassword(), newUser.getPassword()) && (newUser.getUserEnabled() == 1)) {
          session.setAttribute("User", newUser.getUserEmail());

          session.setAttribute("selectionText", "");
          session.setAttribute("selectionName", "");
          session.setAttribute("imageLeft", true);
          session.setAttribute("transcriptionName", null);

          //sets your session to never(-ish) expire once you've loaded the transform page
          session.setMaxInactiveInterval(Integer.MAX_VALUE);

          return "redirect:/";
      } else {
          model.addAttribute("Errormessage", "You entered the wrong password!");
          return "login";
      }


  }
  
  @GetMapping("/logout")
  public String loadLogout(Model model, HttpSession session) {
      if (session.getAttribute("User") != null) {
          return "logout";
      }
      return "redirect:/";
  }

  @PostMapping("/signitout")
  public String Logout (HttpSession session){
      session.removeAttribute("User");

      if (session != null) {
          session.invalidate();
      }

      return "redirect:/";
  }
  
}
