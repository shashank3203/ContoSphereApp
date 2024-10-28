package com.shashank.ContoSphere.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "favorite")
    private boolean favorite=false;

    @Column(name = "websiteLink")
    private String websiteLink;

    @Column(name = "LinkedInLink")
    private String LinkedInLink;

    public String CloudinaryImagePublicId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "contact", cascade = CascadeType.ALL)
    private List<SocialLink> socialLinks = new ArrayList<>();


}
