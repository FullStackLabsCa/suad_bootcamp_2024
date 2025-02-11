package io.reactivestax.active_life_canada.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class StripeService {

    @Value("${stripe.secretkey}")
    private String stripeSecretKey;

    @Autowired
    private RestTemplate restTemplate;

    public String createPaymentMethod(String cardNumber, String expMonth, String expYear, String cvc) {
        String url = "https://api.stripe.com/v1/payment_methods";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + stripeSecretKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = "type=card"
                + "&card[number]=" + cardNumber
                + "&card[exp_month]=" + expMonth
                + "&card[exp_year]=" + expYear
                + "&card[cvc]=" + cvc;

//        String body = "type=card&card[token]=tok_visa";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return exchange.getBody();
    }

    public String createPaymentIntent(long amount, String currency, String paymentMethodId) {
        String url = "https://api.stripe.com/v1/payment_intents";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + stripeSecretKey);
        headers.set("Content-Type", "application/x-www-form-urlencoded");

        String body = "amount=" + amount
                + "&currency=" + currency
                + "&payment_method=" + paymentMethodId
                + "&confirm=true";

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody();
    }


}
