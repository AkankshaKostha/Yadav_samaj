package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.FooterInfo;
import com.example.yadavsajam.repository.FooterRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FooterController {

    private final FooterRepository footerRepository;

    public FooterController(FooterRepository footerRepository) {
        this.footerRepository = footerRepository;
    }

    @GetMapping("/api/footer")
    public FooterInfo getFooterInfo() {
        // Fetch the first row from the database
        Optional<FooterInfo> footerOpt = footerRepository.findById(1L);

        if (footerOpt.isPresent()) {
            return footerOpt.get();
        } else {
            // fallback in case the table is empty
            FooterInfo info = new FooterInfo();
            info.setAbout("Yadav Samaj is a cultural and social platform connecting people and promoting unity.");
            info.setEmail("Rupeshsingh@gmail.com");
            info.setPhone("+91-9981932922");
            info.setAddress("केन्द्रीय कार्यालय सी-65 शकुंतला पुरी, थाटीपुर, मोरार, ग्वालियर (म.प्र.), 474004");
            info.setFacebook("https://facebook.com/yadavsa");
            info.setInstagram("https://instagram.com/yadavsa");
            info.setWhatsapp("https://wa.me/919981932922");
            info.setGoogle("https://www.google.com/maps/place/Your+Address");
            return info;
        }
    }
}
