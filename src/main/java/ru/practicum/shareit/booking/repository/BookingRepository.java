package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
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
            "and :time between b.start and b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findByBookerIdAndCurrentOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long userId, Status status, Pageable pageable);

    List<Booking> findByBookerIdOrderByStartDesc(Long userId, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);

    @Query("select b " +
            "from Booking b left join Item as i on b.item.id = i.id " +
            "left join User as us on i.owner.id = us.id " +
            "where us.id = :userId " +
            "and :time between b.start and b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findByItemOwnerIdAndCurrentOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndIsBeforeOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartIsAfterOrderByStartDesc(Long id, LocalDateTime time, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long id, Status status, Pageable pageable);

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
