package com.web_calling.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}