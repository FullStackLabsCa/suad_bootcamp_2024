package io.reactivestax.active_life_canada.controller;


import io.reactivestax.active_life_canada.dto.CartDto;
import io.reactivestax.active_life_canada.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/cart")
@Slf4j
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<CartDto> save(@RequestHeader("X-family-member-id") Long familyMemberId, @RequestBody CartDto cartDto) {
       return ResponseEntity.ok(cartService.save(familyMemberId, cartDto));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getEnrolledCourses(@PathVariable Long cartId) {
        return ResponseEntity.ok(cartService.getCart(cartId));
    }

    @PutMapping
    public ResponseEntity<CartDto> updateCart(@RequestBody CartDto cartDto) {
        return ResponseEntity.ok(cartService.updateCart(cartDto));
    }

    @DeleteMapping("/deleteCart/{cartId}")
    public ResponseEntity<String> withDrawFromOfferedCourse(@PathVariable Long cartId) {
       return  ResponseEntity.ok(cartService.deleteCart(cartId));
    }

}
