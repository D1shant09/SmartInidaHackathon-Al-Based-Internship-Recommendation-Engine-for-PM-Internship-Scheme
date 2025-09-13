package com.example.demo.controller;

import com.example.demo.model.Internship;
import com.example.demo.model.Recommendation;
import com.example.demo.model.StudentProfile;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class RecommendationController {
    
    @Autowired
    private RecommendationService recommendationService;
    
    @PostMapping("/recommend")
    public Recommendation getRecommendations(@RequestBody StudentProfile profile) {
        return recommendationService.generateRecommendations(profile);
    }
    
    @PostMapping("/students")
    public ResponseEntity<StudentProfile> saveStudentProfile(@RequestBody StudentProfile profile) {
        StudentProfile savedProfile = recommendationService.saveStudentProfile(profile);
        return ResponseEntity.ok(savedProfile);
    }
    
    @GetMapping("/internships")
    public ResponseEntity<List<Internship>> getAllInternships() {
        List<Internship> internships = recommendationService.getAllInternships();
        return ResponseEntity.ok(internships);
    }
    
    @PostMapping("/internships/search")
    public ResponseEntity<List<Internship>> searchInternshipsBySkills(@RequestBody Map<String, List<String>> request) {
        List<String> skills = request.get("skills");
        List<Internship> internships = recommendationService.findInternshipsWithSkills(skills);
        return ResponseEntity.ok(internships);
    }
    
    @PostMapping("/cache/clear")
    public ResponseEntity<String> clearCache() {
        recommendationService.clearCache();
        return ResponseEntity.ok("Cache cleared successfully");
    }
    
    @PostMapping("/algorithm/score")
    public ResponseEntity<Map<String, Object>> getDetailedScoring(
            @RequestBody Map<String, Object> request) {
        // Extract student profile and internship from request
        StudentProfile student = extractStudentProfile(request);
        // For simplicity, we'll get the first internship - in real scenario, you'd pass internship ID
        List<Internship> internships = recommendationService.getAllInternships();
        if (!internships.isEmpty()) {
            Map<String, Object> scoring = recommendationService.getDetailedScoring(student, internships.get(0));
            return ResponseEntity.ok(scoring);
        }
        return ResponseEntity.badRequest().body(Map.of("error", "No internships available"));
    }
    
    private StudentProfile extractStudentProfile(Map<String, Object> request) {
        StudentProfile profile = new StudentProfile();
        profile.setName((String) request.get("name"));
        profile.setLocation((String) request.get("location"));
        profile.setInterestSector((String) request.get("interestSector"));
        profile.setCategoryRural((Integer) request.getOrDefault("categoryRural", 0));
        profile.setCategoryTribal((Integer) request.getOrDefault("categoryTribal", 0));
        profile.setPastParticipation((Integer) request.getOrDefault("pastParticipation", 0));
        
        @SuppressWarnings("unchecked")
        List<String> skills = (List<String>) request.getOrDefault("skills", java.util.Collections.emptyList());
        profile.setSkills(skills);
        
        return profile;
    }
    
    @GetMapping("/health")
    public String healthCheck() {
        return "Internship Recommendation API is running with MySQL, Redis, and ML Fairness Algorithm!";
    }
}
