package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.FamilyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/family")
@Slf4j
public class FamilyManagementController {

    @Autowired
    private FamilyMemberService familyMemberService;


    @PostMapping("/members")
    public ResponseEntity<FamilyMemberDto> addMembers(@RequestHeader("X-family-group-id") Long familyGroupId, @RequestBody FamilyMemberDto familyDto) {
        return ResponseEntity.ok(familyMemberService.addFamilyMembers(familyGroupId, familyDto));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<FamilyMemberDto> getMembers(@PathVariable Long memberId) {
        return ResponseEntity.ok(familyMemberService.findFamilyMemberDtoById(memberId));
    }

    @PutMapping("/members/{memberId}")
    public ResponseEntity<FamilyMemberDto> updateMembers(@PathVariable Long memberId, @RequestBody FamilyMemberDto familyDto) {
        return ResponseEntity.ok(familyMemberService.updateFamilyMember( memberId, familyDto));
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
