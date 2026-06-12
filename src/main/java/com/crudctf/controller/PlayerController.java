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

    @Autowired
    private com.crudctf.service.OpenAiService openAiService;

    @Autowired
    private com.crudctf.repository.AiRequestRepository aiRequestRepository;

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
    // NEW ROUTE: Ask the AI for a hint
    @PostMapping("/ask-ai")
    public String askAi(@RequestParam Long challengeId, HttpSession session, RedirectAttributes redirectAttributes) {

        com.crudctf.model.User loggedInUser = (com.crudctf.model.User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) return "redirect:/login";

        com.crudctf.model.Challenge challenge = challengeRepository.findById(challengeId).orElse(null);

        if (challenge != null) {
            // 1. Get the hint from OpenAI
            String aiResponse = openAiService.generateHint(challenge.getTitle(), challenge.getDescription());
            String promptContext = "Target: " + challenge.getTitle() + " | Intel: " + challenge.getDescription();

            // 2. Log the request to your Oracle Database
            com.crudctf.model.AiRequest log = new com.crudctf.model.AiRequest();
            log.setTeamId(loggedInUser.getUsername());
            log.setChallengeId(String.valueOf(challenge.getId()));
            log.setPromptContext(promptContext);
            log.setAiResponse(aiResponse);
            aiRequestRepository.save(log);

            // 3. Send the response back to the HTML page
            redirectAttributes.addFlashAttribute("aiHint", aiResponse);
            redirectAttributes.addFlashAttribute("aiHintChallengeId", challenge.getId());
        }

        return "redirect:/player/dashboard";
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
