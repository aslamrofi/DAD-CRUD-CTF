package com.crudctf.controller;

import com.crudctf.model.Challenge;
import com.crudctf.model.User;
import com.crudctf.repository.ChallengeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AdminController {

    @Autowired
    private ChallengeRepository challengeRepository;

    // 1. Show the Admin Dashboard and all active challenges
    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(HttpSession session, Model model) {
        // Security check: Make sure they are actually an ADMIN
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null || !"ADMIN".equals(loggedInUser.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("challenges", challengeRepository.findAll());
        return "admin-dashboard";
    }

    // 2. The File Upload Route you asked about
    @PostMapping("/admin/challenge/add")
    public String addChallenge(@ModelAttribute Challenge challenge,
                               @RequestParam("challengeFile") MultipartFile file) {
        try {
            // Check if the admin actually attached a payload
            if (!file.isEmpty()) {
                challenge.setFileName(file.getOriginalFilename());
                challenge.setFileType(file.getContentType());
                challenge.setFileData(file.getBytes());
            }
            // Save the challenge and the file data into the Oracle database
            challengeRepository.save(challenge);

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Bounce the admin back to the dashboard to see the new target
        return "redirect:/admin/dashboard";
    }
}