package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b " +
            "from Booking b left join User as us on b.booker.id = us.id " +
            "where us.id = :userId " +
            "and :time between b.start and b.end ")
    List<Booking> findByBookerIdAndCurrent(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByBookerIdAndEndIsBefore(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByBookerIdAndStartIsAfter(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long userId, Status status, Sort sort);

    List<Booking> findByBookerId(Long userId, Sort sort);

    List<Booking> findByItemOwnerId(Long id, Sort sort);

    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = :userId " +
            "and :time between b.start and b.end ")
    List<Booking> findByItemOwnerIdAndCurrent(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByItemOwnerIdAndEndIsBefore(Long userId, LocalDateTime time, Sort sort);

    List<Booking> findByItemOwnerIdAndStartIsAfter(Long id, LocalDateTime time, Sort sort);

    List<Booking> findByItemOwnerIdAndStatus(Long id, Status status, Sort sort);

   List<Booking> findByBookerIdAndItemIdAndEndIsBefore(Long id, Long itemId, LocalDateTime time);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :userId AND b.status = 'APPROVED' AND b.end < :now " +
            "ORDER BY b.start DESC")
    Booking findLastBooking(LocalDateTime now, Long userId, Long itemId);

    @Query("SELECT b FROM  Booking b " +
            "WHERE b.item.id = :itemId AND b.item.owner.id = :userId AND b.status = 'APPROVED' AND b.start > :now " +
            "ORDER BY b.start")
    Booking findNextBooking(LocalDateTime now, Long userId, Long itemId);

    List<Booking> findAllByItemInAndStatus(List<Item> items, Status status, Sort sort);
}
