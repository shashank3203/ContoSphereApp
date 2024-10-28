package com.shashank.ContoSphere.forms;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForm {

    @NotBlank(message = "Full Name is required")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "\\+?[0-9()-]{7,}", message = "Please enter a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[\\W_]).{8,}$",
            message = "Password must be at least 8 characters long, " +
                    "including at least one uppercase letter, " +
                    "one lowercase letter, " +
                    "one digit, and one special character.")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }

}
