package com.example.backend.validators;

import com.example.backend.DTO.UserDTO;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserDTOValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userDTO.isEmpty()) {
            errors.rejectValue("username", "", "Все поля должны быть введены!");
        } else if (userService.existsByUsername(userDTO.getUsername())) {
            errors.rejectValue("username", "", "Пользователь "+userDTO.getUsername()+" уже существует!");
        } else if (!userDTO.getPassword().equals(userDTO.getConfirmedPassword())) {
            errors.rejectValue("password", "", "Пароли не совпадают!");
        }
    }
}
