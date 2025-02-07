//package io.reactivestax.active_life_canada.dto;
//
//import io.reactivestax.active_life_canada.domain.Cart;
//import io.reactivestax.active_life_canada.domain.OfferedCourse;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class CartItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "cart_item_id")
//    private Long cartItemId;
//
//    @ManyToOne
//    @JoinColumn(name = "cart_id", nullable = false)
//    private Cart cart;
//
//    @ManyToOne
//    @JoinColumn(name = "offered_course_id", nullable = false)
//    private OfferedCourse offeredCourse;
//
//    @Column(name = "quantity")
//    private int quantity;  // Allows duplicate course entries
//}
