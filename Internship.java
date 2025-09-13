package com.example.demo.model;

import java.util.Set;

public class Internship {
    public int id;
    public Set<String> skills;
    public String location;
    public String sector;

    public Internship(int id, Set<String> skills, String location, String sector) {
        this.id = id;
        this.skills = skills;
        this.location = location;
        this.sector = sector;
    }
}
