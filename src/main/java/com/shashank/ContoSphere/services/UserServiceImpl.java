package com.shashank.ContoSphere.services;

import com.shashank.ContoSphere.entity.User;
import com.shashank.ContoSphere.exceptions.AppConstants;
import com.shashank.ContoSphere.exceptions.ResourceNotFoundException;
import com.shashank.ContoSphere.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    // Temporary storage for OTP (consider using a more robust solution)
    private Map<String, String> otpStorage = new HashMap<>();

    // Method to store the OTP for a user
    public void storeOtp(String email, String otp) {
        otpStorage.put(email, otp);
        System.out.println("Stored OTP for " + email + ": " + otp);
    }

    @Override
    public String getOtp(String email) {
        return otpStorage.get(email); // Fetch OTP from storage
    }

    @Override
    @Transactional
    public void enableUser(String email) {
        userRepository.enableUserByEmail(email);
    }

    // Generate and send OTP
    public void sendOtpEmail(String email) {
        String otp = String.valueOf((int) (Math.random() * 900000) + 100000); // Generate 6-digit OTP
        storeOtp(email, otp); // Store OTP for validation
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message);
    }

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRolesList(List.of(AppConstants.ROLE_USER));
        User savedUser = userRepository.save(user);
        
        // Send OTP email after user registration
        sendOtpEmail(savedUser.getEmail());
        
        return savedUser;
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User user2 = userRepository.findById(user.getId()).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        user2.setUserName(user.getUserName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setProviders(user.getProviders());
        user2.setProviderID(user.getProviderID());

        User save = userRepository.save(user2);
        return Optional.ofNullable(save);
    }

    @Override
    public void deleteUser(int id) {
        User user2 = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
        userRepository.deleteById(id);
    }

    @Override
    public boolean isUserExist(int id) {
        User user2 = userRepository.findById(id).orElse(null);
        return user2!=null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user!=null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
