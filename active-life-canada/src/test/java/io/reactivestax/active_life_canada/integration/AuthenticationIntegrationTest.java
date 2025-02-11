package io.reactivestax.active_life_canada.integration;

import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.LoginRequestDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationIntegrationTest {

    @LocalServerPort
    private int port;

    private String BASE_URL;

    @BeforeAll
     void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        BASE_URL = "http://localhost:" + port + "/api/v1/authentication";
    }

    @Test
     void testSignup() {

        SignUpDto signUpDto = SignUpDto.builder()
                .city("Toronto")
                .country("Canada")
                .familyPin("2894")
                .homePhone("4373325652")
                .preferredContact("sms")
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(signUpDto)
                .when()
                .post(BASE_URL + "/signup")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        FamilyMemberDto createdMember = response.as(FamilyMemberDto.class);

        assertThat(createdMember).isNotNull();
        assertThat(createdMember.getCity()).isNotNull();
        assertThat(createdMember.getCountry()).isEqualTo("Canada");
        assertThat(createdMember.getPreferredContact()).isEqualTo("sms");
    }

    @Test
    void testLogin() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .familyMemberId(3L)
                .familyPin("2894")
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
                .when()
                .post(BASE_URL + "/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        String statusString = response.as(String.class);
        StatusLevel statusLevel = StatusLevel.valueOf(statusString); // Convert to Enum

        assertEquals(StatusLevel.SUCCESS, statusLevel);
    }


    @Test
    void testLogin2faForInvalidOtp() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .familyMemberId(9L)
                .familyPin("1234")
                .build();

        given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(loginRequestDto)
                .when()
                .post(BASE_URL + "/login/2fa")
                .then()
                .log().all()
                .statusCode(500)
                .extract()
                .response();
    }
}
