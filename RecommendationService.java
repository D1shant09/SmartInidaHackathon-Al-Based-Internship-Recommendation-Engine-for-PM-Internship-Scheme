// RecommendationService.java
package com.example.demo.service;

import com.example.demo.model.Internship;
import com.example.demo.model.Recommendation;
import com.example.demo.model.StudentProfile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final List<Internship> internships;

    private final double W_RURAL = 0.15;
    private final double W_NO_PAST = 0.10;
    private final double W_TRIBAL = 0.20;
    private final double ALPHA = 0.7;
    private final double BETA = 0.3;

    public RecommendationService() {
        internships = new ArrayList<>();
        // Example internships
        internships.add(new Internship(1, new HashSet<>(Arrays.asList("python", "ml")), "rural", "IT"));
        internships.add(new Internship(2, new HashSet<>(Arrays.asList("excel", "finance")), "urban", "Finance"));
        internships.add(new Internship(3, new HashSet<>(Arrays.asList("python", "sql")), "urban", "IT"));
        internships.add(new Internship(4, new HashSet<>(Arrays.asList("communication", "marketing")), "rural", "Marketing"));
    }

    // Calculate skill overlap
    private double computeSkillOverlap(List<String> studentSkills, Set<String> requiredSkills) {
        if (requiredSkills.isEmpty()) return 0.0;
        long common = studentSkills.stream()
                .filter(requiredSkills::contains)
                .count();
        return (double) common / requiredSkills.size();
    }

    // Simulate ML model score using same formula as training z (without scaler)
    private double computeMLScore(double skillOverlap, int locationMatch, int sectorMatch,
                                  int ruralFlag, int tribalFlag, int pastPart) {
        double z = 2.0 * skillOverlap + 0.8 * locationMatch + 0.9 * sectorMatch
                   + 0.4 * ruralFlag + 0.5 * tribalFlag - 0.2 * pastPart;
        return 1.0 / (1.0 + Math.exp(-z));  // Sigmoid
    }

    // Fairness score
    private double fairnessScore(int rural, int tribal, int past) {
        int noPast = 1 - past;
        return W_RURAL * rural + W_NO_PAST * noPast + W_TRIBAL * tribal;
    }

    public List<Recommendation> recommend(StudentProfile student) {
        List<Recommendation> results = new ArrayList<>();

        for (Internship internship : internships) {
            double skillOverlap = computeSkillOverlap(student.skills, internship.skills);
            int locationMatch = student.location.equalsIgnoreCase(internship.location) ? 1 : 0;
            int sectorMatch = student.interestSector.equalsIgnoreCase(internship.sector) ? 1 : 0;

            double mlScore = computeMLScore(skillOverlap, locationMatch, sectorMatch,
                    student.categoryRural, student.categoryTribal, student.pastParticipation);
            double fairScore = fairnessScore(student.categoryRural, student.categoryTribal, student.pastParticipation);
            double finalScore = ALPHA * mlScore + BETA * fairScore;

            results.add(new Recommendation(internship.id, finalScore, mlScore, fairScore));
        }

        // Sort by final score descending
        return results.stream()
                .sorted(Comparator.comparingDouble(r -> -r.matchScore))
                .limit(5)
                .collect(Collectors.toList());
    }
}
