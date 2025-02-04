package io.reactivestax.active_life_canada.service;

import io.reactivestax.active_life_canada.domain.FamilyMember;
import io.reactivestax.active_life_canada.domain.OfferedCourse;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.ems.EmailDTO;
import io.reactivestax.active_life_canada.dto.ems.PhoneDTO;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.OfferedCourseFeeMapper;
import io.reactivestax.active_life_canada.mapper.OfferedCourseMapper;
import io.reactivestax.active_life_canada.repository.OfferedCourseRepository;
import io.reactivestax.active_life_canada.service.ems.EmsNotificationService;
import org.apache.activemq.artemis.core.server.management.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.management.Notification;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class OfferedCourseServiceTest {

    @MockitoBean
    private OfferedCourseRepository offeredCourseRepository;

    @MockitoBean
    private OfferedCourseMapper offeredCourseMapper;

    @MockitoBean
    private OfferedCourseFeeMapper offeredCourseFeeMapper;

    @MockitoBean
    private OfferedCourseSpecification offeredCourseSpecification;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private EmsNotificationService emsNotificationService;

    @Autowired
    private OfferedCourseService offeredCourseService;

    @Test
    void testSaveOfferedCourse() {
        OfferedCourseDto offeredCourseDto = new OfferedCourseDto();
        OfferedCourse offeredCourse = new OfferedCourse();

        when(offeredCourseMapper.toEntity(any())).thenReturn(offeredCourse);
        when(offeredCourseMapper.toDto(any())).thenReturn(offeredCourseDto);
        when(offeredCourseRepository.save(any())).thenReturn(offeredCourse);

        OfferedCourseDto savedCourse = offeredCourseService.save(offeredCourseDto);

        assertThat(savedCourse).isNotNull();
        verify(offeredCourseRepository, times(1)).save(offeredCourse);
    }

    @Test
    void testFindById_WhenExists() {
        OfferedCourse offeredCourse = new OfferedCourse();
        when(offeredCourseRepository.findById(1L)).thenReturn(Optional.of(offeredCourse));

        OfferedCourse foundCourse = offeredCourseService.findById(1L);

        assertThat(foundCourse).isNotNull();
        verify(offeredCourseRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_WhenNotExists() {
        when(offeredCourseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> offeredCourseService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Offered course not found");
    }

    @Test
    void testDeleteOfferedCourse() {
        OfferedCourse offeredCourse = new OfferedCourse();
        when(offeredCourseRepository.findById(1L)).thenReturn(Optional.of(offeredCourse));

        offeredCourseService.deleteOfferedCourse(1L);

        assertThat(offeredCourse.getAvailableForEnrollment()).isFalse();
        verify(offeredCourseRepository, times(1)).save(offeredCourse);
    }


    @Test
    void testSearchOfferedCourses() {
        Long courseId = 1L;
        Long categoryId = 2L;
        Long facilityId = 3L;
        Long familyMemberId = 4L;
        Long subCategoryId = 5L;

        Specification<OfferedCourse> mockSpec = mock(Specification.class);
        when(offeredCourseSpecification.searchOfferedCourses(courseId, categoryId, facilityId, familyMemberId, subCategoryId))
                .thenReturn(mockSpec);

        OfferedCourse offeredCourse = new OfferedCourse(); // Create a mock offered course
        List<OfferedCourse> offeredCourses = Collections.singletonList(offeredCourse); // List of offered courses
        when(offeredCourseRepository.findAll(mockSpec)).thenReturn(offeredCourses);

        OfferedCourseDto offeredCourseDto = new OfferedCourseDto(); // Create a mock DTO
        when(offeredCourseMapper.toDto(offeredCourse)).thenReturn(offeredCourseDto);

        List<OfferedCourseDto> result = offeredCourseService.searchOfferedCourses(
                courseId, categoryId, facilityId, familyMemberId, subCategoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(offeredCourseDto, result.get(0));
    }


}
