package com.crudctf.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SOLVES")
public class Solve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long solveId;

    private Long userId;
    private Long challengeId;

    // Automatically records the exact time the flag was captured
    private LocalDateTime solveTime = LocalDateTime.now();

    // Getters and Setters
    public Long getSolveId() { return solveId; }
    public void setSolveId(Long solveId) { this.solveId = solveId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getChallengeId() { return challengeId; }
    public void setChallengeId(Long challengeId) { this.challengeId = challengeId; }

    public LocalDateTime getSolveTime() { return solveTime; }
    public void setSolveTime(LocalDateTime solveTime) { this.solveTime = solveTime; }
}