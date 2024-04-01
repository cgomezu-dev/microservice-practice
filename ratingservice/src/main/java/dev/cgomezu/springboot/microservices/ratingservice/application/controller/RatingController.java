package dev.cgomezu.springboot.microservices.ratingservice.application.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.cgomezu.springboot.microservices.ratingservice.domain.model.Rating;
import dev.cgomezu.springboot.microservices.ratingservice.domain.service.RatingService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/ratings")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Rating>> findRatingsByBookId(
        @RequestParam(required = false) Optional<Long> bookId) {
        return ResponseEntity.ok()
                .body(bookId.map(ratingService::findRatingsByBookId)
                    .orElseGet(ratingService::findAllRatings));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        return ResponseEntity.created(URI.create("/ratings/" + rating.getId()))
                .body(ratingService.createRating(rating));
    }

    @DeleteMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteRating(@PathVariable Long ratingId) {
        ratingService.deleteRating(ratingId);
        return ResponseEntity
            .noContent()
            .build();
    }

    @PutMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Rating> updateRating(@PathVariable Long ratingId, @RequestBody Rating rating) {
        return ResponseEntity.ok()
                .body(ratingService.updateRating(ratingId, rating));
    }

    @PatchMapping("/{ratingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Rating> updateRating(@PathVariable Long ratingId, @RequestBody Map<String, String> updates) {
        return ResponseEntity.ok()
                .body(ratingService.updateRating(ratingId, updates));
    }
}
