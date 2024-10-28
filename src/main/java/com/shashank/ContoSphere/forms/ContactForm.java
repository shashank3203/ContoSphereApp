package com.shashank.ContoSphere.forms;

import com.shashank.ContoSphere.entity.SocialLink;
import com.shashank.ContoSphere.validators.ValidFile;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ContactForm {

    @NotBlank(message = "Full Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;
    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "\\+?[0-9()-]{7,}", message = "Please enter a valid phone number")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;
    @ValidFile(message="File Cannot be Empty")
    private MultipartFile contactImage;
    @NotBlank(message = "Please enter a description")
    private String description;
    @Column(name = "favorite")
    private boolean favorite=false;

    @Column(name = "websiteLink")
    private String websiteLink;

    @Column(name = "LinkedInLink")
    private String LinkedInLink;

    private String picture;

    private List<SocialLink> socialLinks = new ArrayList<>();
}
