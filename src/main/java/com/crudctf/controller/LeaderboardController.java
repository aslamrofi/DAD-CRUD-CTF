package com.crudctf.controller;

import com.crudctf.model.Challenge;
import com.crudctf.model.Solve;
import com.crudctf.model.User;
import com.crudctf.repository.ChallengeRepository;
import com.crudctf.repository.SolveRepository;
import com.crudctf.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Controller
public class LeaderboardController {

    @Autowired private UserRepository userRepository;
    @Autowired private SolveRepository solveRepository;
    @Autowired private ChallengeRepository challengeRepository;

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model) {
        List<User> allPlayers = userRepository.findAll();

        // This map will hold Team Name -> Total Score
        Map<String, Integer> teamScores = new HashMap<>();

        for (User user : allPlayers) {
            if ("PLAYER".equals(user.getRole())) {
                int totalScore = 0;
                List<Solve> userSolves = solveRepository.findByUserId(user.getUserId());

                // Add up the points for every challenge they solved
                for (Solve solve : userSolves) {
                    Challenge solvedChallenge = challengeRepository.findById(solve.getChallengeId()).orElse(null);
                    if (solvedChallenge != null) {
                        totalScore += solvedChallenge.getPoints();
                    }
                }
                teamScores.put(user.getUsername(), totalScore);
            }
        }

        // Sort the scoreboard from highest to lowest points
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(teamScores.entrySet());
        sortedScores.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        model.addAttribute("scoreboard", sortedScores);
        return "leaderboard";
    }
}