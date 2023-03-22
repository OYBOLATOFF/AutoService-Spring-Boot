package com.example.backend.controllers;

import com.example.backend.models.BasketItem;
import com.example.backend.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/basket")
@CrossOrigin
public class BasketRestController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping("{userId}")
    public Map<Object, Object> getUserBasket(@PathVariable("userId") String userNameOrId) {
        List<BasketItem> basketItems = null;
        try {
            basketItems = serviceService.findBasketByUserID( Integer.parseInt(userNameOrId) );
        } catch (Exception error) {
            basketItems = serviceService.findBasketByUsername( userNameOrId );
        }
        Double totalBasketSum = basketItems.stream().mapToDouble(basketItem -> basketItem.getTotalCost()).sum();
        return Map.of(
                "userId", userNameOrId,
                "count", basketItems.size(),
                "totalCost", Double.parseDouble(String.format("%.2f", totalBasketSum).replace(',', '.')),
                "basket", basketItems);
    }

    @PostMapping
    public ResponseEntity createOrUpdateBasketItem(@RequestParam("username") String username, @RequestParam("serviceTitle") String serviceTitle) {
        try {
            if (serviceService.existsBasketItem(username, serviceTitle)) {
                serviceService.incrementBasketItem(username, serviceTitle);
            } else {
                serviceService.createBasketItem(username, serviceTitle);
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception error) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity editBasketItem(@RequestParam("username") String username, @RequestParam("serviceTitle") String serviceTitle, @RequestParam("amount") int amount) {
        serviceService.updateBasketItem(username, serviceTitle, amount);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteBasketItem(@RequestParam("username") String username, @RequestParam("serviceTitle") String serviceTitle) {
        serviceService.deleteBasketItem(username, serviceTitle);
        return new ResponseEntity(HttpStatus.OK);
    }

}
