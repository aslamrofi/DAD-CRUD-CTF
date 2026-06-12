package com.crudctf.controller;

import com.crudctf.model.Challenge;
import com.crudctf.model.Solve;
import com.crudctf.model.User;
import com.crudctf.repository.ChallengeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.crudctf.repository.SolveRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private SolveRepository solveRepository;

    // READ: Show the dashboard and active challenges
    @GetMapping("/dashboard")
    public String playerDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        model.addAttribute("teamName", loggedInUser.getUsername());
        model.addAttribute("challenges", challengeRepository.findAll());

        // NEW LOGIC: Get a list of just the IDs for the challenges this team has solved
        List<Solve> userSolves = solveRepository.findByUserId(loggedInUser.getUserId());
        List<Long> solvedChallengeIds = userSolves.stream()
                .map(Solve::getChallengeId)
                .collect(Collectors.toList());

        model.addAttribute("solvedChallengeIds", solvedChallengeIds);

        return "player-dashboard";
    }

    // NEW METHOD: The dedicated "My Solves" section
    @GetMapping("/solves")
    public String mySolves(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        List<Solve> userSolves = solveRepository.findByUserId(loggedInUser.getUserId());
        List<Challenge> solvedChallenges = new ArrayList<>();

        // Match the solves to the actual challenge details
        for (Solve solve : userSolves) {
            challengeRepository.findById(solve.getChallengeId()).ifPresent(solvedChallenges::add);
        }

        model.addAttribute("teamName", loggedInUser.getUsername());
        model.addAttribute("solvedChallenges", solvedChallenges);

        return "player-solves";
    }

    @PostMapping("/submit")
    public String submitFlag(@RequestParam Long challengeId,
                             @RequestParam String flag,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        // ANTI-CHEAT: Check if the team already solved this challenge

        if (solveRepository.existsByUserIdAndChallengeId(loggedInUser.getUserId(), challengeId)) {
            redirectAttributes.addFlashAttribute("errorMsg", "Access Denied: Your team already secured this flag.");
            return "redirect:/player/dashboard";
        }

        Challenge challenge = challengeRepository.findById(challengeId).orElse(null);

        if (challenge != null) {
            if (challenge.getFlag().equals(flag)) {

                // SAVE THE SOLVE TO ORACLE
                Solve newSolve = new Solve();
                newSolve.setUserId(loggedInUser.getUserId());
                newSolve.setChallengeId(challenge.getId());
                solveRepository.save(newSolve);

                redirectAttributes.addFlashAttribute("successMsg",
                        "Flag Correct! " + challenge.getPoints() + " points secured for " + challenge.getTitle() + "!");

            } else {
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Incorrect flag for " + challenge.getTitle() + ". Check your syntax and try again.");
            }
        }
        return "redirect:/player/dashboard";
    }
}
