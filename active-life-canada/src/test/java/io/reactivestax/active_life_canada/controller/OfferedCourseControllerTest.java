package io.reactivestax.active_life_canada.controller;

import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseFeeDto;
import io.reactivestax.active_life_canada.enums.FeeType;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.OfferedCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OfferedCourseController.class)
@AutoConfigureMockMvc
class OfferedCourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfferedCourseService offeredCourseService;

    private OfferedCourseDto mockOfferedCourseDto;

    @BeforeEach
    void setUp() {
        OfferedCourseFeeDto feeDto = OfferedCourseFeeDto.builder()
                .feeType(FeeType.RESIDENTIAL)  // Example of FeeType
                .courseFee(100.0)
                .build();

        mockOfferedCourseDto = OfferedCourseDto.builder()
                .barCode("ABC123")
                .startDate(LocalDate.parse("2025-03-01"))
                .endDate(LocalDate.parse("2025-03-08"))
                .numberOfClassesOffered(5)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .isAllDayCourse(false)
                .numberOfSeats(30)
                .totalNumberOfSeats(50)
                .registrationStartDate(LocalDate.now().plusDays(1))
                .courseId(1L)
                .availableForEnrollment(true)
                .offeredCourseFeeDto(List.of(feeDto))
                .build();
    }

    @Test
    void testAddOfferedCourse() throws Exception {
        when(offeredCourseService.save(any(OfferedCourseDto.class)))
                .thenReturn(mockOfferedCourseDto);

        mockMvc.perform(post("/api/v1/offeredCourses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "barCode": "ABC123",
                                    "startDate": "2025-02-01",
                                    "endDate": "2025-02-08",
                                    "numberOfClassesOffered": 5,
                                    "startTime": "09:00:00",
                                    "endTime": "11:00:00",
                                    "isAllDayCourse": false,
                                    "numberOfSeats": 30,
                                    "totalNumberOfSeats": 50,
                                    "registrationStartDate": "2025-02-02",
                                    "courseId": 1,
                                    "availableForEnrollment": true,
                                    "offeredCourseFeeDto": [{"feeType": "RESIDENTIAL", "courseFee": 100.0}]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
//                .andExpect(jsonPath("$.startDate").value("2025-02-01"))
//                .andExpect(jsonPath("$.endDate").value("2025-02-08"))
                .andExpect(jsonPath("$.numberOfSeats").value(30))
                .andExpect(jsonPath("$.availableForEnrollment").value(true));

        verify(offeredCourseService, times(1)).save(any(OfferedCourseDto.class));
    }

    @Test
    void testUpdateOfferedCourse() throws Exception {
        when(offeredCourseService.updateOfferedCourse(anyLong(), any(OfferedCourseDto.class)))
                .thenReturn(mockOfferedCourseDto);

        mockMvc.perform(put("/api/v1/offeredCourses/{offeredCourseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "barCode": "XYZ789",
                                    "startDate": "2025-03-01",
                                    "endDate": "2025-03-08",
                                    "numberOfClassesOffered": 6,
                                    "startTime": "10:00:00",
                                    "endTime": "12:00:00",
                                    "isAllDayCourse": true,
                                    "numberOfSeats": 35,
                                    "totalNumberOfSeats": 60,
                                    "registrationStartDate": "2025-03-02",
                                    "courseId": 2,
                                    "availableForEnrollment": true,
                                    "offeredCourseFeeDto": [{"feeType": "RESIDENTIAL", "courseFee": 150.0}]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.startDate").value("2025-03-01"))
                .andExpect(jsonPath("$.endDate").value("2025-03-08"))
                .andExpect(jsonPath("$.availableForEnrollment").value(true));

        verify(offeredCourseService, times(1)).updateOfferedCourse(anyLong(), any(OfferedCourseDto.class));
    }

    @Test
    void testGetAllOfferedCourses() throws Exception {
        when(offeredCourseService.getAllOfferedCourse())
                .thenReturn(List.of(mockOfferedCourseDto));

        mockMvc.perform(get("/api/v1/offeredCourses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1))
//                .andExpect(jsonPath("$[0].courseName").value("Test Course"))
                .andExpect(jsonPath("$[0].startDate").value("2025-03-01"));

        verify(offeredCourseService, times(1)).getAllOfferedCourse();
    }

    @Test
    void testGetOfferedCourse() throws Exception {
        when(offeredCourseService.getOfferedCourse(anyLong()))
                .thenReturn(mockOfferedCourseDto);

        mockMvc.perform(get("/api/v1/offeredCourses/{offeredCourseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseId").value(1))
//                .andExpect(jsonPath("$.courseName").value("Test Course"))
                .andExpect(jsonPath("$.startDate").value("2025-03-01"));

        verify(offeredCourseService, times(1)).getOfferedCourse(anyLong());


    }

    @Test
    void testDeleteOfferedCourse() throws Exception {
        when(offeredCourseService.deleteOfferedCourse(anyLong()))
                .thenReturn(StatusLevel.SUCCESS);

        mockMvc.perform(delete("/api/v1/offeredCourses/{offeredCourseId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(StatusLevel.SUCCESS.toString()));

        verify(offeredCourseService, times(1)).deleteOfferedCourse(anyLong());
    }

    @Test
    void testSearchOfferedCourses() throws Exception {
        when(offeredCourseService.searchOfferedCourses(anyLong(), anyLong(), anyLong(), anyLong(), anyLong()))
                .thenReturn(List.of(mockOfferedCourseDto));

        mockMvc.perform(get("/api/v1/offeredCourses/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("courseId", "1")
                        .param("categoryId", "1")
                        .param("facilityId", "1")
                        .param("familyMemberId", "1")
                        .param("subCategoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].courseId").value(1));
//                .andExpect(jsonPath("$[0].courseName").value("Test Course"));

        verify(offeredCourseService, times(1)).searchOfferedCourses(anyLong(), anyLong(), anyLong(), anyLong(), anyLong());
    }
}
