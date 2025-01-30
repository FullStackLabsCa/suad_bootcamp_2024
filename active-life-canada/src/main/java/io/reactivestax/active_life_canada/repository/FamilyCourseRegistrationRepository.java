package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.Category;
import io.reactivestax.active_life_canada.domain.FamilyCourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyCourseRegistrationRepository extends JpaRepository<FamilyCourseRegistration, Long> {
}
