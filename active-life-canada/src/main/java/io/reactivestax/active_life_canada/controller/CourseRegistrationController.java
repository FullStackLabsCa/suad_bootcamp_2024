package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import io.reactivestax.active_life_canada.service.CourseRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/courseRegistrations/enrollment")
@Slf4j
public class CourseRegistrationController {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    //Add to the waitlist if the no of seats is full and show the table accordingly..
    @PostMapping
    public ResponseEntity<CourseRegistrationDto> enrollToOfferedCourse(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody CourseRegistrationDto courseRegistrationDto) {
       return ResponseEntity.ok(courseRegistrationService.save(familyMemberId, courseRegistrationDto));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<CourseRegistrationDto>> getEnrolledCourses(@PathVariable Long memberId) {
        return ResponseEntity.ok(courseRegistrationService.findEnrolledCourses(memberId));
    }

    @PutMapping
    public ResponseEntity<CourseRegistrationDto> updateCourseRegistration(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody CourseRegistrationDto courseRegistrationDto) {
        return ResponseEntity.ok(courseRegistrationService.save(familyMemberId, courseRegistrationDto));
    }

    @DeleteMapping("/withdraw/{id}")
    public ResponseEntity<String> withDrawFromOfferedCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseRegistrationService.withdrawRegisteredCourse(id));
    }
}
