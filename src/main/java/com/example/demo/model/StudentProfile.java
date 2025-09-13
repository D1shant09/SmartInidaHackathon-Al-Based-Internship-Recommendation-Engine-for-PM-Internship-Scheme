package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student_profiles")
public class StudentProfile implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Column(name = "name", nullable = false)
    private String name;
    
    @ElementCollection
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "interest_sector")
    private String interestSector;
    
    @Column(name = "category_rural")
    private int categoryRural;
    
    @Column(name = "category_tribal")
    private int categoryTribal;
    
    @Column(name = "past_participation")
    private int pastParticipation;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Default constructor
    public StudentProfile() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public StudentProfile(String name, List<String> skills, String location, String interestSector, 
                         int categoryRural, int categoryTribal, int pastParticipation) {
        this();
        this.name = name;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.location = location;
        this.interestSector = interestSector;
        this.categoryRural = categoryRural;
        this.categoryTribal = categoryTribal;
        this.pastParticipation = pastParticipation;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills != null ? skills : new ArrayList<>(); }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getInterestSector() { return interestSector; }
    public void setInterestSector(String interestSector) { this.interestSector = interestSector; }
    
    public int getCategoryRural() { return categoryRural; }
    public void setCategoryRural(int categoryRural) { this.categoryRural = categoryRural; }
    
    public int getCategoryTribal() { return categoryTribal; }
    public void setCategoryTribal(int categoryTribal) { this.categoryTribal = categoryTribal; }
    
    public int getPastParticipation() { return pastParticipation; }
    public void setPastParticipation(int pastParticipation) { this.pastParticipation = pastParticipation; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
