package dev.cgomezu.springboot.microservices.bookservice.application.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.cgomezu.springboot.microservices.bookservice.domain.model.Book;
import dev.cgomezu.springboot.microservices.bookservice.domain.service.BookService;


@RestController
@RequestMapping("/books")
public class BookController {
    
    @Autowired
    private BookService bookService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Book>> findAllBooks() {
        return ResponseEntity.ok()
            .body(bookService.findAllBooks());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Book> findBook(@PathVariable("id") long id) {
        return ResponseEntity.ok()
            .body(bookService.findBookById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        return ResponseEntity
            .created(URI.create("/books/" + book.getId()))
            .body(bookService.createBook(book));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBook(@PathVariable("id") long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Book> updateBook(@PathVariable("id") long id, 
        @RequestBody Book book) {
        return ResponseEntity.ok()
            .body(bookService.updateBook(id, book));
    }
    
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Book> patchBook(@PathVariable("id") long id, 
        @RequestBody Map<String, String> updates) {
        return ResponseEntity.ok()
            .body(bookService.updateBook(id, updates));
    }       
}
