package com.api.senai_sync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.senai_sync.entity.Room;
import com.api.senai_sync.repository.RoomRepository;
import com.api.senai_sync.exception.RoomCreationException;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Optional<Room> getRoomById(Long id) {
        return roomRepository.findById(id);
    }

    public Room createRoom(Room room) {
        try {
            return roomRepository.save(room);

        } catch (Exception e) {
           throw new RoomCreationException("Erro ao criar sala: " + e.getMessage(), e);
        }
    }

    public Room updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id).map(room -> {
            room.setName(updatedRoom.getName());
            room.setSeatAmount(updatedRoom.getSeatAmount());
            return roomRepository.save(room);
        }).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}