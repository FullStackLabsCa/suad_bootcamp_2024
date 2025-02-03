package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.FamilyMemberDto;
import io.reactivestax.active_life_canada.dto.LoginRequestDto;
import io.reactivestax.active_life_canada.dto.SignUpDto;
import io.reactivestax.active_life_canada.enums.Status;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/authentication")
@Slf4j
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<FamilyMemberDto> signUpFamilyMember(@RequestBody SignUpDto signUpDto) {
       return ResponseEntity.ok(authenticationService.signUpAndCreateFamilyGroup(signUpDto));
    }

    @PostMapping("/login")
    public ResponseEntity<StatusLevel> loginFamilyMember(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.loginFamilyMember(loginRequestDto));
    }

    @PostMapping("/login/2fa")
    public ResponseEntity<Status> loginFamilyMember2fa(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.login2FA(loginRequestDto));
    }

}
