package com.shashank.ContoSphere.services;

import com.shashank.ContoSphere.entity.Contact;
import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.exceptions.ResourceNotFoundException;
import com.shashank.ContoSphere.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService{


    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact update(Contact contact) {

        Contact oldContact = contactRepository.findById(contact.getId()).orElseThrow(()-> new ResourceNotFoundException("Contact not found"));
        oldContact.setName(contact.getName());
        oldContact.setEmail(contact.getEmail());
        oldContact.setAddress(contact.getAddress());
        oldContact.setPhoneNumber(contact.getPhoneNumber());
        oldContact.setDescription(contact.getDescription());
        oldContact.setPicture(contact.getPicture());
        oldContact.setFavorite(contact.isFavorite());
        oldContact.setLinkedInLink(contact.getLinkedInLink());
        oldContact.setWebsiteLink(contact.getWebsiteLink());
        oldContact.setCloudinaryImagePublicId(contact.getCloudinaryImagePublicId());
        oldContact.setSocialLinks(contact.getSocialLinks());
        return contactRepository.save(oldContact);
    }

    @Override
    public List<Contact> getContacts() {
        return contactRepository.findAll();
    }

    @Override
    public Contact getContactById(int id) {
        return contactRepository.findById(id).orElseThrow(()-> new RuntimeException("Contact Not Found"));
    }

    @Override
    public void deleteContact(int id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> searchContacts(String name, String email, String phoneNumber) {
        return List.of();
    }

    @Override
    public List<Contact> getByUserId(int id) {

        contactRepository.findByUserId(id);
        return List.of();
    }

    @Override
    public Page<Contact> getByUser(User user, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size);
        return contactRepository.findByUser(user, pageable);
    }

    @Override
    public Page<Contact> searchByName(String nameKeyword, String sortBy, String direction, User user) {
        int defaultPage = 0; // Default page number
        int defaultSize = 1000; // Default size (number of items per page)

        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(defaultPage, defaultSize, sort);
        return contactRepository.findByUserAndNameContaining(user, nameKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByEmail(String emailKeyword, String sortBy, String direction, User user) {
        int defaultPage = 0; // Default page number
        int defaultSize = 1000; // Default size (number of items per page)

        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(defaultPage, defaultSize, sort);
        return contactRepository.findByUserAndEmailContaining(user, emailKeyword, pageable);
    }

    @Override
    public Page<Contact> searchByPhoneNumber(String phoneNumberKeyword, String sortBy, String direction, User user) {
        int defaultPage = 0; // Default page number
        int defaultSize = 1000; // Default size (number of items per page)

        Sort sort = direction.equals("desc")? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(defaultPage, defaultSize, sort);
        return contactRepository.findByUserAndPhoneNumberContaining(user, phoneNumberKeyword, pageable);
    }


//    @Override
//    public Contact findById(int id) {
//        Optional<Contact> result = contactRepository.findById(id);
//
//        Contact contact = null;
//
//        if(result.isPresent())
//            contact=result.get();
//        else
//            throw new RuntimeException("Did not find Employee ID: " + id);
//        return contact;
//    }
}
