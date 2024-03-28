package dev.cgomezu.springboot.microservices.bookservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.cgomezu.springboot.microservices.bookservice.domain.model.Book;
import dev.cgomezu.springboot.microservices.bookservice.domain.service.BookService;

@SpringBootApplication(scanBasePackages = "dev.cgomezu.springboot.microservices.bookservice")
public class BookserviceApplication implements CommandLineRunner {

	final BookService bookService;

	public BookserviceApplication(BookService bookService) {
		this.bookService = bookService;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookserviceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.bookService.createBook(new Book().builder().title("To Kill a Mockingbird").author("Harper Lee").build()); 
        this.bookService.createBook(new Book().builder().title("1984").author("George Orwell").build());
        this.bookService.createBook(new Book().builder().title("The Catcher in the Rye").author("J.D. Salinger").build());
        this.bookService.createBook(new Book().builder().title("The Great Gatsby").author("F. Scott Fitzgerald").build());
	}

}
