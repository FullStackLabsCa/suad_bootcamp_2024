package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
