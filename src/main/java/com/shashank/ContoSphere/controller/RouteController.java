package com.shashank.ContoSphere.controller;


import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.exceptions.RetrieveEmailOfLoggedInUser;
import com.shashank.ContoSphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RouteController {
    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    // Add user information to model attribute before rendering views
    @ModelAttribute
    public void loggedInUserInformation(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }

        System.out.println("User information");
        String name = RetrieveEmailOfLoggedInUser.getEmailOfLoggedInUser(authentication);

        logger.info("User Logged-in: {}", name);

        User user = userService.getUserByEmail(name);
        
        if (user == null) {
            model.addAttribute("LoggedInUser", null);
        }
        System.out.println(user.getUserName());
        System.out.println(user.getEmail());

        model.addAttribute("loggedInUser", user);
    }
}
