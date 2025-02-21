package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.dto.CartDto;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, CartDto> redisTemplate;

    private static final String CART_KEY_PREFIX = "cart:";


    public CartDto save(Long familyMemberId, CartDto cartDto) {
        cartDto.setCartId(UUID.randomUUID());
        cartDto.setFamilyMemberId(familyMemberId);
        cartDto.setIsActive(true);
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + cartDto.getCartId(), cartDto, 30, TimeUnit.MINUTES);
        return cartDto;
    }

    public CartDto getCart(UUID cartId) {
        String redisKey = CART_KEY_PREFIX + cartId;
        CartDto cartDto = redisTemplate.opsForValue().get(redisKey);
        return Optional.ofNullable(cartDto).orElseThrow(() -> new ResourceNotFoundException("Cart with the cartId " + cartId + " is not found"));
    }

    public CartDto updateCart(CartDto cartDto) {
        CartDto cart = getCart(cartDto.getCartId());
        cart.setOfferedCourseIds(cartDto.getOfferedCourseIds());
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + cartDto.getCartId(), cart, 30, TimeUnit.MINUTES);
        return cart;
    }

    public String deleteCart(UUID cartId) {
        redisTemplate.delete(CART_KEY_PREFIX + cartId);
        return "Successfully deleted";
    }
}
