
package com.api.senai_sync.controller;

import com.api.senai_sync.entity.Room;
import com.api.senai_sync.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Ver todas as salas (public)
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    // Ver uma sala somente (public)
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        return roomService.getRoomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // criar Salas
    // Só o master e o admin podem criar salas
    @PostMapping("/create")
     @PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_ADMIN')")
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        System.out.println("Recebendo sala: " + room);
        try {
            Room createdRoom = roomService.createRoom(room);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);

        } catch (Exception e) {
            System.err.println("Erro ao criar sala: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // update Room
    // Só o master e o admin podem atualizar salas
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        try {
            Room room = roomService.updateRoom(id, updatedRoom);
            return ResponseEntity.ok(room);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Só o master e o admin podem deletar salas
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (roomService.getRoomById(id).isPresent()) {
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}