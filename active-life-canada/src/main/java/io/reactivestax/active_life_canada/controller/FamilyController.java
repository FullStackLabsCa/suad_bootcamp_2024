package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.service.FamilyMemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
