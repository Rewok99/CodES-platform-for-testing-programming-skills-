package com.rewok.codestudentstest.models;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "tasks")
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String text;
    private String pattern;
    private String testClass;
    private String theory;

}


