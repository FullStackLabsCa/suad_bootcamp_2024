package io.reactivestax.activelifecanada.repository;

import io.reactivestax.activelifecanada.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
