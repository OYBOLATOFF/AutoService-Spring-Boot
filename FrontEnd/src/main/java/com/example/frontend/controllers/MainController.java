package com.example.frontend.controllers;

import com.example.frontend.models.User;
import com.example.frontend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @GetMapping("/cabinet")
    public String cabinetPage(Authentication authentication, Model model) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "cabinet";
    }

    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model) {
        User user = userService.findByUsername( authentication.getName() );
        model.addAttribute("user", user);
        return "adminPage";
    }

    @GetMapping("/reviews")
    public String reviewsPage() {
        return "reviews";
    }

}
