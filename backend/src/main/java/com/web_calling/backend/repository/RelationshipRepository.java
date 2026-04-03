package com.web_calling.backend.repository;

import com.web_calling.backend.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationshipRepository extends JpaRepository<Relationship, Long> {
    boolean existsByPerson1IdAndPerson2Id(Long p1, Long p2);
}