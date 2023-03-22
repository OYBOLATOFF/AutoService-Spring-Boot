package com.example.frontend.models;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private Date birthday;
    private String role;
    private String avatar;

    public User(String username, String password, Date birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    public boolean isAdmin() {
        return role.equals("ADMIN");
    }
}
