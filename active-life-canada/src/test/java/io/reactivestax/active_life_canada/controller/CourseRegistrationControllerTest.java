package io.reactivestax.active_life_canada.controller;

import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.CourseRegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CourseRegistrationController.class)
@AutoConfigureMockMvc
class CourseRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseRegistrationService courseRegistrationService;

    private CourseRegistrationDto mockCourseRegistration;

    @BeforeEach
    void setUp() {
        mockCourseRegistration = CourseRegistrationDto.builder()
                .familyCourseRegistrationId(2L)
                .cost(10.5)
                .enrollmentDate(LocalDate.now())
                .offeredCourseId(5L)
                .enrollmentActor("Parent")
                .build();
    }

    @Test
    void testCourseRegistration() throws Exception {
        when(courseRegistrationService.save(anyLong(), any(CourseRegistrationDto.class)))
                .thenReturn(mockCourseRegistration);

        mockMvc.perform(post("/api/v1/courseRegistrations/enrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-family-member-id", "1")  // Required Header
                        .content("""
                                {
                                   "cost": 10.5,
                                      "enrollmentDate": "2025-02-02",
                                      "isWithdraw": false,
                                      "withdrawCredits": 0.0,
                                      "enrollmentActor": "Parent",
                                      "offeredCourseId": 5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost").value("10.5"))
                .andExpect(jsonPath("$.offeredCourseId").value("5"))
                .andExpect(jsonPath("$.enrollmentActor").value("Parent"));

        verify(courseRegistrationService, times(1)).save(anyLong(), any(CourseRegistrationDto.class));
    }

    @Test
    void testGetEnrolledCourses() throws Exception {
        when(courseRegistrationService.findEnrolledCourses(anyLong()))
                .thenReturn(List.of(mockCourseRegistration));

        mockMvc.perform(get("/api/v1/courseRegistrations/enrollment/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cost").value("10.5"))
                .andExpect(jsonPath("$[0].offeredCourseId").value("5"))
                .andExpect(jsonPath("$[0].enrollmentActor").value("Parent"));

        verify(courseRegistrationService, times(1)).findEnrolledCourses(anyLong());
    }

    @Test
    void testUpdateCourseRegistration() throws Exception {
        when(courseRegistrationService.save(anyLong(), any(CourseRegistrationDto.class)))
                .thenReturn(mockCourseRegistration);

        mockMvc.perform(put("/api/v1/courseRegistrations/enrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-family-member-id", "1")  // Required Header
                        .content("""
                                {
                                "familyCourseRegistrationId": 2,
                                   "cost": 10.5,
                                      "enrollmentDate": "2025-02-02",
                                      "isWithdraw": false,
                                      "withdrawCredits": 0.0,
                                      "enrollmentActor": "Parent",
                                      "offeredCourseId": 5
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.familyCourseRegistrationId").value("2"))
                .andExpect(jsonPath("$.cost").value("10.5"))
                .andExpect(jsonPath("$.offeredCourseId").value("5"))
                .andExpect(jsonPath("$.enrollmentActor").value("Parent"));

        verify(courseRegistrationService, times(1)).save(anyLong(), any(CourseRegistrationDto.class));
    }

    @Test
    void testDeleteCourseRegistration() throws Exception {
        when(courseRegistrationService.withdrawRegisteredCourse(anyLong()))
                .thenReturn(StatusLevel.SUCCESS.toString());

        mockMvc.perform(delete("/api/v1/courseRegistrations/enrollment/withdraw/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(courseRegistrationService, times(1)).withdrawRegisteredCourse(anyLong());
    }

}