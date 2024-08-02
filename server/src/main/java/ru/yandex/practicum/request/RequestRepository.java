package ru.yandex.practicum.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequestorIdOrderByCreatedDesc(Long userId);
    List<Request> findAllByOrderByCreatedDesc(Pageable pageable);
}