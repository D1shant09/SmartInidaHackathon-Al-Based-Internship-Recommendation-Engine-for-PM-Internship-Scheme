// RecommendationController.java
package com.example.demo.controller;

import com.example.demo.model.Recommendation;
import com.example.demo.model.StudentProfile;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/recommend")
    public List<Recommendation> recommend(@RequestBody StudentProfile studentProfile) {
        return recommendationService.recommend(studentProfile);
    }
}
