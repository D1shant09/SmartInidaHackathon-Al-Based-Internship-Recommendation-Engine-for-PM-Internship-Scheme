package com.example.demo.algorithm;

/**
 * Fairness weights configuration for the recommendation algorithm
 */
public class FairnessWeights {
    private double ruralWeight = 0.15;
    private double noPastParticipationWeight = 0.10;
    private double tribalWeight = 0.20;
    private double mlScoreWeight = 0.70; // alpha
    private double fairnessScoreWeight = 0.30; // beta
    
    public FairnessWeights() {}
    
    public FairnessWeights(double ruralWeight, double noPastParticipationWeight, 
                          double tribalWeight, double mlScoreWeight, double fairnessScoreWeight) {
        this.ruralWeight = ruralWeight;
        this.noPastParticipationWeight = noPastParticipationWeight;
        this.tribalWeight = tribalWeight;
        this.mlScoreWeight = mlScoreWeight;
        this.fairnessScoreWeight = fairnessScoreWeight;
    }
    
    // Getters and Setters
    public double getRuralWeight() { return ruralWeight; }
    public void setRuralWeight(double ruralWeight) { this.ruralWeight = ruralWeight; }
    
    public double getNoPastParticipationWeight() { return noPastParticipationWeight; }
    public void setNoPastParticipationWeight(double noPastParticipationWeight) { 
        this.noPastParticipationWeight = noPastParticipationWeight; 
    }
    
    public double getTribalWeight() { return tribalWeight; }
    public void setTribalWeight(double tribalWeight) { this.tribalWeight = tribalWeight; }
    
    public double getMlScoreWeight() { return mlScoreWeight; }
    public void setMlScoreWeight(double mlScoreWeight) { this.mlScoreWeight = mlScoreWeight; }
    
    public double getFairnessScoreWeight() { return fairnessScoreWeight; }
    public void setFairnessScoreWeight(double fairnessScoreWeight) { 
        this.fairnessScoreWeight = fairnessScoreWeight; 
    }
}