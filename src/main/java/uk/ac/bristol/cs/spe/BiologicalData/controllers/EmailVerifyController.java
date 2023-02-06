package uk.ac.bristol.cs.spe.BiologicalData.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import uk.ac.bristol.cs.spe.BiologicalData.database.ConfirmationToken;
import uk.ac.bristol.cs.spe.BiologicalData.database.ConfirmationTokenRepository;
import uk.ac.bristol.cs.spe.BiologicalData.database.User;
import uk.ac.bristol.cs.spe.BiologicalData.database.UserRepository;

@Controller
public class EmailVerifyController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView confirmUserAccount(ModelAndView modelAndView, @RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.getConfirmationTokenByToken(confirmationToken);
        if(token != null)
        {
            System.out.println(token.getUserID());
            User user = userRepo.getUserById(token.getUserID());
            user.setUserEnabled(1);
            userRepo.save(user);
            modelAndView.setViewName("accountVerified");
        }
        else
        {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("resetErrorMessage");
        }

        return modelAndView;
    }
}
