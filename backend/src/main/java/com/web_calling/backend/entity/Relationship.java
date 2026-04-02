package com.web_calling.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long person1Id;
    private Long person2Id;
}