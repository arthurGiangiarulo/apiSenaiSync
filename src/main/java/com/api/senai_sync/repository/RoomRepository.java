package com.api.senai_sync.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.senai_sync.entity.Room;



public interface RoomRepository extends JpaRepository<Room, Long> {
}