package com.example.backend.repositories;

import com.example.backend.models.BasketItem;
import com.example.backend.models.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    @Query("update Service s set s.title = ?1, s.description = ?2, s.priceWithoutDiscount = ?3, s.priceWithDiscount = ?4 where s.id = ?5")
    @Modifying
    void updateServiceById(String title, String description, Double priceWithoutDiscount, Double priceWithDiscount, Integer ID);

    @Query("SELECT DISTINCT(s) FROM Service s LEFT JOIN s.specialistList")
    List<Service> findAll();

    @Query(value = "SELECT new com.example.backend.models.BasketItem(s.title,  s.priceWithoutDiscount, s.priceWithDiscount, b.amount) " +
            "FROM User u JOIN Basket b ON u.id = b.userId JOIN Service s ON s.id = b.serviceId WHERE b.userId = ?1")
    List<BasketItem> findBasketByUserId(int userID);

    @Query(value = "SELECT new com.example.backend.models.BasketItem(s.title, s.priceWithoutDiscount, s.priceWithDiscount, b.amount) " +
            "FROM User u JOIN Basket b ON u.id = b.userId JOIN Service s ON s.id = b.serviceId WHERE u.username = ?1")
    List<BasketItem> findBasketByUsername(String username);

    @Query(value = "UPDATE basket\n" +
            "    SET amount = :amount\n" +
            "WHERE (SELECT user.id FROM user WHERE user.username = :username) = basket.user_id\n" +
            "AND (SELECT service.id FROM service WHERE title = :serviceTitle) = basket.service_id;", nativeQuery = true)
    @Modifying
    void updateBasketItem(String username, String serviceTitle, int amount);

    @Query(value = "DELETE FROM basket WHERE " +
            "(SELECT user.id FROM user WHERE user.username = :username) = basket.user_id  AND " +
            "(SELECT service.id FROM service WHERE title = :serviceTitle) = basket.service_id;", nativeQuery = true)
    @Modifying
    void deleteBasketItem(String username, String serviceTitle);

    @Query(value = "SELECT user.username FROM user JOIN basket ON user.id = basket.user_id " +
            "JOIN service s on basket.service_id = s.id WHERE username = :username AND " +
            "title = :serviceTitle", nativeQuery = true)
    String existsBasketItem(String username, String serviceTitle);

    @Query(value = "INSERT INTO basket(user_id, service_id, amount) VALUES ((SELECT user.id FROM user WHERE user.username = :username), (SELECT service.id FROM service WHERE service.title = :serviceTitle), 1);", nativeQuery = true)
    @Modifying
    void createBasketItem(String username, String serviceTitle);

    @Query(nativeQuery = true, value = "UPDATE basket SET amount = amount+1 WHERE (SELECT user.id FROM user WHERE user.username = :username) = basket.user_id AND (SELECT service.id FROM service WHERE title = :serviceTitle) = basket.service_id")
    @Modifying
    void incrementBasketItem(String username, String serviceTitle);
}
