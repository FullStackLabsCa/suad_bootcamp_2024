package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
