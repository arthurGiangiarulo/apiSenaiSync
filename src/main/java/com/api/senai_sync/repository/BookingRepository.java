package com.api.senai_sync.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.api.senai_sync.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomIdAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(Long roomId, LocalDateTime endTime,
            LocalDateTime startTime);

}
