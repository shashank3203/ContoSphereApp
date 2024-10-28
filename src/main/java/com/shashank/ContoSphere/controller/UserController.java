package com.shashank.ContoSphere.controller;


import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.exceptions.RetrieveEmailOfLoggedInUser;
import com.shashank.ContoSphere.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/signed-in")
    public String signedIn(){
        return "success-signin";
    }

    @RequestMapping("/dashboard")
    public String userDashboard(Authentication authentication){
        String name = RetrieveEmailOfLoggedInUser.getEmailOfLoggedInUser(authentication);

        logger.info("User Logged-in: {}", name);

        User user = userService.getUserByEmail(name);
        System.out.println(user.getUserName());
        System.out.println(user.getEmail());
        return "user/dashboard";
    }

    @RequestMapping("/profile")
    public String userProfile(){
        return "user/profile";
    }
}
