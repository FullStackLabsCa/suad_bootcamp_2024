package io.reactivestax.active_life_canada.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivestax.active_life_canada.domain.*;
import io.reactivestax.active_life_canada.dto.CartDto;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.mapper.CartMapper;
import io.reactivestax.active_life_canada.mapper.OfferedCourseMapper;
import io.reactivestax.active_life_canada.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OfferedCourseMapper offeredCourseMapper;

    @Autowired
    private FamilyMemberService familyMemberService;

    @Autowired
    private OfferedCourseService offeredCourseService;

    @Autowired
    private RedisTemplate<String, Cart> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CART_KEY_PREFIX = "cart:";

    //    @Cacheable(value = "cartCache")
    public CartDto save(Long familyMemberId, CartDto cartDto) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);
        Cart entity = toCartEntity(cartDto);
        entity.setFamilyMember(familyMember);

        /*Currently allowing to add the duplicate offered course. Later check the timing and allow the duplicate addition of course accordingly*/
        Cart cart = cartRepository.save(entity);
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + cart.getCartId(), cart, 30, TimeUnit.MINUTES);
        return cartMapper.toDto(entity);
    }


    public CartDto getCart(Long cartId) {
        String redisKey = CART_KEY_PREFIX + cartId;
        Cart cachedCart =  redisTemplate.opsForValue().get(redisKey);
        if (cachedCart != null) {
//            Cart cart = objectMapper.convertValue(cachedCart, Cart.class);
            return cartMapper.toDto(cachedCart);
        }
        Cart cart = findById(cartId);
        redisTemplate.opsForValue().set(redisKey, cart, 30, TimeUnit.MINUTES);
        return cartMapper.toDto(cart);
    }

    public CartDto updateCart(CartDto cartDto) {
        Cart cart = findById(cartDto.getCartId());
        cart.setOfferedCourseIds(cartDto.getOfferedCourseIds());
        cartRepository.save(cart);
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + cart.getCartId(), cart, 30, TimeUnit.MINUTES);
        return cartMapper.toDto(cart);
    }

    public Cart findById(Long cartId) {
        return cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart is not active"));
    }

    public String deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));
        cart.setIsActive(false);
        cartRepository.save(cart);
        redisTemplate.delete(CART_KEY_PREFIX + cartId);
        return "Successfully deleted";
    }

    public Cart toCartEntity(CartDto cartDto) {
        Cart entity = cartMapper.toEntity(cartDto);
        entity.setIsActive(true);
        entity.setCreatedTimeStamp(LocalDateTime.now());
        return entity;
    }

//   public CartDto toCartDto(Cart cart){
//       CartDto cartDto = cartMapper.toDto(cart);
////       List<Long> offeredCourseIds = cart.getOfferedCourses().stream().map(OfferedCourse::getOfferedCourseId).toList();
//       cartDto.setOfferedCourseIds(cart.getOfferedCourseIds());
//       return cartDto;
//   }


}
