package com.crudctf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    // This catches anyone visiting the base URL (/)
    @GetMapping("/")
    public String redirectToDashboard() {
        // Automatically forwards them to your admin dashboard
        return "redirect:/login";
    }
}