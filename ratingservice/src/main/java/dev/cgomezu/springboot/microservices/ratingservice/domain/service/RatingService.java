package dev.cgomezu.springboot.microservices.ratingservice.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import dev.cgomezu.springboot.microservices.ratingservice.domain.model.Rating;
import dev.cgomezu.springboot.microservices.ratingservice.domain.model.exception.RatingNotFoundException;
import dev.cgomezu.springboot.microservices.ratingservice.infraestructure.repository.RatingCacheRepository;
import dev.cgomezu.springboot.microservices.ratingservice.infraestructure.repository.RatingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@Service
@Transactional(readOnly = true)
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingCacheRepository ratingCacheRepository;


    @CircuitBreaker(name = "ratingsFromDB", fallbackMethod = "findAllCahedRatings")
    public List<Rating> findAllRatings() {
        List<Rating> ratings = ratingCacheRepository.findAllCahedRatings();
        if (ratings.isEmpty()) {
            ratings = ratingRepository.findAll();
            ratings.forEach(ratingCacheRepository::cacheRating);
        }
        return ratings;
    }

    public List<Rating> findAllCahedRatings(Exception exception) {
        return ratingCacheRepository.findAllCahedRatings();
    }

    @CircuitBreaker(name = "ratingsByBookIdFromDB", fallbackMethod = "findCachedRatingsBooksByBookId")
    public List<Rating> findRatingsByBookId(Long bookId) {
        List<Rating> ratings = ratingCacheRepository.findCachedRatingsBooksByBookId(bookId);
        if (ratings.isEmpty()) {
            ratings = ratingRepository.findAllById(List.of(bookId));
            if (ratings.isEmpty()) {
                throw new RatingNotFoundException(String.format("No ratings found for book ID %d", bookId));
            }
            ratings.forEach(ratingCacheRepository::cacheRating);
        }
        return ratings;
    }

    public List<Rating> findCachedRatingsBooksByBookId(Long bookId, Exception exception) {
        return ratingCacheRepository.findCachedRatingsBooksByBookId(bookId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Rating createRating(Rating rating) {
        final Rating ratingPersisted = ratingRepository.save(rating); 
        ratingCacheRepository.cacheRating(ratingPersisted);
        return ratingPersisted;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
        ratingCacheRepository.deleteCachedRating(ratingId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Rating updateRating(Long ratingId, Rating rating) {
        Preconditions.checkNotNull(rating);
        Preconditions.checkState(ratingId == rating.getId(), String.format("Rating ID %d does not match rating ID %d", ratingId, rating.getId()));
        Preconditions.checkArgument(ratingRepository.findById(ratingId).isPresent(), String.format("Rating ID %d does not exist", ratingId));

        final Rating ratingPersisted = ratingRepository.save(rating);
        ratingCacheRepository.updateCachedRating(ratingPersisted);

        return ratingPersisted;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Rating updateRating(Long ratingId, Map<String, String> updates) {
        final Rating rating = ratingRepository.findById(ratingId).get();

        updates.keySet().forEach(key -> {
            switch (key) {
                case "bookId":
                    rating.setBookId(Long.parseLong(updates.get(key)));
                    break;
                case "stars":
                    rating.setStars(Integer.parseInt(updates.get(key)));
                    break;
            }
        });

        final Rating ratingPersisted = ratingRepository.save(rating);
        ratingCacheRepository.updateCachedRating(ratingPersisted);

        return ratingPersisted;
    }

}
