package com.example.backend.DTO;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter @Getter @ToString
public class SaveSpecialistDTO {
    private int id;
    private String firstName;
    private String lastName;
    private int specializationId;
    private int workExperience;
}
