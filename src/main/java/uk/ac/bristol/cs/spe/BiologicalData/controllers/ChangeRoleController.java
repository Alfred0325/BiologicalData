package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import javax.servlet.http.HttpSession;

@Controller
public class ChangeRoleController {


    @Autowired
    UserRepository userRepo;

    @GetMapping(value = "/change")
    public String loadChange(Model model, HttpSession session) {
        String userNew = (String) session.getAttribute("User");
        if (userNew != null) {
            User userAuthed = userRepo.getUserByEmail(userNew);
            if (userAuthed.getUserRole().equals ("ROLE_ADMIN")) {
                model.addAttribute("users", userRepo.findAll());
                return "change";
            }
        }

        return "403";

    }

    @GetMapping(value = "/change/update/{user}")
    public String submitForm(@PathVariable User user) {
        if (userRepo.getRoleByID(user.getUserID()).equals("ROLE_USER")) {
            user.setUserRole("ROLE_ADMIN");
        }
        else if (userRepo.getRoleByID(user.getUserID()).equals("ROLE_ADMIN")) {
            user.setUserRole("ROLE_USER");
        }
        return "redirect:/change";
    }

}

