package dev.cgomezu.springboot.microservices.bookservice.application.controller;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.lessThan;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.http.ContentType;

public class BookControllerTest {

    @Test
    public void testFindAllBooks() {

        baseURI = "http://localhost:8080";

        given()
            .when()
                .get("/books")
            .then()
                .time(lessThan(1000L))
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", hasItems(1,2,3,4));
    }

    @Test
    public void testFindBookById() {
            
            baseURI = "http://localhost:8080";
    
            given()
                .when()
                    .get("/books/1")
                .then()
                    .time(lessThan(1000L))
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(1));
    }

    @Test
    public void testCreateBook() {
            
            baseURI = "http://localhost:8080";

            given()
                .contentType(ContentType.JSON)
                .body("""
                    {
                        "title": "Progress Pilgrim",
                        "author": "Jhon Bounjan"
                    }
                    """)
            .when()
                .post("/books")
                
            .then()
                .statusCode(HttpStatus.CREATED.value())
                .time(lessThan(1000L))
                .assertThat()
                .contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteBook() {
            
            baseURI = "http://localhost:8080";

            given()
                .when()
                    .delete("/books/1")
                .then()
                    .time(lessThan(1000L))
                    .assertThat()
                    .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
