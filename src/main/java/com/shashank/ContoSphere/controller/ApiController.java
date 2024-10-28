package com.shashank.ContoSphere.controller;

import com.shashank.ContoSphere.entity.Contact;
import com.shashank.ContoSphere.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;
    // Get Contacts of Users
    @GetMapping("/contacts/{contactId}")
    public Contact getContacts(@PathVariable("contactId") int contactId){
        return contactService.getContactById(contactId);
    }
}
