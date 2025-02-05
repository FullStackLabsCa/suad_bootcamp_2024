package io.reactivestax.active_life_canada.integration;

import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseFeeDto;
import io.reactivestax.active_life_canada.enums.FeeType;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CourseRegistrationIntegrationTest {

    @LocalServerPort
    private int port;

    private String BASE_URL;

    private CourseRegistrationDto courseRegistrationDto;

    @BeforeAll
     void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        BASE_URL = "http://localhost:" + port + "/api/v1/courseRegistrations/enrollment";

         courseRegistrationDto = CourseRegistrationDto.builder()
                .cost(2.50)
                .enrollmentDate(LocalDate.parse("2025-02-02"))
                .offeredCourseId(5L)
                .build();
    }

    @Test
     void testCourseRegistration() {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .header("X-family-member-id", "19")
                .body(courseRegistrationDto)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        CourseRegistrationDto createdOfferedCourse = response.as(CourseRegistrationDto.class);

        assertThat(createdOfferedCourse).isNotNull();
        assertThat(createdOfferedCourse.getEnrollmentDate()).isEqualTo(LocalDate.parse("2025-02-02"));
        assertThat(createdOfferedCourse.getOfferedCourseId()).isEqualTo(5);
    }

    @Test
    void testGetCourseRegistration() {

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courseRegistrationDto)
                .when()
                .get(BASE_URL + "/19")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();



        List<CourseRegistrationDto> courseRegistrationDtos = response.as(new TypeRef<List<CourseRegistrationDto>>() {});

        assertThat(courseRegistrationDtos.get(4)).isNotNull();
        assertThat(courseRegistrationDtos.get(4).getEnrollmentDate()).isEqualTo(LocalDate.parse("2025-02-02"));
        assertThat(courseRegistrationDtos.get(4).getCost()).isEqualTo(2.5);
        assertThat(courseRegistrationDtos.get(4).getOfferedCourseId()).isEqualTo(5);
    }


    @Test
    void testWithdrawCourseRegistration() {

        given()
                .log().all()
                .when()
                .delete(BASE_URL + "/withdraw/25")
                .then()
                .log().all()
                .contentType(ContentType.JSON) // Ensure response content type
                .statusCode(200)
                .extract()
                .response();
    }
}
