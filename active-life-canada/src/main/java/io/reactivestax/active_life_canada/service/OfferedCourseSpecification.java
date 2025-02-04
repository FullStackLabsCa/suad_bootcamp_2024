package io.reactivestax.active_life_canada.service;

import io.reactivestax.active_life_canada.domain.OfferedCourse;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfferedCourseSpecification {

    public Specification<OfferedCourse> searchOfferedCourses(Long courseId, Long categoryId, Long facilityId, Long familyMemberId, Long subCategoryId) {
        return (Root<OfferedCourse> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (courseId != null) {
                predicates.add(cb.equal(root.get("course").get("id"), courseId));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("course").get("category").get("id"), categoryId));
            }
            if (facilityId != null) {
                predicates.add(cb.equal(root.get("facility").get("id"), facilityId));
            }
//            if (familyMemberId != null) {
//                predicates.add(cb.equal(root.get("familyCourseRegistrations").get("familyMember").get("id"), familyMemberId));
//            }
            if (subCategoryId != null) {
                predicates.add(cb.equal(root.get("course").get("subCategory").get("id"), subCategoryId));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
