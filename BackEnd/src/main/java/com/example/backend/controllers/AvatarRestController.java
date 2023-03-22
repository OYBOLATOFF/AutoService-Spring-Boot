package com.example.backend.controllers;

import com.example.backend.models.Service;
import com.example.backend.models.Specialist;
import com.example.backend.services.ServiceService;
import com.example.backend.services.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/api")
@CrossOrigin
public class AvatarRestController {

    @Autowired
    private SpecialistService specialistService;

    @Autowired
    private ServiceService serviceService;

    @PostMapping("changeMasterAvatar/{id}")
    public String changeMasterAvatar(@PathVariable("id") int masterID, @RequestParam("imageFile") MultipartFile multipartFile) {
        Optional<Specialist> specialistOptional = specialistService.findById(masterID);
        if (specialistOptional.isPresent()) {
            Specialist specialist = specialistOptional.get();
            specialistService.setNewAvatar(specialist, multipartFile);
        }

        return "redirect:http://localhost:8080/admin";
    }

    @PostMapping("changeServiceAvatar/{id}")
    public String changeServiceAvatar(@PathVariable("id") int serviceID, @RequestParam("imageFile") MultipartFile multipartFile) {
        Optional<Service> serviceOptional = serviceService.findById(serviceID);
        if (serviceOptional.isPresent()) {
            Service service = serviceOptional.get();
            serviceService.setNewImage(service, multipartFile);
        }
        return "redirect:http://localhost:8080/admin";
    }
}
