package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.OfferedCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/offeredCourses")
@Slf4j
public class OfferedCourseController {

    @Autowired
    private OfferedCourseService offeredCourseService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OfferedCourseDto> addOfferedCourse(@RequestBody OfferedCourseDto offeredCourseDto) {
       return ResponseEntity.ok(offeredCourseService.save(offeredCourseDto));
    }


    @PutMapping("/{offeredCourseId}")
    public ResponseEntity<OfferedCourseDto> updateOfferedCourse(@PathVariable Long offeredCourseId, @RequestBody OfferedCourseDto offeredCourseDto) {
        return ResponseEntity.ok(offeredCourseService.updateOfferedCourse(offeredCourseId, offeredCourseDto));
    }

    @GetMapping
    public ResponseEntity<List<OfferedCourseDto>> getAllOfferedCourse() {
        return ResponseEntity.ok(offeredCourseService.getAllOfferedCourse());
    }

    @GetMapping("/{offeredCourseId}")
    public ResponseEntity<OfferedCourseDto> getOfferedCourse(@PathVariable Long offeredCourseId) {
        return ResponseEntity.ok(offeredCourseService.getOfferedCourse(offeredCourseId));
    }

    @DeleteMapping("{offeredCourseId}")
    public ResponseEntity<StatusLevel> deleteMembers(@PathVariable Long offeredCourseId) {
        return ResponseEntity.ok(offeredCourseService.deleteOfferedCourse(offeredCourseId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<OfferedCourseDto>> searchOfferedCourses(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long facilityId,
            @RequestParam(required = false) Long familyMemberId,
            @RequestParam(required = false) Long subCategoryId) {
        return ResponseEntity.ok(offeredCourseService.searchOfferedCourses(courseId, categoryId, facilityId, familyMemberId, subCategoryId));
    }
}
