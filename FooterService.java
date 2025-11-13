package com.example.yadavsajam.service;

import com.example.yadavsajam.model.FooterInfo;
import com.example.yadavsajam.repository.FooterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FooterService {

    @Autowired
    private FooterRepository footerRepository;

    public FooterInfo getFooterInfo() {
        List<FooterInfo> list = footerRepository.findAll();
        return list.isEmpty() ? null : list.get(0);
    }

    public FooterInfo saveFooterInfo(FooterInfo footerInfo) {
        return footerRepository.save(footerInfo);
    }

    public FooterInfo updateFooterInfo(Long id, FooterInfo updatedFooter) {
        FooterInfo existing = footerRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setAbout(updatedFooter.getAbout());
            existing.setEmail(updatedFooter.getEmail());
            existing.setPhone(updatedFooter.getPhone());
            existing.setFacebook(updatedFooter.getFacebook());
            existing.setInstagram(updatedFooter.getInstagram());
            existing.setTwitter(updatedFooter.getTwitter());
            existing.setLinkedin(updatedFooter.getLinkedin());
            return footerRepository.save(existing);
        }
        return null;
    }
}
