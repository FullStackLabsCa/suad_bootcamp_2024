//package io.reactivestax.active_life_canada.controller;
//
//import io.reactivestax.active_life_canada.dto.PaymentIntentRequest;
//import io.reactivestax.active_life_canada.dto.PaymentMethodRequestDto;
//import io.reactivestax.active_life_canada.service.StripeService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/stripe")
//public class StripeController {
//
//    @Autowired
//    private StripeService stripeService;
//
//    @PostMapping("/create-payment-method")
//    public String createPaymentMethod(@RequestBody PaymentMethodRequestDto request) {
//        return stripeService.createPaymentMethod(
//                request.getCardNumber(),
//                request.getExpMonth(),
//                request.getExpYear(),
//                request.getCvc()
//        );
//    }
//
//    @PostMapping("/create-payment-intent")
//    public String createPaymentIntent(@RequestBody PaymentIntentRequest request) {
//        return stripeService.createPaymentIntent(
//                request.getAmount(),
//                request.getCurrency(),
//                request.getPaymentMethodId()
//        );
//    }
//}
