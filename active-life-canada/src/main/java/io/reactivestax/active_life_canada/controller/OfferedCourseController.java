package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.LoginRequestDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.FamilyMemberService;
import io.reactivestax.active_life_canada.service.OfferedCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/offeredCourses")
@Slf4j
public class OfferedCorseController {

    @Autowired
    private OfferedCourseService offeredCourseService;

    @PostMapping()
    public ResponseEntity<OfferedCourseDto> addOfferedCourse(@RequestBody OfferedCourseDto offeredCourseDto) {
       return ResponseEntity.ok(offeredCourseService.save(offeredCourseDto));
    }

    @PostMapping()
    public ResponseEntity<StatusLevel> updateOfferedCourse(@RequestBody OfferedCourseDto offeredCourseDto) {
        return ResponseEntity.ok(offeredCourseDto.loginFamilyMember(offeredCourseDto));
    }

    @PostMapping("/login/2fa")
    public ResponseEntity<Status> loginFamilyMember2fa(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(familyMemberService.login2FA(loginRequestDto));
    }

    @PostMapping("/members")
    public ResponseEntity<FamilyMemberDto> addMembers(@RequestHeader("X-family-group-id") Long familyGroupId, @RequestBody FamilyMemberDto familyDto) {
        return ResponseEntity.ok(familyMemberService.addFamilyMembers(familyGroupId, familyDto));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<FamilyMemberDto> getMembers(@PathVariable Long memberId) {
        return ResponseEntity.ok(familyMemberService.findFamilyMemberDtoById(memberId));
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<FamilyMemberDto> updateMembers(@RequestHeader("X-family-group-id") Long familyGroupId, @PathVariable Long memberId, @RequestBody FamilyMemberDto familyDto) {
        return ResponseEntity.ok(familyMemberService.updateFamilyMember(familyGroupId, memberId, familyDto));
    }

    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<StatusLevel> deleteMembers(@PathVariable Long memberId) {
        return ResponseEntity.ok(familyMemberService.deleteFamilyMember(memberId));
    }

    @PostMapping("/members/{memberId}/activation")
    public ResponseEntity<StatusLevel> activateMember(@PathVariable Long memberId, @RequestHeader("X-uuid-token") String uuid) {
        return ResponseEntity.ok(familyMemberService.activateMemberByUuid(memberId, UUID.fromString(uuid)));
    }
}
