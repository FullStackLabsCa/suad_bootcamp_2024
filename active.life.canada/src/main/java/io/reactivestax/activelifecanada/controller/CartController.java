package io.reactivestax.activelifecanada.controller;


import io.reactivestax.activelifecanada.dto.CartDto;
import io.reactivestax.activelifecanada.dto.CourseRegistrationDto;
import io.reactivestax.activelifecanada.service.CartService;
import jakarta.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/cart")
@Slf4j
public class CartController extends HttpServlet {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<List<CourseRegistrationDto>> save(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody CartDto cartDto) {
       return ResponseEntity.ok(cartService.saveAndEnrollToCourse(familyMemberId, cartDto));
    }

    @GetMapping("/{familyMemberId}")
    public ResponseEntity<CartDto> getEnrolledCourses(@PathVariable Long familyMemberId) {
        return ResponseEntity.ok(cartService.getCart(familyMemberId));
    }

    @PutMapping
    public ResponseEntity<CartDto> updateCart(@RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.updateCart(cartDto));
    }

    @DeleteMapping("/deleteCart/{cartId}")
    public ResponseEntity<String> withDrawFromOfferedCourse(@PathVariable Long familyMemberId) {
       return  ResponseEntity.ok(cartService.deleteCart(familyMemberId));
    }

}
