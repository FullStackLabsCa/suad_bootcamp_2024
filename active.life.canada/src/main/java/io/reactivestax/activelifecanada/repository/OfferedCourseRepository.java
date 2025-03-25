package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.OfferedCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OfferedCourseRepository extends JpaRepository<OfferedCourse, Long>, JpaSpecificationExecutor<OfferedCourse> {
}
