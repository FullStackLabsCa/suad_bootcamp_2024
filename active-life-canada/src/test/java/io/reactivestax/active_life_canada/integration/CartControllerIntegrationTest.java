package io.reactivestax.active_life_canada.integration;

import io.reactivestax.active_life_canada.dto.CartDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CartControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String BASE_URL;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        BASE_URL = "http://localhost:" + port + "/api/v1/cart";
    }

    @Test
    void testSaveCart() {
        CartDto cartDto = CartDto.builder()
                .offeredCourseIds(List.of(1L, 2L, 3L))
                .isActive(true)
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("X-family-member-id", 1L)
                .body(cartDto)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        CartDto createdCart = response.as(CartDto.class);
        assertThat(createdCart).isNotNull();
        assertThat(createdCart.getOfferedCourseIds()).contains(1L, 2L, 3L);
        assertThat(createdCart.getIsActive()).isTrue();
    }

    @Test
    void testGetCart() {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL + "/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        CartDto cartDto = response.as(CartDto.class);
        assertThat(cartDto).isNotNull();
    }

    @Test
    void testUpdateCart() {
        CartDto cartDto = CartDto.builder()
                .cartId(1L)
                .offeredCourseIds(List.of(4L, 5L))
                .isActive(false)
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(cartDto)
                .when()
                .put(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        CartDto updatedCart = response.as(CartDto.class);
        assertThat(updatedCart).isNotNull();
        assertThat(updatedCart.getOfferedCourseIds()).contains(4L, 5L);
        assertThat(updatedCart.getIsActive()).isFalse();
    }

    @Test
    void testDeleteCart() {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_URL + "/deleteCart/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        String status = response.getBody().asString();
        assertEquals("Successfully deleted", status);
    }
}
