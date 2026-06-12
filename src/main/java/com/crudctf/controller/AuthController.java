package com.crudctf.controller;

import com.crudctf.model.User;
import com.crudctf.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    // Show the registration form
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // Process the new registration
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute User user, Model model) {
        // Force all new sign-ups to be regular players
        user.setRole("PLAYER");

        try {
            // Save the new team/hacker to the Oracle database
            userRepository.save(user);

            // Redirect them to the login page after successful registration
            return "redirect:/login";
        } catch (Exception e) {
            // If the username already exists (because it's marked as UNIQUE in the model)
            model.addAttribute("error", "That Team Name is already taken. Choose another.");
            return "register";
        }
    }

    @Autowired
    private UserRepository userRepository;

    // Show the login page
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // Process the login attempt
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {

        User user = userRepository.findByUsername(username);

        // Check if user exists and password matches
        if (user != null && user.getPassword().equals(password)) {
            // Save user in session so they stay logged in
            session.setAttribute("loggedInUser", user);

            // Redirect based on role
            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin/dashboard";
            } else {
                return "redirect:/player/dashboard"; // The hacker dashboard
            }
        }

        // If login fails, send them back to the login page with an error
        model.addAttribute("error", "Invalid username or password. Try again.");
        return "login";
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Destroys the active session
        return "redirect:/login";
    }
}