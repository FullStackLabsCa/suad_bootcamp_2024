package io.reactivestax.active_life_canada.repository;

import io.reactivestax.active_life_canada.domain.Category;
import io.reactivestax.active_life_canada.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
