package com.example.backend.repositories;

import com.example.backend.models.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist, Integer> {
    @Query("update Specialist s set s.firstName = ?1, s.lastName = ?2, s.specialization.id = ?3, s.workExperience = ?4 where s.id = ?5")
    @Modifying
    void updateSpecialistById(String firstName, String lastName, int specializationId, Integer workExperience, Integer specialistID);

    @Query("SELECT s FROM Specialist s JOIN FETCH s.specialization")
    List<Specialist> findAll();

    @Query(value = "INSERT INTO specialist(first_name, last_name, work_experience, specialization_id) VALUES (:firstName, :lastName, :workExperience, :specializationId)", nativeQuery = true)
    @Modifying
    void saveByDTO(String firstName, String lastName, int specializationId, Integer workExperience);
}
