package com.example.backend.controllers;

import com.example.backend.DTO.SaveSpecialistDTO;
import com.example.backend.models.Specialist;
import com.example.backend.services.SpecialistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/specialists")
@CrossOrigin
public class SpecialistRestController {

    @Autowired
    private SpecialistService specialistService;

    @GetMapping
    public List<Specialist> findAllSpecialists() {
        List<Specialist> specialists = specialistService.findAll();
        return specialists;
    }

    @PostMapping
    public ResponseEntity addSpecialist(@RequestBody SaveSpecialistDTO specialistDTO) {
        try {
            specialistService.saveByDTO(specialistDTO);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity("Что-то пошло не так", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{specialistID}")
    @CrossOrigin
    public ResponseEntity deleteSpecialist(@PathVariable("specialistID") int specialistID) {
        Optional<Specialist> specialistOpt = specialistService.findById(specialistID);
        if (specialistOpt.isPresent()) {
            Specialist specialist = specialistOpt.get();
            specialistService.delete(specialist);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping
    @CrossOrigin
    public ResponseEntity updateSpecialist(@RequestBody SaveSpecialistDTO saveSpecialistDTO) {
        try {
            specialistService.updateById(saveSpecialistDTO);
            return new ResponseEntity("Specialist info has been updated!", HttpStatus.OK);
        } catch (Exception error) {
            error.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
