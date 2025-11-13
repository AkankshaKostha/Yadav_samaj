package com.example.yadavsajam.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "home_section")
public class HomeSection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    @ElementCollection
    private List<String> imageUrls = new ArrayList<>();
    private String imageUrl;
    private String route; // for clickable cards
    private Boolean active = true;
    

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private SectionType type;// BANNER or CARD



    public enum SectionType {
        BANNER,
        CARD,
        AD,
        EVENT,
        ANNOUNCEMENT,
        ACTIVITY,
        MATRIMONIAL_BANNER,
        MATRIMONIAL_AD,
        SAMAJ_BANNER,
        SAMAJ_AD,
        MEMBERSHIP_BANNER,
        MEMBERSHIP_ANNOUNCEMENT,
        MEMBERSHIP_EVENT,
        FAQ,
        HERO,
        MEMBERSHIP_ACTIVITY,
        FEATURED_PROFILE,
        COMMUNITY ,
        LEADERSHIP ,
        GENERAL,MEMBERSHIP_PHOTO,
        
    }




}
