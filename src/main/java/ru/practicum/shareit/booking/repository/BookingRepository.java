package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findBookingsByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartDesc(Long userId, Status status);

    @Query("select b " +
            "from Booking b left join User as us on b.booker.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByBookerIdOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> searchBookingByItemOwnerId(Long id);

    List<Booking> searchBookingByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime time);

    List<Booking> findBookingsByItemOwnerIdOrderByStartDesc(Long id);

    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = ?1 " +
            "and ?2 between b.start and b.end " +
            "order by b.start DESC")
    List<Booking> findCurrentBookingsByItemOwnerIdOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> findBookingsByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time);

    List<Booking> searchBookingByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime time);

    List<Booking> findBookingsByItemIdAndEndIsBeforeOrderByEndDesc(Long id, LocalDateTime time);

    List<Booking> findBookingsByItemIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime time);


}

