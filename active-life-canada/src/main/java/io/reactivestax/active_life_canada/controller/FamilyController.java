package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.service.FamilyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/family")
@Slf4j
public class FamilyController {

    @Autowired
    private FamilyMemberService familyMemberService;

    @PostMapping("/signup")
    public FamilyMemberDto signUpFamilyMember(@RequestBody FamilyMemberDto familyDto) {
        FamilyMemberDto familyMemberDto = familyMemberService.saveFamilyMemberAndCreateFamilyGroup(familyDto);
        log.debug("Family Member saved: " + familyMemberDto);
        return familyMemberDto;
    }

    @PostMapping("/members")
    public FamilyMemberDto addMembers(@RequestHeader("X-family-group-id") Long familyGroupId, @RequestBody FamilyMemberDto familyDto) {
        return familyMemberService.addFamilyMembers(familyGroupId, familyDto);
    }

    @GetMapping("/members/{memberId}")
    public FamilyMemberDto getMembers(@PathVariable Long memberId) {
        return familyMemberService.findFamilyMemberById(memberId);
    }

    @PutMapping("/members/{memberId}")
    public FamilyMemberDto updateMembers(@RequestHeader("X-family-group-id") Long familyGroupId, @PathVariable Long memberId, @RequestBody FamilyMemberDto familyDto) {
        return familyMemberService.updateFamilyMember(familyGroupId, memberId, familyDto);
    }

    @DeleteMapping("/members/{memberId}")
    public Status deleteMembers(@PathVariable Long memberId) {
        return familyMemberService.deleteFamilyMember(memberId);
    }
}
