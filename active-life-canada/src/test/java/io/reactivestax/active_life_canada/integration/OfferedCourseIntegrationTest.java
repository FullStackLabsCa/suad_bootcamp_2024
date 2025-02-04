package io.reactivestax.active_life_canada.integration;

import io.reactivestax.active_life_canada.dto.*;
import io.reactivestax.active_life_canada.enums.FeeType;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.restassured.RestAssured;
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
class OfferedCourseIntegrationTest {

    @LocalServerPort
    private int port;

    private String BASE_URL;

    private OfferedCourseDto offeredCourseDto;

    @BeforeAll
     void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        BASE_URL = "http://localhost:" + port + "/api/v1/offeredCourses";

    }

    @Test
     void testOfferedCourseCreation() {

        OfferedCourseFeeDto feeDto = OfferedCourseFeeDto.builder()
                .feeType(FeeType.RESIDENTIAL)  // Example of FeeType
                .courseFee(100.0)
                .build();

        offeredCourseDto = OfferedCourseDto.builder()
                .barCode("ABC123")
                .startDate(LocalDate.parse("2025-03-01"))
                .endDate(LocalDate.parse("2025-03-08"))
                .numberOfClassesOffered(5)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .isAllDayCourse(false)
                .numberOfSeats(30)
                .totalNumberOfSeats(50)
                .registrationStartDate(LocalDate.parse("2025-02-05"))
                .courseId(1L)
                .availableForEnrollment(true)
                .offeredCourseFeeDto(List.of(feeDto))
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(offeredCourseDto)
                .when()
                .post(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        OfferedCourseDto createdOfferedCourse = response.as(OfferedCourseDto.class);

        assertThat(createdOfferedCourse).isNotNull();
        assertThat(createdOfferedCourse.getStartDate()).isEqualTo(LocalDate.parse("2025-03-01"));
        assertThat(createdOfferedCourse.getEndDate()).isEqualTo(LocalDate.parse("2025-03-08"));
        assertThat(createdOfferedCourse.getNumberOfClassesOffered()).isEqualTo(5);
    }

    @Test
    void testUpdateOfferedCourseCreation() {

        OfferedCourseFeeDto feeDto = OfferedCourseFeeDto.builder()
                .feeType(FeeType.RESIDENTIAL)  // Example of FeeType
                .courseFee(100.0)
                .build();

        offeredCourseDto = OfferedCourseDto.builder()
                .barCode("ABC123")
                .startDate(LocalDate.parse("2025-03-01"))
                .endDate(LocalDate.parse("2025-03-08"))
                .numberOfClassesOffered(5)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .isAllDayCourse(true)
                .numberOfSeats(20)
                .totalNumberOfSeats(20)
                .registrationStartDate(LocalDate.parse("2025-02-05"))
                .courseId(7L)
                .availableForEnrollment(true)
                .offeredCourseFeeDto(List.of(feeDto))
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(offeredCourseDto)
                .when()
                .put(BASE_URL + "/6")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        OfferedCourseDto createdOfferedCourse = response.as(OfferedCourseDto.class);

        assertThat(createdOfferedCourse).isNotNull();
        assertThat(createdOfferedCourse.getStartDate()).isEqualTo(LocalDate.parse("2025-03-08"));
        assertThat(createdOfferedCourse.getEndDate()).isEqualTo(LocalDate.parse("2025-03-08"));
        assertThat(createdOfferedCourse.getNumberOfSeats()).isEqualTo(20);
        assertThat(createdOfferedCourse.getIsAllDayCourse()).isTrue();
    }


    @Test
    void testGetOfferedCourses() {
        given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(BASE_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

    }


    @Test
    void testDeleteOfferedCourseById() {
        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(BASE_URL + "/1")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        String statusString = response.as(String.class);
        StatusLevel statusLevel = StatusLevel.valueOf(statusString);

        assertEquals(StatusLevel.SUCCESS, statusLevel);

    }
}
