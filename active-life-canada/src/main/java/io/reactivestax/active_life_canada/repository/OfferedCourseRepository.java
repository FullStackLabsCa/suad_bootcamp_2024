package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.OfferedCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferedCourseRepository extends JpaRepository<OfferedCourse, Long>, JpaSpecificationExecutor<OfferedCourse> {
}
