package dev.cgomezu.springboot.microservices.ratingservice.infraestructure.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cgomezu.springboot.microservices.ratingservice.domain.model.Rating;

@Repository
public class RatingCacheRepository implements InitializingBean {

    private final Logger LOGGER = LoggerFactory.getLogger(RatingCacheRepository.class);

    @Autowired
    private LettuceConnectionFactory cacheConnectionFactory;

    private StringRedisTemplate redisTemplate;
    private ValueOperations<String, String> valueOps;
    private SetOperations<String, String> setOps;

    private ObjectMapper jsonMapper;

    @Override
    public void afterPropertiesSet() throws Exception {

        this.redisTemplate = new StringRedisTemplate(cacheConnectionFactory);
        this.valueOps = redisTemplate.opsForValue();
        this.setOps = redisTemplate.opsForSet();

        jsonMapper = new ObjectMapper();
        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Rating> findCachedRatingsBooksByBookId(Long bookId) {
        List<Rating> ratings;

        try {
            ratings = setOps.members("book-" + bookId)
                .stream()
                .map(ratingId -> {
                    try {
                        var rating = jsonMapper.readValue(valueOps.get(ratingId), Rating.class);
                        rating.setFromCache(true);
                        return rating;
                    } catch (JsonProcessingException e) {
                        LOGGER.error("Error reading rating from cache: {}", e.getMessage());
                        return null;
                    }
                })
                .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error reading ratings from cache: {}", e.getMessage());
            ratings = List.of();
        }
        return ratings;
    }

    public Rating findCachedRaitingById(Long ratingId) {
        try {
            return jsonMapper.readValue(valueOps.get("rating-" + ratingId), Rating.class);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error reading rating from cache: {}", e.getMessage());
            return null;
        }
    }

    public List<Rating> findAllCahedRatings() {

        List<Rating> ratings;
        
        try {
            ratings = redisTemplate
                    .keys("rating*")
                    .stream()
                    .map(ratingId -> {
                        try {
                            var rating = jsonMapper.readValue(valueOps.get(ratingId), Rating.class);
                            rating.setFromCache(true);
                            return rating;
                        } catch (JsonProcessingException e) {
                            LOGGER.error("Error reading rating from cache: {}", e.getMessage());
                            return null;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error reading ratings from cache: {}", e.getMessage());
            ratings = List.of();
        } 
        return ratings;
    }

    public boolean cacheRating(Rating ratingPersisted) {
        try {
            valueOps.set("rating-" + ratingPersisted.getId(), jsonMapper.writeValueAsString(ratingPersisted));
            setOps.add("book-" + ratingPersisted.getBookId(), "rating-" + ratingPersisted.getId());
            return true;
        } catch (Exception e) {
            LOGGER.error("Error caching rating: {}", e.getMessage());
            return false;
        }
    }

    public boolean updateCachedRating(Rating ratingPersisted) {
        try {
            valueOps.set("rating-" + ratingPersisted.getId(), jsonMapper.writeValueAsString(ratingPersisted));
            return true;
        } catch (Exception e) {
            LOGGER.error("Error updating rating: {}", e.getMessage());
           return false;
        }
    }

    public void deleteCachedRating(Long ratingId) {
        Rating ratingToDel;
        try {
            ratingToDel = jsonMapper.readValue(valueOps.get("rating-" + ratingId), Rating.class);
            setOps.remove("book-" + ratingToDel.getBookId(), "rating-" + ratingId);
            redisTemplate.delete("rating-" + ratingId);
        } catch (Exception e) {
            LOGGER.error("Error deleting rating: {}", e.getMessage());
        }
    }

}
