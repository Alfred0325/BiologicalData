package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.bristol.cs.spe.BiologicalData.Transcription;

import javax.servlet.http.HttpSession;

/**
 * Controller class for the main page of the BiologicalData website.
 */
@Controller
public class BiologicalDataController {
    /**
     * Loads the index html page upon accessing the webapp.
     */
    @GetMapping("/")
    public String loadIndex(Model model, HttpSession session) {
        model.addAttribute("sessionId", session.getId());

        if (session.getAttribute("User") != null ) {
            model.addAttribute("userEmail", (String) session.getAttribute("User"));
            model.addAttribute("Logout", "Log Out");
        } else{
            model.addAttribute("userEmail", "Please sign in first");
            model.addAttribute("Login", "Log in");
        }


        return "index";
    }

    @GetMapping("/about-us")
    public String loadAbout(){
        return "about-us";
    }

    @GetMapping("/faqs")
    public String loadFAQS(){
        return "faqs";
    }

    @GetMapping("/instructions")
    public String loadInstructions(){
        return "instructions";
    }

    @GetMapping("/overwrite")
    public String loadOverwrite() { return "overwrite"; }
}
