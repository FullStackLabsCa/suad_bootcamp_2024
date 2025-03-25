package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.CourseRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, Long> {


}

