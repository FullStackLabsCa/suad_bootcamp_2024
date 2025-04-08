package io.reactivestax.activelifecanada.controller;


import io.reactivestax.activelifecanada.dto.CourseRegistrationDto;
import io.reactivestax.activelifecanada.service.CourseRegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/courseRegistrations/enrollment")
@Slf4j
public class CourseRegistrationController {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    @PostMapping
    public ResponseEntity<CourseRegistrationDto> enrollToOfferedCourse(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody CourseRegistrationDto courseRegistrationDto) {
       return ResponseEntity.ok(courseRegistrationService.save(familyMemberId, courseRegistrationDto));
    }

//    @PostMapping("/all")
//    public ResponseEntity<CourseRegistrationDto> enrollToOfferedCourse(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody List<CourseRegistrationDto> courseRegistrationDtos) {
//        return ResponseEntity.ok(courseRegistrationService.save(familyMemberId, courseRegistrationDto));
//    }

    @GetMapping("/{memberId}")
    public ResponseEntity<List<CourseRegistrationDto>> getEnrolledCourses(@PathVariable Long memberId) {
        return ResponseEntity.ok(courseRegistrationService.findEnrolledCourses(memberId));
    }

    @PutMapping
    public ResponseEntity<CourseRegistrationDto> updateCourseRegistration(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody CourseRegistrationDto courseRegistrationDto) {
        return ResponseEntity.ok(courseRegistrationService.save(familyMemberId, courseRegistrationDto));
    }


    @DeleteMapping("/withdraw/{registrationId}")
    public ResponseEntity<Map<String, String>> withDrawFromOfferedCourse(@PathVariable Long registrationId) {
        Map<String, String> response = Map.of("message", courseRegistrationService.withdrawRegisteredCourse(registrationId));
        return ResponseEntity.ok(response);
    }

}
