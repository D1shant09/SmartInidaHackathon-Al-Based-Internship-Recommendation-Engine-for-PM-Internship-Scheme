package com.example.demo.config;

import com.example.demo.model.Internship;
import com.example.demo.repository.InternshipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    @Autowired
    private InternshipRepository internshipRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Check if database is empty and initialize with sample data
        if (internshipRepository.count() == 0) {
            logger.info("Database is empty. Initializing with sample internship data...");
            seedInternshipData();
            logger.info("Sample data initialization completed.");
        } else {
            logger.info("Database already contains {} internships. Skipping data initialization.", 
                       internshipRepository.count());
        }
    }
    
    private void seedInternshipData() {
        try {
            // Software Development Internship
            Internship softwareDev = new Internship();
            softwareDev.setTitle("Software Development Intern");
            softwareDev.setCompany("TechCorp");
            softwareDev.setLocation("Bangalore");
            softwareDev.setSector("IT");
            softwareDev.setDescription("Develop web applications using Java and Spring Boot. Work with modern frameworks and contribute to real-world projects.");
            softwareDev.setRequiredSkills(Arrays.asList("Java", "Spring Boot", "MySQL", "React"));
            softwareDev.setDurationMonths(6);
            softwareDev.setStipend(25000.0);
            softwareDev.setApplicationDeadline(LocalDateTime.now().plusMonths(2));
            
            // Data Analysis Internship
            Internship dataAnalysis = new Internship();
            dataAnalysis.setTitle("Data Analyst Intern");
            dataAnalysis.setCompany("DataFirm");
            dataAnalysis.setLocation("Mumbai");
            dataAnalysis.setSector("IT");
            dataAnalysis.setDescription("Analyze data using Python and SQL. Create insights and visualizations for business decision making.");
            dataAnalysis.setRequiredSkills(Arrays.asList("Python", "SQL", "Data Analysis", "Visualization"));
            dataAnalysis.setDurationMonths(4);
            dataAnalysis.setStipend(20000.0);
            dataAnalysis.setApplicationDeadline(LocalDateTime.now().plusMonths(1));
            
            // Rural Development Internship
            Internship ruralDev = new Internship();
            ruralDev.setTitle("Rural Development Intern");
            ruralDev.setCompany("NGO Connect");
            ruralDev.setLocation("Rural Areas");
            ruralDev.setSector("Social Work");
            ruralDev.setDescription("Work on rural development projects, community outreach, and social impact initiatives.");
            ruralDev.setRequiredSkills(Arrays.asList("Community Outreach", "Project Management", "Communication"));
            ruralDev.setDurationMonths(3);
            ruralDev.setStipend(15000.0);
            ruralDev.setApplicationDeadline(LocalDateTime.now().plusMonths(3));
            
            // Marketing Internship
            Internship marketing = new Internship();
            marketing.setTitle("Marketing Intern");
            marketing.setCompany("BrandCorp");
            marketing.setLocation("Delhi");
            marketing.setSector("Marketing");
            marketing.setDescription("Digital marketing and social media campaigns. Experience with modern marketing tools and strategies.");
            marketing.setRequiredSkills(Arrays.asList("Digital Marketing", "Social Media", "Content Creation", "Analytics"));
            marketing.setDurationMonths(4);
            marketing.setStipend(18000.0);
            marketing.setApplicationDeadline(LocalDateTime.now().plusMonths(1));
            
            // Research Internship
            Internship research = new Internship();
            research.setTitle("Research Intern");
            research.setCompany("ResearchLab");
            research.setLocation("Chennai");
            research.setSector("Research");
            research.setDescription("Conduct research in technology and innovation. Publish findings and contribute to academic publications.");
            research.setRequiredSkills(Arrays.asList("Research", "Technical Writing", "Data Collection", "Analysis"));
            research.setDurationMonths(5);
            research.setStipend(22000.0);
            research.setApplicationDeadline(LocalDateTime.now().plusMonths(2));
            
            // AI/ML Internship
            Internship aiml = new Internship();
            aiml.setTitle("AI/ML Research Intern");
            aiml.setCompany("AI Solutions");
            aiml.setLocation("Hyderabad");
            aiml.setSector("Artificial Intelligence");
            aiml.setDescription("Work on machine learning models, neural networks, and AI applications. Hands-on experience with latest AI technologies.");
            aiml.setRequiredSkills(Arrays.asList("Python", "Machine Learning", "TensorFlow", "Data Science"));
            aiml.setDurationMonths(6);
            aiml.setStipend(30000.0);
            aiml.setApplicationDeadline(LocalDateTime.now().plusMonths(2));
            
            // Save all internships to database
            internshipRepository.saveAll(Arrays.asList(
                softwareDev, dataAnalysis, ruralDev, marketing, research, aiml
            ));
            
        } catch (Exception e) {
            logger.error("Error occurred during data initialization: {}", e.getMessage(), e);
        }
    }
}