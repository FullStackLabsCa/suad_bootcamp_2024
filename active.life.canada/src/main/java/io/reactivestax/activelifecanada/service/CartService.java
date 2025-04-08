package io.reactivestax.activelifecanada.service;


import io.reactivestax.activelifecanada.dto.CartDto;
import io.reactivestax.activelifecanada.dto.CourseRegistrationDto;
import io.reactivestax.activelifecanada.dto.OfferedCourseDto;
import io.reactivestax.activelifecanada.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class CartService {

    @Autowired
    private RedisTemplate<String, CartDto> redisTemplate;

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    @Autowired
    private FamilyMemberService familyMemberService;

    private static final String CART_KEY_PREFIX = "cart:";


    @Transactional
    public List<CourseRegistrationDto> saveAndEnrollToCourse(Long familyMemberId, CartDto cartDto) {
        cartDto.setCartId(CART_KEY_PREFIX + familyMemberId);
        cartDto.setFamilyMemberId(familyMemberId);
        cartDto.setIsActive(true);
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + familyMemberId, cartDto, 30, TimeUnit.MINUTES);
        String actorName = familyMemberService.findFamilyMemberById(familyMemberId).getName();
        List<CourseRegistrationDto> courseRegistrationDtos = cartDto.getCartItems().stream().map(offeredCourseDto -> toDto(actorName, offeredCourseDto)).toList();
        return courseRegistrationService.bulkRegistrationToOfferedCourse(familyMemberId,  courseRegistrationDtos);
    }


    public CartDto getCart(Long familyMemberId) {
        String redisKey = CART_KEY_PREFIX + familyMemberId;
        CartDto cartDto = redisTemplate.opsForValue().get(redisKey);
        return Optional.ofNullable(cartDto).orElseThrow(() -> new ResourceNotFoundException("Cart of the family member id " + familyMemberId + " is not found"));
    }

    public CartDto updateCart(CartDto cartDto) {
        CartDto cart = getCart(cartDto.getFamilyMemberId());
        redisTemplate.opsForValue().set(CART_KEY_PREFIX + cartDto.getCartId(), cart, 30, TimeUnit.MINUTES);
        return cart;
    }

    public String deleteCart(Long familyMemberId) {
        redisTemplate.delete(CART_KEY_PREFIX + familyMemberId);
        return "Successfully deleted";
    }

    public CourseRegistrationDto toDto(String actorName, OfferedCourseDto offeredCourseDto){
        CourseRegistrationDto courseRegistrationDto = new CourseRegistrationDto();
        courseRegistrationDto.setOfferedCourseId(offeredCourseDto.getOfferedCourseId());
        if (offeredCourseDto.getOfferedCourseFeeDto() != null && !offeredCourseDto.getOfferedCourseFeeDto().isEmpty()) {
            courseRegistrationDto.setCost(offeredCourseDto.getOfferedCourseFeeDto().get(0).getCourseFee());
        } else {
            courseRegistrationDto.setCost(null); // or default value like 0.0 if needed
        }
        courseRegistrationDto.setCost(offeredCourseDto.getOfferedCourseFeeDto().get(0).getCourseFee());
        courseRegistrationDto.setEnrollmentDate(LocalDate.now());
        courseRegistrationDto.setEnrollmentActor(actorName);
        return courseRegistrationDto;
    }
}
