package com.example.demo.algorithm;

import com.example.demo.model.Internship;
import com.example.demo.model.StudentProfile;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Core recommendation algorithm implementing the Python ML fairness-based approach
 * This is a Java adaptation of the Algo.py neural network algorithm
 */
@Component
public class InternshipRecommendationAlgorithm {
    
    private final FairnessWeights fairnessWeights;
    
    public InternshipRecommendationAlgorithm() {
        this.fairnessWeights = new FairnessWeights();
    }
    
    public InternshipRecommendationAlgorithm(FairnessWeights fairnessWeights) {
        this.fairnessWeights = fairnessWeights;
    }
    
    /**
     * Main recommendation method - implements the core algorithm from Algo.py
     */
    public List<Internship> generateRecommendations(StudentProfile student, List<Internship> availableInternships) {
        List<ScoredInternship> scoredInternships = new ArrayList<>();
        
        for (Internship internship : availableInternships) {
            // Extract features (equivalent to Python's feature vector)
            FeatureVector features = extractFeatures(student, internship);
            
            // Calculate ML score (simplified neural network approximation)
            double mlScore = calculateMLScore(features);
            
            // Calculate fairness score
            double fairnessScore = calculateFairnessScore(features);
            
            // Blend scores using alpha/beta weights
            double finalScore = blendScores(mlScore, fairnessScore);
            
            scoredInternships.add(new ScoredInternship(internship, finalScore, mlScore, fairnessScore));
        }
        
        // Sort by final score (highest first) and return top recommendations
        return scoredInternships.stream()
                .sorted((a, b) -> Double.compare(b.getFinalScore(), a.getFinalScore()))
                .map(ScoredInternship::getInternship)
                .peek(internship -> {
                    // Set the match score for display
                    ScoredInternship scored = scoredInternships.stream()
                        .filter(si -> si.getInternship().equals(internship))
                        .findFirst()
                        .orElse(null);
                    if (scored != null) {
                        internship.setMatchScore(scored.getFinalScore());
                    }
                })
                .collect(Collectors.toList());
    }
    
    /**
     * Extract features from student-internship pair (equivalent to Python feature extraction)
     */
    private FeatureVector extractFeatures(StudentProfile student, Internship internship) {
        // Calculate skill overlap (0-1 scale)
        double skillOverlap = calculateSkillOverlap(student.getSkills(), internship.getRequiredSkills());
        
        // Location match (0 or 1)
        int locationMatch = student.getLocation() != null && 
                           internship.getLocation() != null && 
                           student.getLocation().equalsIgnoreCase(internship.getLocation()) ? 1 : 0;
        
        // Sector match (0 or 1)
        int sectorMatch = student.getInterestSector() != null && 
                         internship.getSector() != null && 
                         student.getInterestSector().equalsIgnoreCase(internship.getSector()) ? 1 : 0;
        
        // Rural flag
        int ruralFlag = student.getCategoryRural();
        
        // Tribal flag  
        int tribalFlag = student.getCategoryTribal();
        
        // Past participation
        int pastParticipation = student.getPastParticipation() > 0 ? 1 : 0;
        
        return new FeatureVector(skillOverlap, locationMatch, sectorMatch, 
                                ruralFlag, tribalFlag, pastParticipation);
    }
    
    /**
     * Calculate skill overlap between student and internship (0-1 scale)
     */
    private double calculateSkillOverlap(List<String> studentSkills, List<String> requiredSkills) {
        if (studentSkills == null || studentSkills.isEmpty() || 
            requiredSkills == null || requiredSkills.isEmpty()) {
            return 0.0;
        }
        
        Set<String> studentSkillsSet = studentSkills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        
        Set<String> requiredSkillsSet = requiredSkills.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        
        Set<String> intersection = new HashSet<>(studentSkillsSet);
        intersection.retainAll(requiredSkillsSet);
        
        // Jaccard similarity: |intersection| / |union|
        Set<String> union = new HashSet<>(studentSkillsSet);
        union.addAll(requiredSkillsSet);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }
    
    /**
     * Simplified ML score calculation (approximates neural network from Python)
     * This implements the logistic regression equivalent of the MLP
     */
    private double calculateMLScore(FeatureVector features) {
        // Weights learned from the Python model (approximated)
        double[] weights = {2.0, 0.8, 0.9, 0.4, 0.5, -0.2};
        double[] featureArray = features.toArray();
        
        // Calculate linear combination
        double z = 0.0;
        for (int i = 0; i < weights.length && i < featureArray.length; i++) {
            z += weights[i] * featureArray[i];
        }
        
        // Apply sigmoid (logistic function)
        return 1.0 / (1.0 + Math.exp(-z));
    }
    
    /**
     * Calculate fairness score based on rural, tribal, and past participation
     * Implements the fairness_score function from Python
     */
    private double calculateFairnessScore(FeatureVector features) {
        double ruralScore = fairnessWeights.getRuralWeight() * features.getRuralFlag();
        double noPastScore = fairnessWeights.getNoPastParticipationWeight() * (1 - features.getPastParticipation());
        double tribalScore = fairnessWeights.getTribalWeight() * features.getTribalFlag();
        
        return ruralScore + noPastScore + tribalScore;
    }
    
    /**
     * Blend ML score and fairness score using alpha/beta weights
     * Implements the blend_scores function from Python
     */
    private double blendScores(double mlScore, double fairnessScore) {
        return fairnessWeights.getMlScoreWeight() * mlScore + 
               fairnessWeights.getFairnessScoreWeight() * fairnessScore;
    }
    
    /**
     * Inner class to hold scored internship results
     */
    private static class ScoredInternship {
        private final Internship internship;
        private final double finalScore;
        private final double mlScore;
        private final double fairnessScore;
        
        public ScoredInternship(Internship internship, double finalScore, double mlScore, double fairnessScore) {
            this.internship = internship;
            this.finalScore = finalScore;
            this.mlScore = mlScore;
            this.fairnessScore = fairnessScore;
        }
        
        public Internship getInternship() { return internship; }
        public double getFinalScore() { return finalScore; }
        public double getMlScore() { return mlScore; }
        public double getFairnessScore() { return fairnessScore; }
    }
    
    /**
     * Get detailed scoring information for debugging
     */
    public Map<String, Object> getDetailedScoring(StudentProfile student, Internship internship) {
        FeatureVector features = extractFeatures(student, internship);
        double mlScore = calculateMLScore(features);
        double fairnessScore = calculateFairnessScore(features);
        double finalScore = blendScores(mlScore, fairnessScore);
        
        Map<String, Object> details = new HashMap<>();
        details.put("features", features);
        details.put("mlScore", mlScore);
        details.put("fairnessScore", fairnessScore);
        details.put("finalScore", finalScore);
        details.put("skillOverlap", features.getSkillOverlap());
        details.put("locationMatch", features.getLocationMatch() == 1);
        details.put("sectorMatch", features.getSectorMatch() == 1);
        
        return details;
    }
}