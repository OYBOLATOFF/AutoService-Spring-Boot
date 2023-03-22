package com.example.backend.controllers;

import com.example.backend.models.User;
import com.example.backend.services.ServiceService;
import com.example.backend.services.SpecialistService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private ServiceService serviceService;

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

}
