package com.crudctf.controller;

import com.crudctf.model.Challenge;
import com.crudctf.model.User;
import com.crudctf.repository.ChallengeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    private ChallengeRepository challengeRepository;

    // READ: Show the dashboard and active challenges
    @GetMapping("/dashboard")
    public String playerDashboard(HttpSession session, Model model) {
        // Security check: Bounce them to login if they aren't authenticated
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Pass the team name and all challenges to the HTML template
        model.addAttribute("teamName", loggedInUser.getUsername());
        model.addAttribute("challenges", challengeRepository.findAll());

        return "player-dashboard";
    }

    // CREATE (Gameplay): Handle flag submissions
    @PostMapping("/submit")
    public String submitFlag(@RequestParam Long challengeId,
                             @RequestParam String flag,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        Optional<Challenge> optionalChallenge = challengeRepository.findById(challengeId);

        if (optionalChallenge.isPresent()) {
            Challenge challenge = optionalChallenge.get();

            // Check if the submitted flag matches the database flag
            if (challenge.getFlag().equals(flag)) {
                redirectAttributes.addFlashAttribute("successMsg",
                        "Flag Correct! " + challenge.getPoints() + " points secured for " + challenge.getTitle() + "!");

                // TODO for Wan Afiq: This is where you will save the success to the "Solves" table!

            } else {
                redirectAttributes.addFlashAttribute("errorMsg",
                        "Incorrect flag for " + challenge.getTitle() + ". Check your syntax and try again.");
            }
        }
        return "redirect:/player/dashboard";
    }
}
