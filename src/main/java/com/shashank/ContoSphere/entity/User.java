package com.shashank.ContoSphere.entity;


import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    @Getter(value = AccessLevel.NONE)
    private String password;

    @Column(name = "about", length = 1000)
    private String about;

    @Column(name = "ProfilePic", length = 1000)
    private String profilePic;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender")
    private String gender;

    @Column(name = "enabled")
    @Getter(value = AccessLevel.NONE)
    private boolean enabled=true;

    @Column(name = "emailVerified")
    private boolean emailVerified=false;

    @Column(name = "providers")
    @Enumerated(EnumType.STRING)
    private Providers providers=Providers.SELF;

    @Column(name = "providerID")
    private String providerID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Contact> contacts=new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> rolesList=new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", about='" + about + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", enabled=" + enabled +
                ", emailVerified=" + emailVerified +
                ", providers=" + providers +
                ", providerID='" + providerID + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles = rolesList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}