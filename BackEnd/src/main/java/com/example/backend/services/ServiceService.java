package com.example.backend.services;

import com.example.backend.models.BasketItem;
import com.example.backend.models.Service;
import com.example.backend.repositories.ServiceRepository;
import com.example.backend.utils.FilesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public Optional<Service> findById(int serviceID) {
        return serviceRepository.findById(serviceID);
    }

    public void setNewImage(Service service, MultipartFile multipartFile) {
        String base64 = FilesUtil.getBase64FromMultipartFile(multipartFile);
        service.setImage(base64);
        save(service);
    }

    public void save(Service service) {
        serviceRepository.save(service);
    }

    public void updateById(Service service) {
        serviceRepository.updateServiceById(
                service.getTitle(),
                service.getDescription(),
                service.getPriceWithoutDiscount(),
                service.getPriceWithDiscount(),
                service.getId()
        );
    }

    public void deleteById(int id) {
        serviceRepository.deleteById(id);
    }

    public List<BasketItem> findBasketByUserID(int userID) {
        return serviceRepository.findBasketByUserId(userID);
    }

    public List<BasketItem> findBasketByUsername(String username) {
        return serviceRepository.findBasketByUsername(username);
    }

    public void updateBasketItem(String username, String serviceTitle, int amount) {
        serviceRepository.updateBasketItem(username, serviceTitle, amount);
    }

    public void deleteBasketItem(String username, String serviceTitle) {
        serviceRepository.deleteBasketItem(username, serviceTitle);
    }

    public boolean existsBasketItem(String username, String serviceTitle) {
        String result = serviceRepository.existsBasketItem(username, serviceTitle);
        return result != null;
    }

    public void createBasketItem(String username, String serviceTitle) {
        serviceRepository.createBasketItem(username, serviceTitle);
    }

    public void incrementBasketItem(String username, String serviceTitle) {
        serviceRepository.incrementBasketItem(username, serviceTitle);
    }
}
