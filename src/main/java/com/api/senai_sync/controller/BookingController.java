package com.api.senai_sync.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.senai_sync.entity.Booking;
import com.api.senai_sync.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        try {
            Booking savedBooking = bookingService.createBooking(booking);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> checkAvailability(@RequestParam Long roomId, @RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime) {
        boolean isAvailable = bookingService.isRoomAvailable(roomId, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }
}
