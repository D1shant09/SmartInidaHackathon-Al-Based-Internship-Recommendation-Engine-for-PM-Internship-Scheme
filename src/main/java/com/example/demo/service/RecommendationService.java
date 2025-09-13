package com.example.demo.service;

import com.example.demo.algorithm.InternshipRecommendationAlgorithm;
import com.example.demo.model.Internship;
import com.example.demo.model.Recommendation;
import com.example.demo.model.StudentProfile;
import com.example.demo.repository.InternshipRepository;
import com.example.demo.repository.RecommendationRepository;
import com.example.demo.repository.StudentProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private InternshipRepository internshipRepository;
    
    @Autowired
    private StudentProfileRepository studentProfileRepository;
    
    @Autowired
    private RecommendationRepository recommendationRepository;
    
    @Autowired
    private InternshipRecommendationAlgorithm recommendationAlgorithm;

    // Mock internship database - will be replaced with repository calls
    private List<Internship> internshipDatabase = Arrays.asList(
        createInternship("Software Development Intern", "TechCorp", "Bangalore", "IT", 
                        "Develop web applications using Java and Spring Boot"),
        createInternship("Data Analyst Intern", "DataFirm", "Mumbai", "IT", 
                        "Analyze data using Python and SQL"),
        createInternship("Rural Development Intern", "NGO Connect", "Rural Areas", "Social Work", 
                        "Work on rural development projects"),
        createInternship("Marketing Intern", "BrandCorp", "Delhi", "Marketing", 
                        "Digital marketing and social media campaigns"),
        createInternship("Research Intern", "ResearchLab", "Chennai", "Research", 
                        "Conduct research in technology and innovation")
    );
    
    private Internship createInternship(String title, String company, String location, String sector, String description) {
        Internship internship = new Internship(title, company, location, sector, description);
        return internship;
    }
    
    @Cacheable(value = "recommendations", key = "#profile.name + '_' + #profile.location + '_' + #profile.interestSector")
    public Recommendation generateRecommendations(StudentProfile profile) {
        // Get all active internships from database, fallback to mock data if no data exists
        List<Internship> availableInternships = internshipRepository.findByIsActiveTrue();
        if (availableInternships.isEmpty()) {
            availableInternships = internshipDatabase;
        }
        
        // Use the ML fairness algorithm from Algo.py
        List<Internship> recommendedInternships = recommendationAlgorithm
            .generateRecommendations(profile, availableInternships);
        
        // Filter by threshold (only internships with score > 0.3)
        List<Internship> filteredRecommendations = recommendedInternships.stream()
            .filter(internship -> internship.getMatchScore() > 0.3)
            .collect(Collectors.toList());
        
        String message = generateRecommendationMessage(profile, filteredRecommendations.size());
        Recommendation recommendation = new Recommendation(filteredRecommendations, message);
        
        // Save the recommendation if the student exists in the database
        if (profile.getId() != null) {
            recommendation.setStudent(profile);
            recommendationRepository.save(recommendation);
        }
        
        return recommendation;
    }
    
    private String generateRecommendationMessage(StudentProfile profile, int count) {
        if (count == 0) {
            return "No matching internships found. Consider expanding your skills or interests.";
        } else if (count == 1) {
            return "Found 1 matching internship opportunity for you!";
        } else {
            return "Found " + count + " matching internship opportunities for you!";
        }
    }
    
    @Cacheable(value = "internships", key = "'all_internships'")
    public List<Internship> getAllInternships() {
        List<Internship> dbInternships = internshipRepository.findByIsActiveTrue();
        return dbInternships.isEmpty() ? new ArrayList<>(internshipDatabase) : dbInternships;
    }
    
    public StudentProfile saveStudentProfile(StudentProfile profile) {
        return studentProfileRepository.save(profile);
    }
    
    public List<StudentProfile> findStudentsWithSkills(List<String> skills) {
        return studentProfileRepository.findStudentsWithSkills(skills);
    }
    
    public List<Internship> findInternshipsWithSkills(List<String> skills) {
        List<Internship> dbInternships = internshipRepository.findInternshipsWithSkills(skills);
        if (dbInternships.isEmpty()) {
            // Fallback to filtering mock data
            return internshipDatabase.stream()
                .filter(internship -> skills.stream().anyMatch(skill -> 
                    internship.getTitle().toLowerCase().contains(skill.toLowerCase()) ||
                    internship.getDescription().toLowerCase().contains(skill.toLowerCase())))
                .collect(Collectors.toList());
        }
        return dbInternships;
    }
    
    public List<Recommendation> getRecommendationHistory(StudentProfile student) {
        return recommendationRepository.findByStudentOrderByCreatedAtDesc(student);
    }
    
    public java.util.Map<String, Object> getDetailedScoring(StudentProfile student, Internship internship) {
        return recommendationAlgorithm.getDetailedScoring(student, internship);
    }
    
    @CacheEvict(value = {"recommendations", "internships"}, allEntries = true)
    public void clearCache() {
        // This method will clear all cache entries for recommendations and internships
    }
    
    @CacheEvict(value = "recommendations", key = "#profile.name + '_' + #profile.location + '_' + #profile.interestSector")
    public void clearCacheForProfile(StudentProfile profile) {
        // This method will clear cache entry for specific profile
    }
}
