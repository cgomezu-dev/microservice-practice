package dev.cgomezu.springboot.microservices.ratingservice.infraestructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cgomezu.springboot.microservices.ratingservice.domain.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {}