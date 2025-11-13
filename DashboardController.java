package com.example.yadavsajam.controller;

import com.example.yadavsajam.repository.MatrimonialUserRepository;
import com.example.yadavsajam.repository.MembershipUserRepository;
import com.example.yadavsajam.service.PaymentService;
import com.example.yadavsajam.service.HappyStoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final MatrimonialUserRepository matrimonialRepo;
    private final MembershipUserRepository membershipRepo;
    private final PaymentService paymentService;
    private final HappyStoryService happyStoryService;

    // ✅ Existing dashboard stats (unchanged)
    @GetMapping("/dashboard")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalMatrimonialUsers = matrimonialRepo.count();
        long approvedMatrimonial = matrimonialRepo.findAll().stream()
                .filter(u -> Boolean.TRUE.equals(u.getApproved()))
                .count();
        long pendingMatrimonial = matrimonialRepo.findAll().stream()
                .filter(u -> !Boolean.TRUE.equals(u.getApproved()))
                .count();


        double totalPayments = paymentService.getTotalPayments();
        long happyStoriesCount = happyStoryService.getAllStories().size();

        stats.put("totalMatrimonialUsers", totalMatrimonialUsers);
        stats.put("approvedMatrimonial", approvedMatrimonial);
        stats.put("pendingMatrimonial", pendingMatrimonial);


        stats.put("totalPayments", totalPayments);
        stats.put("happyStoriesCount", happyStoriesCount);

        return stats;
    }

    // ✅ Existing pending users method (unchanged)
    @GetMapping("/admin/pending-users")
    public List<Map<String, Object>> getPendingUsers() {
        List<Map<String, Object>> pendingUsers = new ArrayList<>();

        matrimonialRepo.findAll().stream()
                .filter(u -> !Boolean.TRUE.equals(u.getApproved()))
                .forEach(u -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", u.getId());
                    map.put("name", u.getFullName());
                    map.put("email", u.getEmail());
                    map.put("type", "Matrimonial");
                    pendingUsers.add(map);
                });


        return pendingUsers;
    }

    // ✅ NEW: Earnings Data for Dashboard Chart (Week / Month / Year)
    @GetMapping("/dashboard/earnings")
    public Map<String, Object> getEarnings(@RequestParam(defaultValue = "year") String type) {
        Map<String, Object> response = new HashMap<>();
        List<Integer> earnings = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        switch (type.toLowerCase()) {
            case "week":
                labels = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
                earnings = Arrays.asList(50, 80, 60, 120, 90, 150, 100);
                break;
            case "month":
                labels = Arrays.asList("Week 1", "Week 2", "Week 3", "Week 4");
                earnings = Arrays.asList(300, 450, 500, 600);
                break;
            default: // year
                labels = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
                earnings = Arrays.asList(100, 200, 150, 300, 500, 700, 1000, 600, 400, 300, 200, 100);
        }

        response.put("type", type);
        response.put("labels", labels);
        response.put("earnings", earnings);
        response.put("totalEarnings", earnings.stream().mapToInt(Integer::intValue).sum());

        return response;
    }
}
