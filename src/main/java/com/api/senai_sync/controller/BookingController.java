package com.api.senai_sync.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.api.senai_sync.entity.Booking;
import com.api.senai_sync.service.BookingService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Endpoint público para listar todas as reservas - acessível para todos
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Endpoint público para buscar uma reserva por ID - acessível para todos
    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long bookingId) {
        Optional<Booking> booking = bookingService.getBookingById(bookingId);
        return booking.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para criar uma reserva
    @PreAuthorize("hasAuthority('ROLE_COLABORADOR' , 'ROLE_MASTER' , 'ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        String currentUsername = getCurrentUsername();
        booking.setCreatedBy(currentUsername); // Define o criador da reserva
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    // Endpoint para atualizar uma reserva
    @PreAuthorize("hasAuthority('ROLE_COLABORADOR' , 'ROLE_MASTER' , 'ROLE_ADMIN')")
    @PutMapping("/{bookingId}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long bookingId, @RequestBody Booking booking) {
        Optional<Booking> existingBooking = bookingService.getBookingById(bookingId);

        if (existingBooking.isPresent() && isBookingOwner(existingBooking.get())) {
            Booking updatedBooking = bookingService.updateBooking(bookingId, booking);
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    // Endpoint para deletar uma reserva
    @PreAuthorize("hasAuthority('ROLE_COLABORADOR') or hasAuthority('ROLE_MASTER') or hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        Optional<Booking> existingBooking = bookingService.getBookingById(bookingId);

        if (existingBooking.isPresent()) {
            // Verifica se o usuário logado é o criador ou se ele tem um dos papéis
            // autorizados
            if (isBookingOwner(existingBooking.get()) || isUserAdminOrMaster()) {
                bookingService.deleteBooking(bookingId);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Caso não seja o dono ou admin/master
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Caso a reserva não exista
    }

    // Método auxiliar para verificar se o usuário autenticado é o criador da
    // reserva
    private boolean isBookingOwner(Booking booking) {
        String currentUsername = getCurrentUsername();
        return booking.getCreatedBy().equals(currentUsername);
    }

    // Método auxiliar para obter o nome de usuário do usuário autenticado
    private String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    private boolean isUserAdminOrMaster() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_MASTER")
                        || grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}