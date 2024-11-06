package com.api.senai_sync.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.senai_sync.entity.Booking;
import com.api.senai_sync.repository.BookingRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Criação de uma nova reserva
    public Booking createBooking(Booking booking) {
        if (!isWeekday(booking.getStartTime()) || !isWithinAllowedHours(booking.getStartTime(), booking.getEndTime())) {
            throw new RuntimeException("Reservations can only be made on weekdays and within specified time slots.");
        }

        if (isRoomAvailable(booking.getRoom().getId(), booking.getStartTime(), booking.getEndTime())) {
            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Room is not available for the selected time.");
        }
    }

    // Verificação de disponibilidade
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> conflictingBookings = bookingRepository
                .findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(roomId, endTime, startTime);
        return conflictingBookings.isEmpty();
    }

    // Verificação se o dia é útil (sem fim de semana)
    private boolean isWeekday(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    // Verificação se o horário está dentro do intervalo permitido
    private boolean isWithinAllowedHours(LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        return (isWithinTimeSlot(start, end, LocalTime.of(8, 0), LocalTime.of(12, 0)) ||
                isWithinTimeSlot(start, end, LocalTime.of(13, 0), LocalTime.of(17, 0)) ||
                isWithinTimeSlot(start, end, LocalTime.of(18, 0), LocalTime.of(22, 0)));
    }

    // Método auxiliar para checar se o horário está dentro do slot permitido
    private boolean isWithinTimeSlot(LocalTime start, LocalTime end, LocalTime slotStart, LocalTime slotEnd) {
        return !start.isBefore(slotStart) && !end.isAfter(slotEnd);
    }

    // Listagem de todas as reservas
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // Buscar reserva por ID
    public Optional<Booking> getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId);
    }

    // Atualizar reserva
    public Booking updateBooking(Long bookingId, Booking updatedBooking) {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);

        if (existingBooking.isPresent()) {
            Booking booking = existingBooking.get();
            booking.setStartTime(updatedBooking.getStartTime());
            booking.setEndTime(updatedBooking.getEndTime());
            booking.setRoom(updatedBooking.getRoom());
            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Booking not found");
        }
    }

    // Deletar reserva
    public void deleteBooking(Long bookingId) {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);

        if (existingBooking.isPresent()) {
            bookingRepository.deleteById(bookingId);
        } else {
            throw new RuntimeException("Booking not found");
        }
    }
}
