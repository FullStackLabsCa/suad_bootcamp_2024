package io.reactivestax.active_life_canada.integration;

import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
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
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FamilyManagementIntegrationTest {

    @LocalServerPort
    private int port;

    private String BASE_URL;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        BASE_URL = "http://localhost:" + port + "/api/v1/family";
    }

    @Test
    void testAddMember() {
        FamilyMemberDto familyMemberDto = FamilyMemberDto.builder()
                .name("John Doe")
                .dob(LocalDate.of(1990, 1, 1))
                .gender("Male")
                .emailId("john.doe@example.com")
                .city("Toronto")
                .country("Canada")
                .preferredContact("email")
                .build();

        Response response = given()
                .log().all()
                .header("X-family-group-id", 3L)
                .contentType(ContentType.JSON)
                .body(familyMemberDto)
                .when()
                .post(BASE_URL + "/members")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        FamilyMemberDto createdMember = response.as(FamilyMemberDto.class);
        assertThat(createdMember).isNotNull();
        assertThat(createdMember.getCity()).isEqualTo("Toronto");
    }

    @Test
    void testGetMember() {
        long memberId = 1L;
        Response response = given()
                .log().all()
                .when()
                .get(BASE_URL + "/members/" + memberId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        FamilyMemberDto retrievedMember = response.as(FamilyMemberDto.class);
        assertThat(retrievedMember).isNotNull();
    }

    @Test
    void testUpdateMember() {
        long memberId = 1L;
        FamilyMemberDto updatedFamilyMemberDto = FamilyMemberDto.builder()
                .name("John Updated")
                .city("Montreal")
                .country("Canada")
                .preferredContact("phone")
                .build();

        Response response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(updatedFamilyMemberDto)
                .when()
                .put(BASE_URL + "/members/" + memberId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        FamilyMemberDto updatedMember = response.as(FamilyMemberDto.class);
        assertThat(updatedMember).isNotNull();
        assertThat(updatedMember.getCity()).isEqualTo("Montreal");
    }

    @Test
    void testDeleteMember() {
        long memberId = 1L;
        Response response = given()
                .log().all()
                .when()
                .delete(BASE_URL + "/members/" + memberId)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        StatusLevel status = response.as(StatusLevel.class);
        assertEquals(StatusLevel.SUCCESS, status);
    }

    @Test
    void testActivateMember() {
        long memberId = 3L;

        Response response = given()
                .log().all()
                .header("X-uuid-token", "aa44c1f0-16c7-40aa-bc3e-789e4d73597f")
                .when()
                .post(BASE_URL + "/members/" + memberId + "/activation")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        StatusLevel status = response.as(StatusLevel.class);
        assertEquals(StatusLevel.SUCCESS, status);
    }
}
