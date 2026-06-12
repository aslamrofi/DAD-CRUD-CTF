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

    // ... your existing fields like title, description, points ...

    private String fileName;
    private String fileType;

    @Lob
    private byte[] fileData;

    // --- THESE EXACT GETTERS AND SETTERS MUST BE AT THE BOTTOM ---

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}