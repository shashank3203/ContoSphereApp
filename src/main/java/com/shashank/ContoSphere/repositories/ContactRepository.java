package com.shashank.ContoSphere.repositories;

import com.shashank.ContoSphere.entity.Contact;
import com.shashank.ContoSphere.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

//    Custom Finder Method
    Page<Contact> findByUser(User user, Pageable pageable);

    @Query("select c from Contact c where c.user.id = :userId")
    List<Contact> findByUserId(@Param("userId") int id);

    // For Search Methods
    Page<Contact> findByUserAndNameContaining(User user, String nameKeyword, Pageable pageable);
    Page<Contact> findByUserAndEmailContaining(User user, String emailKeyword, Pageable pageable);
    Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneNumberKeyword, Pageable pageable);

}
