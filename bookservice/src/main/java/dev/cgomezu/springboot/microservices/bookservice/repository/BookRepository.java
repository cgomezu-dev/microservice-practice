package dev.cgomezu.springboot.microservices.bookservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cgomezu.springboot.microservices.bookservice.domain.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
} 