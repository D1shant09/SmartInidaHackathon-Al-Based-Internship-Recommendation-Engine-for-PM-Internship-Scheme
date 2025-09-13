package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "internships")
public class Internship implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;
    
    @NotBlank(message = "Company is required")
    @Column(name = "company", nullable = false)
    private String company;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "sector")
    private String sector;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "internship_skills", joinColumns = @JoinColumn(name = "internship_id"))
    @Column(name = "skill")
    private List<String> requiredSkills = new ArrayList<>();
    
    @Column(name = "duration_months")
    private Integer durationMonths;
    
    @Column(name = "stipend")
    private Double stipend;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @Column(name = "application_deadline")
    private LocalDateTime applicationDeadline;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Transient
    private double matchScore; // Calculated field, not persisted
    
    // Default constructor
    public Internship() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isActive = true;
    }
    
    // Constructor with basic fields
    public Internship(String title, String company, String location, String sector, String description) {
        this();
        this.title = title;
        this.company = company;
        this.location = location;
        this.sector = sector;
        this.description = description;
    }
    
    // Constructor with all fields
    public Internship(String title, String company, String location, String sector, 
                     String description, List<String> requiredSkills, Integer durationMonths, Double stipend) {
        this(title, company, location, sector, description);
        this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>();
        this.durationMonths = durationMonths;
        this.stipend = stipend;
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public List<String> getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(List<String> requiredSkills) { this.requiredSkills = requiredSkills != null ? requiredSkills : new ArrayList<>(); }
    
    public Integer getDurationMonths() { return durationMonths; }
    public void setDurationMonths(Integer durationMonths) { this.durationMonths = durationMonths; }
    
    public Double getStipend() { return stipend; }
    public void setStipend(Double stipend) { this.stipend = stipend; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getApplicationDeadline() { return applicationDeadline; }
    public void setApplicationDeadline(LocalDateTime applicationDeadline) { this.applicationDeadline = applicationDeadline; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public double getMatchScore() { return matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }
}
