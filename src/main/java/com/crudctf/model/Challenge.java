package com.crudctf.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CHALLENGES")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category; // e.g., Pwn, Reverse Engineering, Forensics
    private String description;
    private String flag;
    private int points;

    // TODO for Wan Afiq: Add standard Getters and Setters below so Spring can access these fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFlag() { return flag; }
    public void setFlag(String flag) { this.flag = flag; }
    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}