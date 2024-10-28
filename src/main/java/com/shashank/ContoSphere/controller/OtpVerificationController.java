package com.shashank.ContoSphere.controller;


import com.shashank.ContoSphere.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OtpVerificationController {

    @Autowired
    private UserService userService; // Inject UserService

    @GetMapping("/verify-otp")
    public String showOtpVerificationPage(@RequestParam String email, Model model) {
        System.out.println("Email to verify: " + email);
        model.addAttribute("email", email); // Set the email in the model
        return "verify-otp"; // Return your OTP verification page template
    }


    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp, Model model) {
        System.out.println("Email for OTP verification: " + email); // Debug output
        String storedOtp = userService.getOtp(email); // Fetch OTP from storage
        System.out.println("Stored OTP: " + storedOtp + ", Entered OTP: " + otp); // Debug output
        if (storedOtp != null && storedOtp.equals(otp)) {
            userService.enableUser(email);
            model.addAttribute("message", "Email verified successfully!");
            return "success-signup";
        } else {
            model.addAttribute("error", "Invalid OTP. Please try again.");
            return "verify-otp";
        }
    }
}
