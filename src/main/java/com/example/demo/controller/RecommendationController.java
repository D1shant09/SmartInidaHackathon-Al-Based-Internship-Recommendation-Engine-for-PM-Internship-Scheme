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
    
    @GetMapping("/health")
    public String healthCheck() {
        return "Internship Recommendation API is running with MySQL and Redis!";
    }
}
