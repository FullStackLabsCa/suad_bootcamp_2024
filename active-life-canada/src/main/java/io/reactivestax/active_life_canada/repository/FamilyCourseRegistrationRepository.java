package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyCourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {
}
