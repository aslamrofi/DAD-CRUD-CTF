package com.crudctf.controller;

import com.crudctf.model.Challenge;
import com.crudctf.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class ChallengeController {

    @Autowired
    private ChallengeRepository repository;

    // READ: Display all challenges
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("challenges", repository.findAll());
        model.addAttribute("newChallenge", new Challenge());
        return "admin-dashboard";
    }

    // CREATE: Save a new challenge to Oracle
    @PostMapping("/add")
    public String addChallenge(@ModelAttribute Challenge challenge) {
        repository.save(challenge);
        return "redirect:/admin/dashboard";
    }

    // DELETE: Remove a challenge by ID
    @GetMapping("/delete/{id}")
    public String deleteChallenge(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}