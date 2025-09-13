package com.example.demo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "recommendations")
public class Recommendation implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentProfile student;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "recommendation_internships",
        joinColumns = @JoinColumn(name = "recommendation_id"),
        inverseJoinColumns = @JoinColumn(name = "internship_id")
    )
    private List<Internship> internships = new ArrayList<>();
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "match_count")
    private Integer matchCount;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Default constructor
    public Recommendation() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Constructor with basic fields
    public Recommendation(StudentProfile student, List<Internship> internships, String message) {
        this();
        this.student = student;
        this.internships = internships != null ? internships : new ArrayList<>();
        this.message = message;
        this.matchCount = this.internships.size();
    }
    
    // Constructor for response (without student)
    public Recommendation(List<Internship> internships, String message) {
        this();
        this.internships = internships != null ? internships : new ArrayList<>();
        this.message = message;
        this.matchCount = this.internships.size();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public StudentProfile getStudent() { return student; }
    public void setStudent(StudentProfile student) { this.student = student; }
    
    public List<Internship> getInternships() { return internships; }
    public void setInternships(List<Internship> internships) { 
        this.internships = internships != null ? internships : new ArrayList<>(); 
        this.matchCount = this.internships.size();
    }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public Integer getMatchCount() { return matchCount; }
    public void setMatchCount(Integer matchCount) { this.matchCount = matchCount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
