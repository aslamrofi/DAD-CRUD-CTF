package com.crudctf.repository;

import com.crudctf.model.AiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiRequestRepository extends JpaRepository<AiRequest, Long> {
    // Custom method if you ever want to pull all AI requests made by a specific team
    List<AiRequest> findByTeamId(String teamId);
}