package com.crudctf.repository;

import com.crudctf.model.Solve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolveRepository extends JpaRepository<Solve, Long> {
    // Checks if a team already captured a specific flag
    boolean existsByUserIdAndChallengeId(Long userId, Long challengeId);

    // Gets all the solves for a specific team
    List<Solve> findByUserId(Long userId);
}