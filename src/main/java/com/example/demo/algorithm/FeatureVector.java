package com.example.demo.algorithm;

/**
 * Feature vector for student-internship matching
 * Based on the Python algorithm's feature extraction
 */
public class FeatureVector {
    private double skillOverlap;     // 0-1 scale
    private int locationMatch;       // 0 or 1
    private int sectorMatch;         // 0 or 1
    private int ruralFlag;           // 0 or 1
    private int tribalFlag;          // 0 or 1
    private int pastParticipation;   // 0 or 1
    
    public FeatureVector(double skillOverlap, int locationMatch, int sectorMatch,
                        int ruralFlag, int tribalFlag, int pastParticipation) {
        this.skillOverlap = skillOverlap;
        this.locationMatch = locationMatch;
        this.sectorMatch = sectorMatch;
        this.ruralFlag = ruralFlag;
        this.tribalFlag = tribalFlag;
        this.pastParticipation = pastParticipation;
    }
    
    /**
     * Convert to array format for ML processing
     */
    public double[] toArray() {
        return new double[] {
            skillOverlap,
            locationMatch,
            sectorMatch,
            ruralFlag,
            tribalFlag,
            pastParticipation
        };
    }
    
    // Getters and Setters
    public double getSkillOverlap() { return skillOverlap; }
    public void setSkillOverlap(double skillOverlap) { this.skillOverlap = skillOverlap; }
    
    public int getLocationMatch() { return locationMatch; }
    public void setLocationMatch(int locationMatch) { this.locationMatch = locationMatch; }
    
    public int getSectorMatch() { return sectorMatch; }
    public void setSectorMatch(int sectorMatch) { this.sectorMatch = sectorMatch; }
    
    public int getRuralFlag() { return ruralFlag; }
    public void setRuralFlag(int ruralFlag) { this.ruralFlag = ruralFlag; }
    
    public int getTribalFlag() { return tribalFlag; }
    public void setTribalFlag(int tribalFlag) { this.tribalFlag = tribalFlag; }
    
    public int getPastParticipation() { return pastParticipation; }
    public void setPastParticipation(int pastParticipation) { this.pastParticipation = pastParticipation; }
}