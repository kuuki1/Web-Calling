package com.web_calling.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "relationship", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"person1Id", "person2Id"})
})
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long person1Id;
    private Long person2Id;
}