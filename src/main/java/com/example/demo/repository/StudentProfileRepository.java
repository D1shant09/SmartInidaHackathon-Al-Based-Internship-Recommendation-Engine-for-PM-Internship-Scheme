package com.example.demo.repository;

import com.example.demo.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    
    // Find students by name
    List<StudentProfile> findByNameContainingIgnoreCase(String name);
    
    // Find students by location
    List<StudentProfile> findByLocationIgnoreCase(String location);
    
    // Find students by interest sector
    List<StudentProfile> findByInterestSectorIgnoreCase(String interestSector);
    
    // Custom query to find students with specific skills
    @Query("SELECT sp FROM StudentProfile sp JOIN sp.skills s WHERE s IN :skills")
    List<StudentProfile> findStudentsWithSkills(@Param("skills") List<String> skills);
    
    // Find students by rural category
    List<StudentProfile> findByCategoryRural(int categoryRural);
    
    // Find students by tribal category
    List<StudentProfile> findByCategoryTribal(int categoryTribal);
    
    // Find students with past participation
    List<StudentProfile> findByPastParticipationGreaterThan(int pastParticipation);
}