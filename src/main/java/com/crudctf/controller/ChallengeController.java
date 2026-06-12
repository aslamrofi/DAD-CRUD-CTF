package com.crudctf.controller;

import com.crudctf.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ChallengeController {

    @Autowired
    private ChallengeRepository repository;

    // DELETE: Remove a target from the database
    @GetMapping("/admin/delete/{id}")
    public String deleteChallenge(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}