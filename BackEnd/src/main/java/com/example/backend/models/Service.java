package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private Double priceWithoutDiscount;
    private Double priceWithDiscount;

    @OneToMany(mappedBy = "specialization")
    @JsonIgnore
    private List<Specialist> specialistList;

    @ManyToMany(mappedBy = "serviceList")
    @JsonIgnore
    private List<User> userList;

    @JsonIgnore
    private String image;

    public String getShortDescription() {
        try {
            String[] words = getDescription().split(" ");

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 5 && i < words.length; i++) {
                sb.append(words[i]+" ");
            }

            return sb.toString()+"...";
        } catch (Exception error) {
            return getDescription();
        }
    }

    @JsonProperty("image")
    public String getImageSrc() {
        return (image == null)? "/images/other/empty_good_img.png": "data:image/jpeg;base64,"+image;
    }

    public boolean hasDiscount() {
        return getPriceWithDiscount() != null;
    }
}
