package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;
import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedAsc(Long userId);

    @Query(value = "SELECT r " +
            "FROM ItemRequest r " +
            "WHERE r.requestor.id <> :userId")
    List<ItemRequest> findAllByRequestorIdNotLike(Long userId, Pageable pageable);
}
