package com.crudctf.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AI_REQUESTS")
public class AiRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String teamId;

    @Column(nullable = false)
    private String challengeId;

    @Lob // Uses CLOB in Oracle for massive text chunks
    @Column(nullable = false)
    private String promptContext;

    @Lob
    @Column(nullable = false)
    private String aiResponse;

    private LocalDateTime requestedAt = LocalDateTime.now();

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTeamId() { return teamId; }
    public void setTeamId(String teamId) { this.teamId = teamId; }

    public String getChallengeId() { return challengeId; }
    public void setChallengeId(String challengeId) { this.challengeId = challengeId; }

    public String getPromptContext() { return promptContext; }
    public void setPromptContext(String promptContext) { this.promptContext = promptContext; }

    public String getAiResponse() { return aiResponse; }
    public void setAiResponse(String aiResponse) { this.aiResponse = aiResponse; }

    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
}