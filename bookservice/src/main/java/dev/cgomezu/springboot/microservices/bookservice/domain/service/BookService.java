package dev.cgomezu.springboot.microservices.bookservice.domain.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import dev.cgomezu.springboot.microservices.bookservice.domain.model.Book;
import dev.cgomezu.springboot.microservices.bookservice.domain.model.exception.BookNotFoundException;
import dev.cgomezu.springboot.microservices.bookservice.repository.BookRepository;
import lombok.NonNull;



@Service
@Transactional(readOnly = true)
public class BookService {

    private final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

    @Autowired
    BookRepository bookRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(String.format("Book with ID: %s Not Found." , id)));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Book createBook(@NonNull final Book book) {
        return bookRepository.save(book);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBook(long id) {
        LOGGER.info("Book with id {} has been deleted", id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Book updateBook(long id, Book book) {   
        Preconditions.checkNotNull(book, "Book cannot be null.");
        Preconditions.checkState(id == book.getId(), "The ID of the book must match the ID of the URL.");
        Preconditions.checkArgument(bookRepository.findById(id).isPresent(), "Book with ID: " + id + " Not Found.");

        return bookRepository.save(book);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Book updateBook(long id, Map<String, String> updates) {
        final Book book = findBookById(id);
        
        updates.keySet()
            .forEach(key -> {
                switch (key) {
                    case "author":
                        book.setAuthor(updates.get(key));
                        break;
                    case "title":
                        book.setTitle(updates.get(key));
                        break;
                }
            });
        
        return bookRepository.save(book);
    }

}
