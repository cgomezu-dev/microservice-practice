package dev.cgomezu.springboot.microservices.ratingservice.application.controller;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;


import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

;

public class RatingControllerTest {
    
    @Test
    void testCreateRating() {
        
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "bookId": 1,
                    "stars": 7
                }
                """)
        .when()
            .port(8082)
            .basePath("/ratingservice")
            .post("/ratings")
        .then()
            .statusCode(201)
            .assertThat()
                .body("bookId", is(1))
                .and()
                .body("stars", is(7));
    }

    @Test
    void testDeleteRating() {

        given()
            .contentType(ContentType.JSON)
        .when()
            .port(8082)
            .basePath("/ratingservice")
            .delete("/ratings/1")
        .then()
            .statusCode(204);
    }

    @Test
    void testFindRatingsByBookId() {

        given()
            .contentType(ContentType.JSON)
        .when()
            .port(8082)
            .basePath("/ratingservice")
            .get("/ratings" + "?bookId=2")
        .then()
            .statusCode(200)
            .assertThat()
            .body("size()", is(1))
            .and()
            .body("[0].bookId", is(2));

    }

    @Test
    void testUpdateRating() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "id": 1,
                    "bookId": 1,
                    "stars": 8
                }
                """)
        .when()
            .port(8082)
            .basePath("/ratingservice")
            .put("/ratings/1")
        .then()
            .statusCode(200)
            .assertThat()
            .body("bookId", is(1))
            .and()
            .body("stars", is(8));
    }

    @Test
    void testPatchRating2() {
        given()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "stars": 9
                }
                """)
        .when()
            .port(8082)
            .basePath("/ratingservice")
            .patch("/ratings/2")
        .then()
            .statusCode(200)
            .assertThat()
            .body("stars", is(9));
    }
}
