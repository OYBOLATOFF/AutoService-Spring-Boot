package com.example.backend.services;

import com.example.backend.DTO.SaveSpecialistDTO;
import com.example.backend.models.Specialist;
import com.example.backend.repositories.SpecialistRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.FilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SpecialistService {

    @Autowired
    private SpecialistRepository specialistRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Specialist> findAll() {
        return specialistRepository.findAll();
    }

    public Optional<Specialist> findById(int specialistID) {
        return specialistRepository.findById(specialistID);
    }

    public void delete(Specialist specialist) {
        specialistRepository.delete(specialist);
    }

    public void updateById(SaveSpecialistDTO saveSpecialistDTO) {
        specialistRepository.updateSpecialistById(
                saveSpecialistDTO.getFirstName(),
                saveSpecialistDTO.getLastName(),
                saveSpecialistDTO.getSpecializationId(),
                saveSpecialistDTO.getWorkExperience(),
                saveSpecialistDTO.getId()
        );
    }

    public void setNewAvatar(Specialist specialist, MultipartFile multipartFile) {
        String base64 = FilesUtil.getBase64FromMultipartFile(multipartFile);
        specialist.setPortrait(base64);
        save(specialist);
    }

    private void save(Specialist specialist) {
        specialistRepository.save(specialist);
    }

    public void saveByDTO(SaveSpecialistDTO specialistDTO) {
        specialistRepository.saveByDTO(specialistDTO.getFirstName(), specialistDTO.getLastName(), specialistDTO.getSpecializationId(), specialistDTO.getWorkExperience());
    }
}
