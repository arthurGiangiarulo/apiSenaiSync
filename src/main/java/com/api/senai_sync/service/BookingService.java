package com.api.senai_sync.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.senai_sync.entity.Booking;
import com.api.senai_sync.repository.BookingRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Booking> conflictingBookings = bookingRepository.findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(roomId, endTime, startTime);
        return conflictingBookings.isEmpty();
    }

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

    private boolean isWeekday(LocalDateTime dateTime) {
        DayOfWeek day = dateTime.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY;
    }

    private boolean isWithinAllowedHours(LocalDateTime startTime, LocalDateTime endTime) {
        LocalTime start = startTime.toLocalTime();
        LocalTime end = endTime.toLocalTime();

        return (isWithinTimeSlot(start, end, LocalTime.of(8, 0), LocalTime.of(12, 0)) ||
                isWithinTimeSlot(start, end, LocalTime.of(13, 0), LocalTime.of(17, 0)) ||
                isWithinTimeSlot(start, end, LocalTime.of(18, 0), LocalTime.of(22, 0)));
    }

    private boolean isWithinTimeSlot(LocalTime start, LocalTime end, LocalTime slotStart, LocalTime slotEnd) {
        return !start.isBefore(slotStart) && !end.isAfter(slotEnd);
    }
}
