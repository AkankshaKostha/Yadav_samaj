package com.example.yadavsajam.repository;

import com.example.yadavsajam.model.HomeSection;
import com.example.yadavsajam.model.HomeSection.SectionType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeSectionRepository extends JpaRepository<HomeSection, Long> {
	 List<HomeSection> findByType(HomeSection.SectionType type);
	    List<HomeSection> findByTypeAndActiveTrue(HomeSection.SectionType type);
	    List<HomeSection> findByType(String type);
	    
}
