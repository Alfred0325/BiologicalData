package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.ac.bristol.cs.spe.BiologicalData.EmailSenderService;
import uk.ac.bristol.cs.spe.BiologicalData.Login;
import uk.ac.bristol.cs.spe.BiologicalData.ResetUserInfo;
import uk.ac.bristol.cs.spe.BiologicalData.Signup;
import uk.ac.bristol.cs.spe.BiologicalData.database.ConfirmationToken;
import uk.ac.bristol.cs.spe.BiologicalData.database.ConfirmationTokenRepository;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

import javax.validation.Valid;


@Controller
public class ResetPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    @RequestMapping(value="/forgotPassword", method=RequestMethod.GET)
    public ModelAndView displayResetPassword(ModelAndView modelAndView, @ModelAttribute("user") ResetUserInfo user) {
        modelAndView.addObject("user", user);
        modelAndView.setViewName("forgotPassword");
        return modelAndView;
    }

    // Receive the address and send an email
    @RequestMapping(value="/forgotPassword", method=RequestMethod.POST)
    public ModelAndView forgotUserPassword(ModelAndView modelAndView, ResetUserInfo user) {

        if (userRepository.getUserCollectionByEmail(user.getEmail()).size() > 0) {
            User existingUser = userRepository.getUserByEmail(user.getEmail());
            // Create token
            ConfirmationToken confirmationToken = new ConfirmationToken(existingUser.getUserID());

            // Save it
            confirmationTokenRepository.save(confirmationToken);

            // Create the email
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(existingUser.getUserEmail());
            mailMessage.setSubject("Complete Password Reset!");
            mailMessage.setFrom("biologicaldata@gmail.com");
            mailMessage.setText("To complete the password reset process, please click here: "
                    + "http://localhost:8080/confirm-reset?token="+confirmationToken.getConfirmationToken());
                  //+ "https://biologicaldata.spe.cs.bris.ac.uk/confirm-reset?token="+confirmationToken.getConfirmationToken());
            // Send the email
            emailSenderService.sendEmail(mailMessage);

            modelAndView.addObject("message", "Request to reset password received. Check your inbox for the reset link.");
            modelAndView.setViewName("successForgotPassword");

        } else {
            modelAndView.addObject("message", "This email address does not exist!");
            modelAndView.setViewName("resetErrorMessage");
        }
        return modelAndView;
    }

    // Endpoint to confirm the token
    @RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(ModelAndView modelAndView, @RequestParam("token")String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.getConfirmationTokenByToken(confirmationToken);

        if (token != null) {
            User user = userRepository.getUserById(token.getUserID());
            ResetUserInfo newUser= new ResetUserInfo();
            newUser.setEmail(user.getUserEmail());
            // userRepository.save(user);
            modelAndView.addObject("user", newUser);
            modelAndView.addObject("email", newUser.getEmail());

            modelAndView.setViewName("resetPassword");
        }else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("resetErrorMessage");
        }
        return modelAndView;
    }

    // Endpoint to update a user's password
    @RequestMapping(value = "/reset-Password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(ModelAndView modelAndView, @Valid ResetUserInfo user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("message", "The password should contain at least one upper case, one lower case, one special case, one number and at least 8 characters!");
            ResetUserInfo newUser= new ResetUserInfo();
            newUser.setEmail(user.getEmail());
            modelAndView.addObject("user", newUser);
            modelAndView.addObject("email", newUser.getEmail());
            modelAndView.setViewName("resetPassword");
            return modelAndView;
        }

        if (user.getEmail() != null) {
            if (userRepository.getUserCollectionByEmail(user.getEmail()).size() == 0) {
                modelAndView.addObject("message", "You have not signed up with this email address, please check again!");
                ResetUserInfo newUser= new ResetUserInfo();
                newUser.setEmail(user.getEmail());
                modelAndView.addObject("user", newUser);
                modelAndView.addObject("email", newUser.getEmail());
                modelAndView.setViewName("resetPassword");
            }
            // Use email to find user
            User tokenUser = userRepository.getUserByEmail(user.getEmail());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            tokenUser.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(tokenUser);

            modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
            modelAndView.setViewName("successResetPassword");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("resetErrorMessage");
        }
        return modelAndView;
    }



}
