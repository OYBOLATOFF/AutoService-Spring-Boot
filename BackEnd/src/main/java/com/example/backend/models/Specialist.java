package com.example.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "specialist")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Specialist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private Integer workExperience;
    @JsonIgnore
    private String portrait;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialization_id")
    @JsonIgnore
    private Service specialization;

    @JsonProperty("specialization")
    public String getSpecialization() {
        return specialization.getTitle();
    }

    @JsonProperty("portrait")
    public String getPortraitSrc() {
        return (portrait == null)? "/images/other/default_avatar.png": "data:image/jpeg;base64,"+portrait;
    }

    public Specialist(String firstName, String lastName, Integer workExperience, String portrait, Service specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.workExperience = workExperience;
        this.portrait = portrait;
        this.specialization = specialization;
    }
}
