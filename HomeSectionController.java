
package com.example.yadavsajam.controller;

import com.example.yadavsajam.model.HomeSection;
import com.example.yadavsajam.repository.HomeSectionRepository;
import com.example.yadavsajam.service.HomeSectionService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/home-sections")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class HomeSectionController {

    private final HomeSectionService service;

    private static final String UPLOAD_DIR = "E:/Akanksha/Yadav_samaj/yadavsajam/uploads/home-sections/";
    @Autowired
    private HomeSectionRepository homeSectionRepository;

    // -------------------- GET --------------------

    @GetMapping
    public ResponseEntity<List<HomeSection>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<?> getByType(@PathVariable String type) {
        try {
            HomeSection.SectionType sectionType = HomeSection.SectionType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(service.getByType(sectionType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid section type: " + type));
        }
    }

    @GetMapping("/active/type/{type}")
    public ResponseEntity<List<HomeSection>> getActiveByType(@PathVariable String type) {
        try {
            HomeSection.SectionType sectionType = HomeSection.SectionType.valueOf(type.toUpperCase());
            return ResponseEntity.ok(service.getActiveByType(sectionType));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }

    // -------------------- ADD --------------------

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addSection(
            @RequestPart("title") String title,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "route", required = false) String route,
            @RequestPart("type") String type,
            @RequestPart(value = "image", required = false) MultipartFile[] images // ✅ array
    ) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            HomeSection.SectionType sectionType;
            try {
                sectionType = HomeSection.SectionType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid section type: " + type));
            }

            List<String> imageUrls = new ArrayList<>();
            if (images != null) {
                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                        File dest = new File(uploadDir, fileName);
                        image.transferTo(dest);
                        imageUrls.add("/uploads/home-sections/" + fileName);
                    }
                }
            }

            HomeSection section = new HomeSection();
            section.setTitle(title);
            section.setDescription(description);
            section.setRoute(route);
            section.setType(sectionType);
            section.setImageUrl(imageUrls.isEmpty() ? null : imageUrls.get(0)); // store first for compatibility
            section.setActive(true);

            HomeSection saved = service.save(section);
            return ResponseEntity.ok(Map.of("success", true, "data", saved));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Upload failed", "error", e.getMessage()));
        }
    }
    @PostMapping(value = "/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPhotos(@RequestPart("files") List<MultipartFile> files) {
        List<HomeSection> savedSections = new ArrayList<>();
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            for (MultipartFile image : files) {
                if (image != null && !image.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                    File dest = new File(uploadDir, fileName);
                    image.transferTo(dest);

                    HomeSection section = new HomeSection();
                    section.setType(HomeSection.SectionType.MEMBERSHIP_PHOTO);
                    section.setImageUrl("/uploads/home-sections/" + fileName);
                    section.setActive(true);

                    savedSections.add(service.save(section));
                }
            }
            return ResponseEntity.ok(Map.of("success", true, "data", savedSections));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Upload failed", "error", e.getMessage()));
        }
    }
    

    // -------------------- UPDATE --------------------
    @PutMapping("/{id}")
    public HomeSection updateHomeSection(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String route,
            @RequestParam(required = false) MultipartFile file) throws IOException {
        return service.updateHomeSection(id, title, description, file, route);
    }
    // -------------------- DELETE --------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSection(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Section deleted successfully"));
    }

    // -------------------- HELPER --------------------

    private ResponseEntity<?> handleSectionAdd(String title, String description, String route, String type, MultipartFile image) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            HomeSection.SectionType sectionType;
            try {
                sectionType = HomeSection.SectionType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException ex) {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid section type: " + type));
            }

            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                File dest = new File(uploadDir, fileName);
                image.transferTo(dest);
                imageUrl = "/uploads/home-sections/" + fileName;
            }

            HomeSection section = new HomeSection();
            section.setTitle(title);
            section.setDescription(description);
            section.setRoute(route);
            section.setType(sectionType);
            section.setImageUrl(imageUrl);
            section.setActive(true);

            HomeSection saved = service.save(section);
            return ResponseEntity.ok(Map.of("success", true, "data", saved));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Upload failed", "error", e.getMessage()));
        }
    }
 // ------------------ FAQ Endpoints ------------------
  //✅ FAQ endpoints
  @GetMapping("/faqs")
  public ResponseEntity<List<HomeSection>> getAllFaqs() {
   return ResponseEntity.ok(service.getByType(HomeSection.SectionType.FAQ));
  }

  @PostMapping("/faqs")
  public ResponseEntity<?> addFaq(@RequestBody HomeSection faq) {
   faq.setType(HomeSection.SectionType.FAQ);
   faq.setActive(true);
   HomeSection saved = service.save(faq);
   return ResponseEntity.ok(saved);
  }

  @PutMapping("/faqs/{id}")
  public ResponseEntity<?> updateFaq(@PathVariable Long id, @RequestBody HomeSection faq) {
   Optional<HomeSection> existingOpt = service.getById(id);
   if (existingOpt.isEmpty())
       return ResponseEntity.status(HttpStatus.NOT_FOUND).body("FAQ not found");

   HomeSection existing = existingOpt.get();
   existing.setTitle(faq.getTitle());
   existing.setDescription(faq.getDescription());
   existing.setType(HomeSection.SectionType.FAQ); // ensure type stays FAQ
   service.save(existing);
   return ResponseEntity.ok(existing);
  }

  @DeleteMapping("/faqs/{id}")
  public ResponseEntity<?> deleteFaq(@PathVariable Long id) {
   service.delete(id);
   return ResponseEntity.ok(Map.of("message", "FAQ deleted successfully"));
  }

  // ------------------ General Sections ------------------



  @PostMapping("/leadership")
  public ResponseEntity<HomeSection> addLeadership(
          @RequestParam("title") String title,
          @RequestParam("description") String description,
          @RequestParam(value = "image", required = false) MultipartFile image) {

      try {
          HomeSection section = new HomeSection();
          section.setType(HomeSection.SectionType.LEADERSHIP);
          section.setTitle(title);
          section.setDescription(description);

          if (image != null && !image.isEmpty()) {
              String uploadDir = "uploads/home-sections/";
              String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
              Path path = Paths.get(uploadDir, fileName);
              Files.createDirectories(path.getParent());
              Files.write(path, image.getBytes());

              // store relative URL path (so it can be served later)
              section.setImageUrl("/uploads/home-sections/" + fileName);
          }

          return ResponseEntity.ok(homeSectionRepository.save(section));
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.internalServerError().build();
      }
  }


  @GetMapping("/leadership")
  public ResponseEntity<List<HomeSection>> getLeadership() {
      List<HomeSection> leadershipSections = homeSectionRepository.findByType(HomeSection.SectionType.LEADERSHIP);
      return ResponseEntity.ok(leadershipSections);
  }



  //Update Leadership
  @PutMapping(value = "/leadership/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateLeadership(
       @PathVariable Long id,
       @RequestPart("title") String title,
       @RequestPart(value = "description", required = false) String description,
       @RequestPart(value = "route", required = false) String route,
       @RequestPart(value = "image", required = false) MultipartFile image
  ) {
   Optional<HomeSection> existingOpt = service.getById(id);
   if (existingOpt.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND)
           .body(Map.of("message", "Leadership not found"));

   try {
       HomeSection section = existingOpt.get();
       section.setTitle(title);
       section.setDescription(description);
       section.setRoute(route);

       if (image != null && !image.isEmpty()) {
           File uploadDir = new File(UPLOAD_DIR);
           if (!uploadDir.exists()) uploadDir.mkdirs();

           String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
           File dest = new File(uploadDir, fileName);
           image.transferTo(dest);
           section.setImageUrl("/uploads/home-sections/" + fileName);
       }

       HomeSection saved = service.save(section);
       return ResponseEntity.ok(Map.of("success", true, "data", saved));
   } catch (Exception e) {
       e.printStackTrace();
       return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body(Map.of("message", "Update failed", "error", e.getMessage()));
   }
  }

  //Delete Leadership
  @DeleteMapping("/leadership/{id}")
  public ResponseEntity<?> deleteLeadership(@PathVariable Long id) {
   service.delete(id);
   return ResponseEntity.ok(Map.of("message", "Leadership deleted successfully"));
  }

  
  @PostMapping("/banner")
  public ResponseEntity<?> addBanner(
          @RequestParam String title,
          @RequestParam String description,
          @RequestParam(required = false) String route,
          @RequestParam MultipartFile file
  ) throws IOException {

      String uploadDir = "uploads/home-sections/";
      String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      Path path = Paths.get(uploadDir, fileName);
      Files.createDirectories(path.getParent());
      Files.write(path, file.getBytes());

      HomeSection banner = new HomeSection();
      banner.setTitle(title);
      banner.setDescription(description);
      banner.setRoute(route);
      banner.setActive(true);
      banner.setImageUrl("/" + uploadDir + fileName);
      banner.setType(HomeSection.SectionType.BANNER); // Important

      return ResponseEntity.ok(homeSectionRepository.save(banner));
  }

  @GetMapping("/banner")
  public List<HomeSection> getBanners() {
      return service.getBanners();
  }

  
}