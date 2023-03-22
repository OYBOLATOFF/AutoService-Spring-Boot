package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ManyToMany
    @JoinTable(name = "basket",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id"))
    @JsonIgnore
    private List<Service> serviceList;

    @JsonProperty("basket")
    public List<Map<Object, Object>> basket() {
        List<Map<Object, Object>> result = new ArrayList<>();
        serviceList.stream().collect(Collectors.toSet()).forEach(service -> {
            result.add(Map.of(
                    "title", service.getTitle(),
                    "price", service.getPriceWithoutDiscount(),
                    "discount", (service.getPriceWithDiscount() == null)? 0: service.getPriceWithDiscount(),
                    "amount", Collections.frequency(serviceList, service)
            ));
        });

        return result;
    }

    public User(String username, String password, Date birthday) {
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }

    public boolean isAdmin() {
        return role.equals("ADMIN");
    }
}
