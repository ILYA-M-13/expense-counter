package ru.mayorov.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false, columnDefinition = "UUID")
    private UUID uuid = UUID.randomUUID();

    @Column(nullable = false)
    private Long telegramId;

    @Column
    private String username;

}