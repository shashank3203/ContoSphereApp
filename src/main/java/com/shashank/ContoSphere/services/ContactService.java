package com.shashank.ContoSphere.services;

import com.shashank.ContoSphere.entity.Contact;
import com.shashank.ContoSphere.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ContactService {

    Contact saveContact(Contact contact);
    Contact update(Contact contact);
    List<Contact> getContacts();
    Contact getContactById(int id);
    void deleteContact(int id);
    List<Contact> searchContacts(String name, String email, String phoneNumber);
    List<Contact> getByUserId(int id); //for now no use

    // for pagination
    Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction);

    // for search functionality
    Page<Contact> searchByName(String nameKeyword, String sortBy, String direction, User user);
    Page<Contact> searchByEmail(String emailKeyword, String sortBy, String direction, User user);
    Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, String sortBy, String direction, User user);

//    Contact findById(int id);
}
