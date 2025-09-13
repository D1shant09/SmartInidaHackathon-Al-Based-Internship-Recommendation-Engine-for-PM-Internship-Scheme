package com.example.demo.service;

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
        List<Internship> recommendedInternships = new ArrayList<>();
        
        // Get all active internships from database, fallback to mock data if no data exists
        List<Internship> availableInternships = internshipRepository.findByIsActiveTrue();
        if (availableInternships.isEmpty()) {
            availableInternships = internshipDatabase;
        }
        
        for (Internship internship : availableInternships) {
            double score = calculateMatchScore(profile, internship);
            if (score > 0.3) { // Threshold for recommendation
                internship.setMatchScore(score);
                recommendedInternships.add(internship);
            }
        }
        
        // Sort by match score (highest first)
        recommendedInternships.sort((i1, i2) -> Double.compare(i2.getMatchScore(), i1.getMatchScore()));
        
        String message = generateRecommendationMessage(profile, recommendedInternships.size());
        
        Recommendation recommendation = new Recommendation(recommendedInternships, message);
        
        // Save the recommendation if the student exists in the database
        if (profile.getId() != null) {
            recommendation.setStudent(profile);
            recommendationRepository.save(recommendation);
        }
        
        return recommendation;
    }
    
    private double calculateMatchScore(StudentProfile profile, Internship internship) {
        double score = 0.0;
        
        // Skill matching
        if (profile.getSkills() != null) {
            for (String skill : profile.getSkills()) {
                if (internship.getDescription().toLowerCase().contains(skill.toLowerCase())) {
                    score += 0.4;
                }
            }
        }
        
        // Sector matching
        if (profile.getInterestSector() != null && 
            internship.getSector().equalsIgnoreCase(profile.getInterestSector())) {
            score += 0.3;
        }
        
        // Location preference
        if (profile.getLocation() != null) {
            if (profile.getLocation().equalsIgnoreCase("rural") && 
                internship.getLocation().toLowerCase().contains("rural")) {
                score += 0.2;
            } else if (!profile.getLocation().equalsIgnoreCase("rural") && 
                       !internship.getLocation().toLowerCase().contains("rural")) {
                score += 0.1;
            }
        }
        
        // Rural category bonus
        if (profile.getCategoryRural() == 1 && 
            internship.getLocation().toLowerCase().contains("rural")) {
            score += 0.2;
        }
        
        // Past participation bonus
        if (profile.getPastParticipation() > 0) {
            score += 0.1;
        }
        
        return Math.min(score, 1.0); // Cap at 1.0
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
    
    @CacheEvict(value = {"recommendations", "internships"}, allEntries = true)
    public void clearCache() {
        // This method will clear all cache entries for recommendations and internships
    }
    
    @CacheEvict(value = "recommendations", key = "#profile.name + '_' + #profile.location + '_' + #profile.interestSector")
    public void clearCacheForProfile(StudentProfile profile) {
        // This method will clear cache entry for specific profile
    }
}
