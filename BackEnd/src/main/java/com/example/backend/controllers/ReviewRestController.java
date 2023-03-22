package com.example.backend.controllers;

import com.example.backend.models.Review;
import com.example.backend.repositories.ReviewRepository;
import com.example.backend.services.ReviewService;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin
public class ReviewRestController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public List<Review> getReviews() {
        return reviewService.findAll();
    }

    @PostMapping
    public ResponseEntity addReview(@RequestParam("author") String author, @RequestParam("content") String content, @RequestParam("rate") int rate) {
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        Review review = new Review(author, currentTimeStamp, content, rate);
        reviewService.addReview(review);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteReview(@RequestParam("reviewId") int reviewId) {
       reviewService.deleteById(reviewId);
        return new ResponseEntity(HttpStatus.OK);
    }

}
