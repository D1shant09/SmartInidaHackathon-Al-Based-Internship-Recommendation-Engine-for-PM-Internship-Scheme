package com.example.demo.repository;

import com.example.demo.model.Recommendation;
import com.example.demo.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    
    // Find recommendations by student
    List<Recommendation> findByStudentOrderByCreatedAtDesc(StudentProfile student);
    
    // Find recommendations by student ID
    @Query("SELECT r FROM Recommendation r WHERE r.student.id = :studentId ORDER BY r.createdAt DESC")
    List<Recommendation> findByStudentIdOrderByCreatedAtDesc(@Param("studentId") Long studentId);
    
    // Find recent recommendations (last 30 days)
    @Query("SELECT r FROM Recommendation r WHERE r.createdAt >= :dateFrom ORDER BY r.createdAt DESC")
    List<Recommendation> findRecentRecommendations(@Param("dateFrom") LocalDateTime dateFrom);
    
    // Find recommendations with minimum match count
    List<Recommendation> findByMatchCountGreaterThanEqualOrderByCreatedAtDesc(Integer minMatchCount);
    
    // Find the latest recommendation for a student
    @Query("SELECT r FROM Recommendation r WHERE r.student.id = :studentId ORDER BY r.createdAt DESC LIMIT 1")
    Recommendation findLatestRecommendationForStudent(@Param("studentId") Long studentId);
}