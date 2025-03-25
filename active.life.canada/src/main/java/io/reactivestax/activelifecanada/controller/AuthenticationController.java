package io.reactivestax.activelifecanada.controller;


import io.reactivestax.activelifecanada.dto.FamilyMemberDto;
import io.reactivestax.activelifecanada.dto.LoginRequestDto;
import io.reactivestax.activelifecanada.dto.SignUpDto;
import io.reactivestax.activelifecanada.enums.StatusLevel;
import io.reactivestax.activelifecanada.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    public ResponseEntity<Map<String, String>> loginFamilyMember2fa(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.login2FA(loginRequestDto));
    }

}
