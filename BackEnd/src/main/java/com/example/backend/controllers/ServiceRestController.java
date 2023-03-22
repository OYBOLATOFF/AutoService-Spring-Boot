package com.example.backend.controllers;

import com.example.backend.models.Service;
import com.example.backend.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin
public class ServiceRestController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public List<Service> findAllServices() {
        return serviceService.findAll();
    }

    @PostMapping
    public ResponseEntity addService(@RequestBody Service service) {
        System.out.println(service);
        try {
            serviceService.save(service);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity("Какие-то данные заполнены неверно!", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity updateService(@RequestBody Service service) {
        try {
            serviceService.updateById(service);
            return new ResponseEntity("Service info has been updated!", HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity deleteService(@RequestParam(name = "id") int id) {
        serviceService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
