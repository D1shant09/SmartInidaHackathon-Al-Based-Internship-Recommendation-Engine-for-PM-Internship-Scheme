package com.example.demo.model;

public class Recommendation {
    public int internshipId;
    public double matchScore;
    public double mlScore;
    public double fairnessScore;

    public Recommendation(int internshipId, double matchScore, double mlScore, double fairnessScore) {
        this.internshipId = internshipId;
        this.matchScore = matchScore;
        this.mlScore = mlScore;
        this.fairnessScore = fairnessScore;
    }
}
