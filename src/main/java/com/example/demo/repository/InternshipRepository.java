package com.example.demo.repository;

import com.example.demo.model.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {
    
    // Find active internships
    List<Internship> findByIsActiveTrue();
    
    // Find internships by company
    List<Internship> findByCompanyContainingIgnoreCase(String company);
    
    // Find internships by location
    List<Internship> findByLocationIgnoreCase(String location);
    
    // Find internships by sector
    List<Internship> findBySectorIgnoreCase(String sector);
    
    // Find internships by title
    List<Internship> findByTitleContainingIgnoreCase(String title);
    
    // Custom query to find internships with specific required skills
    @Query("SELECT i FROM Internship i JOIN i.requiredSkills s WHERE s IN :skills AND i.isActive = true")
    List<Internship> findInternshipsWithSkills(@Param("skills") List<String> skills);
    
    // Find internships by location and sector
    List<Internship> findByLocationIgnoreCaseAndSectorIgnoreCaseAndIsActiveTrue(String location, String sector);
    
    // Find internships with upcoming deadlines
    @Query("SELECT i FROM Internship i WHERE i.applicationDeadline > :now AND i.isActive = true ORDER BY i.applicationDeadline ASC")
    List<Internship> findInternshipsWithUpcomingDeadlines(@Param("now") LocalDateTime now);
    
    // Find internships by stipend range
    @Query("SELECT i FROM Internship i WHERE i.stipend BETWEEN :minStipend AND :maxStipend AND i.isActive = true")
    List<Internship> findInternshipsByStipendRange(@Param("minStipend") Double minStipend, @Param("maxStipend") Double maxStipend);
    
    // Find internships by duration
    List<Internship> findByDurationMonthsAndIsActiveTrue(Integer durationMonths);
}