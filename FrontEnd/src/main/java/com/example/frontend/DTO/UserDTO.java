package com.example.frontend.DTO;

import com.example.frontend.models.User;
import lombok.*;

import java.sql.Date;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    public String username;
    public String password;
    public String confirmedPassword;
    public Date birthDay;

    public User createUser() {
        return new User(username, password, birthDay);
    }

    public boolean isEmpty() {
        return (username.isEmpty() || password.isEmpty() || confirmedPassword.isEmpty() || birthDay == null);
    }
}
