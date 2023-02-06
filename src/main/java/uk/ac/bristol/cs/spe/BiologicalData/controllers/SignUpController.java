package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.bristol.cs.spe.BiologicalData.EmailSenderService;
import uk.ac.bristol.cs.spe.BiologicalData.Signup;
import uk.ac.bristol.cs.spe.BiologicalData.database.ConfirmationToken;
import uk.ac.bristol.cs.spe.BiologicalData.database.ConfirmationTokenRepository;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    @Autowired
    UserRepository userRepo;
    
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @ModelAttribute("signup")
    public Signup signup() {
        return new Signup();
    }

    @GetMapping
    public String showForm() {
        return "signup";
    }

    @PostMapping
    public String submitForm(Model model, @Valid @ModelAttribute("signup") Signup signup, BindingResult bindingResult) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        
        if(userRepo.getUserCollectionByEmail(signup.getEmail()).size() > 0)
        {
            model.addAttribute("Existedmessage", "This email already exists!");
            return "signup";
        }
        if (!signup.getPassword().equals(signup.getComfirmPassword())){
            model.addAttribute("Existedmessage", "This passwords are not identical! Please check again!");
            return "signup";
        }
        
        if (signup.getComfirmPassword().equals(signup.getPassword())) {
            String hashedPassword = signup.getHashedPassword();
            //Get data which was added by the Thymeleaf engine
            User user = new User(signup.getEmail(), hashedPassword);
            userRepo.save(user); //Save new user in database
            ConfirmationToken confirmationToken = new ConfirmationToken(user.getUserID());

            confirmationTokenRepository.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getUserEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("biologicaldata@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
                  + "https://biologicaldata.spe.cs.bris.ac.uk/confirm-account?token=" + confirmationToken.getConfirmationToken());
            emailSenderService.sendEmail(mailMessage);
            
            // go back to the home page after signed up
            return "index";
        }
        return "signup";
    }
}

