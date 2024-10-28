package com.shashank.ContoSphere.repositories;


import com.shashank.ContoSphere.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Extra methods for DB Related operations
    //  Custom Query Methods
    //  Custom Finder Methods

    Optional<User> findByEmail(String email);
    @Modifying
    @Query("UPDATE User u SET u.enabled = true WHERE u.email = ?1")
    void enableUserByEmail(String email);
}
