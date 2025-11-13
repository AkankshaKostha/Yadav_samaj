package com.example.yadavsajam.service;

import com.example.yadavsajam.model.HomeSection;
import com.example.yadavsajam.repository.HomeSectionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HomeSectionService {

	 @Autowired
	    private HomeSectionRepository repo;
	 @Value("${file.upload-dir}")
	 private String uploadDir;

	    public HomeSection createSection(String type, String title, String description, String route, List<MultipartFile> files) {
	        HomeSection section = new HomeSection();
	        section.setType(HomeSection.SectionType.valueOf(type));

	        section.setTitle(title);
	        section.setDescription(description);
	        section.setRoute(route);

	        if (files != null && !files.isEmpty()) {
	            List<String> imageUrls = saveFiles(files);
	            section.setImageUrls(imageUrls);
	        }

	        return repo.save(section);
	    }

	    public HomeSection updateSection(Long id, String title, String description, String route, List<MultipartFile> files) {
	        HomeSection section = repo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Section not found"));

	        section.setTitle(title);
	        section.setDescription(description);
	        section.setRoute(route);

	        if (files != null && !files.isEmpty()) {
	            List<String> imageUrls = saveFiles(files);
	            section.setImageUrls(imageUrls); // Replace old images
	        }

	        return repo.save(section);
	    }

	    public List<String> saveFiles(List<MultipartFile> files) {
	        List<String> urls = new ArrayList<>();
	        for (MultipartFile file : files) {
	            try {
	                String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	                Path path = Paths.get(uploadDir, filename);
	                Files.write(path, file.getBytes());
	                urls.add("/uploads/" + filename);
	            } catch (IOException e) {
	                throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
	            }
	        }
	        return urls;
	    }
	    public List<HomeSection> getSectionsByType(String type) {
	        return repo.findByType(type);
	    }

	    public void deleteSection(Long id) {
	        repo.deleteById(id);
	    }

public HomeSectionService(HomeSectionRepository repo) {
    this.repo = repo;
}

public List<HomeSection> getAll() {
    return repo.findAll();
}

public List<HomeSection> getByType(HomeSection.SectionType type) {
    return repo.findByType(type);
}

// New method used by FAQ endpoints
public List<HomeSection> getAllByType(HomeSection.SectionType type) {
    return repo.findByType(type);
}

public Optional<HomeSection> getById(Long id) {
    return repo.findById(id);
}

public HomeSection save(HomeSection section) {
    return repo.save(section);
}

public void delete(Long id) {
    repo.deleteById(id);
}

public List<HomeSection> getActiveByType(HomeSection.SectionType type) {
    return repo.findByTypeAndActiveTrue(type);
}
//HomeSectionService.java
public List<HomeSection> getActiveCommunity() {
    return repo.findByTypeAndActiveTrue(HomeSection.SectionType.COMMUNITY);
}
public List<HomeSection> getLeadership() {
    return repo.findByTypeAndActiveTrue(HomeSection.SectionType.LEADERSHIP);
}
public HomeSection saveBanner(String title, String description, String route, MultipartFile file) throws IOException {
    // Save image to disk
    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
    Path path = Paths.get(uploadDir + fileName);
    Files.createDirectories(path.getParent());
    Files.write(path, file.getBytes());

    // Save banner in DB
    HomeSection banner = new HomeSection();
    banner.setTitle(title);
    banner.setDescription(description);
    banner.setRoute(route);
    banner.setType(HomeSection.SectionType.BANNER); 
    banner.setActive(true);
    banner.setImageUrl("/" + uploadDir + fileName);

    return repo.save(banner);
}

public List<HomeSection> getAllSections() {
    return repo.findAll();
}
//public List<HomeSection> getBanners() {
//    try {
//        List<HomeSection> banners = repo.findByType("BANNER");
//        return banners != null ? banners : new ArrayList<>();
//    } catch (Exception e) {
//        // log the error
//        e.printStackTrace();
//        return new ArrayList<>();
//    }
//}

//HomeSectionService.java

public HomeSection updateHomeSection(Long id, String title, String description, MultipartFile file, String route) throws IOException {
 Optional<HomeSection> optionalSection = repo.findById(id);

 if (!optionalSection.isPresent()) {
     throw new RuntimeException("HomeSection not found with id: " + id);
 }

 HomeSection section = optionalSection.get();

 // Update text fields if provided
 if (title != null) section.setTitle(title);
 if (description != null) section.setDescription(description);
 if (route != null) section.setRoute(route);

 // Handle file upload safely
 if (file != null && !file.isEmpty()) {
     String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

     // Use class-level uploadDir
     File dir = new File(uploadDir);
     if (!dir.exists()) dir.mkdirs();

     // Construct safe file path
     File dest = new File(dir, fileName);
     file.transferTo(dest);

     // Save relative path in DB
     section.setImageUrl("/uploads/" + fileName);
 }

 return repo.save(section);
}
public List<HomeSection> getBanners() {
    try {
        List<HomeSection> banners = repo.findByType(HomeSection.SectionType.BANNER);  // ✅ correct
        return banners != null ? banners : new ArrayList<>();
    } catch (Exception e) {
        e.printStackTrace();
        return new ArrayList<>();
    }
}

}
