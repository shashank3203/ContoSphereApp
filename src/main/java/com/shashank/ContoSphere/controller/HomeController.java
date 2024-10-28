package com.shashank.ContoSphere.controller;

import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.forms.UserForm;
import com.shashank.ContoSphere.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contosphere")
public class HomeController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(){
        System.out.println("Home Page Handler");
        return "home";
    }
    @GetMapping("/about")
    public String aboutPage(){
        System.out.println("About Page Handler");
        return "about";
    }
    @GetMapping("/services")
    public String servicePage(){
        System.out.println("Service Page Handler");
        return "services";
    }

    @GetMapping("/contact")
    public String contactPage() {
        System.out.println("Contact Page Handler");
        return "contact";
    }
    @GetMapping("/login")
    public String loginPage() {
        System.out.println("Login Page Handler");
        return "login";
    }
    @GetMapping("/signup")
    public String signupPage(Model theModel) {
        UserForm userForm=new UserForm();
        theModel.addAttribute("userForm", userForm);
        return "signup";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute UserForm userForm, BindingResult result) {
        if (result.hasErrors()) {
            return "signup"; // return to the signup view with errors
        }
        System.out.println("Registering");
        System.out.println(userForm);
        User user = new User();
        user.setUserName(userForm.getUserName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPassword(userForm.getPassword());
        user.setProfilePic("https://imgs.search.brave.com/wGgfdqlFqaMenZ26MF0WogbGt-djpTOK_PzwLaN1lPs/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly90NC5m/dGNkbi5uZXQvanBn/LzAwLzY0LzY3LzI3/LzM2MF9GXzY0Njcy/NzM2X1U1a3BkR3M5/a2VVbGw4Q1JRM3Az/WWFFdjJNNnFrVlk1/LmpwZw");

        User saveUser = userService.save(user);

        return "redirect:/verify-otp?email=" + saveUser.getEmail();


/*      // Using this we are unable to get the Default Values so that e are using a new method mentioned below

        User user = User.builder()
                .userName(userForm.getUserName())
                .email(userForm.getEmail())
                .phoneNumber(userForm.getPhoneNumber())
                .password(userForm.getPassword())
                .profilePic("https://imgs.search.brave.com/wGgfdqlFqaMenZ26MF0WogbGt-djpTOK_PzwLaN1lPs/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly90NC5m/dGNkbi5uZXQvanBn/LzAwLzY0LzY3LzI3/LzM2MF9GXzY0Njcy/NzM2X1U1a3BkR3M5/a2VVbGw4Q1JRM3Az/WWFFdjJNNnFrVlk1/LmpwZw")
                .build();

 */
    }
}
