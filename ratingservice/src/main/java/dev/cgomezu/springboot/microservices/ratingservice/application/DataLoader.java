package dev.cgomezu.springboot.microservices.ratingservice.application;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import dev.cgomezu.springboot.microservices.ratingservice.domain.model.Rating;
import dev.cgomezu.springboot.microservices.ratingservice.domain.service.RatingService;
import dev.cgomezu.springboot.microservices.ratingservice.infraestructure.repository.RatingRepository;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Rating> ratings = List.of(
            Rating.builder().bookId(1L).stars(5).build(),
            Rating.builder().bookId(2L).stars(4).build(),
            Rating.builder().bookId(3L).stars(3).build(),
            Rating.builder().bookId(4L).stars(2).build(),
            Rating.builder().bookId(5L).stars(1).build()
        );

       //ratings.forEach(ratingService::createRating);
       
       ratingRepository.saveAll(ratings);
    }
    
}
