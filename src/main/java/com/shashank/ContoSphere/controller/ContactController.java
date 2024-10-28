package com.shashank.ContoSphere.controller;

import com.shashank.ContoSphere.entity.Contact;
import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.exceptions.AppConstants;
import com.shashank.ContoSphere.exceptions.RetrieveEmailOfLoggedInUser;
import com.shashank.ContoSphere.forms.ContactForm;
import com.shashank.ContoSphere.services.ContactService;
import com.shashank.ContoSphere.services.ImageService;
import com.shashank.ContoSphere.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/user/contact")
public class ContactController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserService userService;

    @GetMapping("/contacts")
    public String addContact() {
        return "user/contacts";
    }

    @GetMapping("/addcontactform")
    public String showAddContactForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "user/add-contact-form";
    }

    @PostMapping("/processcontact")
    public String processContact(
            @Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "user/add-contact-form"; // Return to form if validation fails
        }

        String username = RetrieveEmailOfLoggedInUser.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Contact contact = new Contact();

        try {
            // Handle image upload
            String fileName = UUID.randomUUID().toString();
            String fileURL = imageService.uploadImage(contactForm.getContactImage(), fileName);
            contact.setCloudinaryImagePublicId(fileName);
            contact.setPicture(fileURL);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Image upload failed: " + e.getMessage());
            return "redirect:/user/contact/addcontactform";
        }

        // Set contact details
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);

        // Save the contact
        contactService.saveContact(contact);
        redirectAttributes.addFlashAttribute("successMessage", "Contact added successfully!");
        return "redirect:/user/contact/viewcontacts"; // Redirect to view contacts page
    }

    @GetMapping("/viewcontacts")
    public String viewContacts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        String username = RetrieveEmailOfLoggedInUser.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        Page<Contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);
        return "user/view-contacts";
    }

    @GetMapping("/search")
    public String searchContact(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        User user = userService.getUserByEmail(RetrieveEmailOfLoggedInUser.getEmailOfLoggedInUser(authentication));
        Page<Contact> pageContact = searchContacts(keyword, user, sortBy, direction);

        model.addAttribute("pageContact", pageContact);
        model.addAttribute("keyword", keyword); // Retain the keyword for the search input
        return "user/search-contact";
    }

    private Page<Contact> searchContacts(String keyword, User user, String sortBy, String direction) {
        if (isValidEmail(keyword)) {
            return contactService.searchByEmail(keyword, sortBy, direction, user);
        } else if (isValidPhoneNumber(keyword)) {
            return contactService.searchByPhoneNumber(keyword, sortBy, direction, user);
        } else {
            return contactService.searchByName(keyword, sortBy, direction, user);
        }
    }

    @GetMapping("/findcontact")
    public String findById(@RequestParam("contactId") int theId, Model model) {
        Contact contact = contactService.getContactById(theId);
        ContactForm contactForm = new ContactForm();

        // Populate the contact form with existing contact data
        contactForm.setName(contact.getName());
        contactForm.setEmail(contact.getEmail());
        contactForm.setAddress(contact.getAddress());
        contactForm.setPhoneNumber(contact.getPhoneNumber());
        contactForm.setDescription(contact.getDescription());
        contactForm.setFavorite(contact.isFavorite());
        contactForm.setLinkedInLink(contact.getLinkedInLink());
        contactForm.setWebsiteLink(contact.getWebsiteLink());
        contactForm.setSocialLinks(contact.getSocialLinks());

        model.addAttribute("contactId", contact.getId());
        model.addAttribute("contactForm", contactForm);
        return "user/contact-detail";
    }

    @PostMapping("/updatecontact")
    public String updateContact(
            @Valid @RequestParam("contactId") int theId,
            @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please fix the errors in the form.");
            return "redirect:/user/contact/findcontact?contactId=" + theId; // Redirect with error message
        }

        Contact contact = contactService.getContactById(theId);
        contact.setName(contactForm.getName());
        contact.setEmail(contactForm.getEmail());
        contact.setAddress(contactForm.getAddress());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setDescription(contactForm.getDescription());
        contact.setFavorite(contactForm.isFavorite());
        contact.setLinkedInLink(contactForm.getLinkedInLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setSocialLinks(contactForm.getSocialLinks());

        // Handle image upload if a new image is provided
        if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString();
                String imageUrl = imageService.uploadImage(contactForm.getContactImage(), fileName);
                contact.setCloudinaryImagePublicId(fileName);
                contact.setPicture(imageUrl);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Image upload failed: " + e.getMessage());
                return "redirect:/user/contact/findcontact?contactId=" + theId;
            }
        }

        contactService.update(contact);
        redirectAttributes.addFlashAttribute("successMessage", "Contact updated successfully!");
        return "redirect:/user/contact/findcontact?contactId=" + theId; // Redirect to the contact detail page
    }

    @GetMapping("/delete")
    public String deleteContact(@RequestParam("contactId") int theId, RedirectAttributes redirectAttributes) {
        contactService.deleteContact(theId);
        redirectAttributes.addFlashAttribute("successMessage", "Contact deleted successfully!");
        return "redirect:/user/contact/viewcontacts";
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    // Phone number validation method
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{1,10}"); // Adjust regex as needed
    }
}
