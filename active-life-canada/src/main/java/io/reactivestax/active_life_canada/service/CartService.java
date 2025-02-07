package io.reactivestax.active_life_canada.service;


import io.reactivestax.active_life_canada.domain.*;
import io.reactivestax.active_life_canada.dto.CartDto;
import io.reactivestax.active_life_canada.dto.CourseRegistrationDto;
import io.reactivestax.active_life_canada.dto.OfferedCourseDto;
import io.reactivestax.active_life_canada.enums.StatusLevel;
import io.reactivestax.active_life_canada.exception.ResourceNotFoundException;
import io.reactivestax.active_life_canada.exception.UnauthorizedException;
import io.reactivestax.active_life_canada.mapper.CartMapper;
import io.reactivestax.active_life_canada.mapper.CourseRegistrationMapper;
import io.reactivestax.active_life_canada.mapper.OfferedCourseMapper;
import io.reactivestax.active_life_canada.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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

    @Cacheable(value = "cartCache")
    public CartDto save(Long familyMemberId, CartDto cartDto) {
        FamilyMember familyMember = familyMemberService.findFamilyMemberById(familyMemberId);
        Cart entity = toCartEntity(cartDto);
        entity.setFamilyMember(familyMember);

        /*Currently allowing to add the duplicate offered course. Later check the timing and allow the duplicate addition of course accordingly*/
        cartRepository.save(entity);
        return cartMapper.toDto(entity);
    }


    @Cacheable(value = "cartCache", key = "#cartId")
    public CartDto getCart(Long cartId) {
        Cart cart = findById(cartId);
        return cartMapper.toDto(cart);
    }


    @CachePut(value = "cartCache", key = "#cartDto.cartId")
    public CartDto updateCart(CartDto cartDto) {
       Cart cart = findById(cartDto.getCartId());
       cart.setOfferedCourseIds(cartDto.getOfferedCourseIds());
       return cartMapper.toDto(cart);
    }

    public Cart findById(Long cartId){
        return cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart is not active"));
    }

    @CacheEvict(value = "cartCache", key = "#cartId")
    public String deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart Not found"));
        cart.setIsActive(false);
        cartRepository.save(cart);
        return "Successfully deleted";
    }

   public Cart toCartEntity(CartDto cartDto){
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
