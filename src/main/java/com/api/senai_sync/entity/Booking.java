package com.api.senai_sync.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Room room;

    @Column (nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false, updatable = false) // Não pode ser atualizado apos a criaçao
    private String createdBy;

    @Column(nullable = false, updatable = false) // Não pode ser atualizado apos a criaçao
    private LocalDateTime createdAt;

    // Construtor para criar Booking com createdBy e createdAt
    public Booking(String createdBy, LocalDateTime startTime, LocalDateTime endTime, Room room) {
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now(); // Define a data de criação automaticamente
        this.startTime = startTime;
        this.endTime = endTime;
        this.room = room;
    }
}
