package com.example.backend.controllers;

import com.example.backend.DTO.UserDTO;
import com.example.backend.models.User;
import com.example.backend.services.UserService;
import com.example.backend.validators.UserDTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserRestController {

    @Autowired
    private UserDTOValidator userDTOValidator;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @GetMapping("isAdmin/{username}")
    public boolean isAdmin(@PathVariable("username") String username) {
        User user = userService.findByUsername(username);
        return user.isAdmin();
    }

    @PostMapping
    public ResponseEntity registerNewUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        userDTOValidator.validate(userDTO, bindingResult);
        if (!bindingResult.hasErrors()) {
            User user = userDTO.createUser();
            user.setPassword( encoder.encode(user.getPassword()) );
            user.setRole("USER");
            userService.save(user);
            return new ResponseEntity("Регистрация прошла успешно", HttpStatus.OK);
        }

        String errorMessage = bindingResult.getAllErrors().get(0).getDefaultMessage();
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
