package ru.yandex.practicum.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE (b.id = ?1 AND b.booker.id = ?2) OR (b.id = ?1 AND b.item.owner.id = ?3)")
    Optional<Booking> findByIdAndBookerIdOrIdAndItemOwnerId(Long id, Long bookerId, Long ownerId);

    Optional<Booking> findFirstBookingByItemIdAndStartIsAfterAndStatusNotOrderByStartAsc(Long itemId, LocalDateTime end, Status status);

    Optional<Booking> findFirstBookingByItemIdAndStartIsBeforeAndStatusNotOrderByStartDesc(Long itemId, LocalDateTime start, Status status);

    Optional<Booking> findByIdAndItemOwnerId(Long bookingId, Long ownerId);

    Optional<Booking> findFirstByBookerIdAndItemIdAndEndBeforeOrderByEndDesc(Long bookerId, Long itemId, LocalDateTime end);

    //WAITING, REJECTED
    @Query(value = "SELECT b.* FROM bookings as b " +
            "JOIN items as i on i.id = b.item_id " +
            "WHERE b.booker_id = ?1 AND b.status = ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    Collection<Booking> findBookingsByUserAndStatus(Long userId, Status state);

    //ALL
    @Query(value = "SELECT b.* FROM bookings as b " +
            "JOIN items as i on i.id = b.item_id " +
            "WHERE b.booker_id = ?1 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    Collection<Booking> findAllBookingsByUser(Long userId);

    //CURRENT
    @Query(value = "SELECT b.* FROM bookings as b " +
            "JOIN items as i on i.id = b.item_id " +
            "WHERE b.booker_id = ?1 AND ?2 BETWEEN b.start_date AND b.end_date " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    Collection<Booking> findCurrentBookingsByUser(Long userId, LocalDateTime time);

    //PAST
    @Query(value = "SELECT b.* FROM bookings as b " +
            "JOIN items as i on i.id = b.item_id " +
            "WHERE b.booker_id = ?1 AND b.end_date < ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    Collection<Booking> findPastBookingsByUser(Long userId, LocalDateTime time);

    //FUTURE
    @Query(value = "SELECT b.* FROM bookings as b " +
            "JOIN items as i on i.id = b.item_id " +
            "WHERE b.booker_id = ?1 AND b.start_date > ?2 " +
            "ORDER BY b.start_date DESC", nativeQuery = true)
    Collection<Booking> findFutureBookingsByUser(Long userId, LocalDateTime time);

    //WAITING, REJECTED
    Page<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, Status status, Pageable pageable);

    //ALL
    Page<Booking> findByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);

    //CURRENT
    Page<Booking> findByItemOwnerIdAndEndIsAfterAndStartIsBeforeOrderByStartDesc(Long ownerId, LocalDateTime end, LocalDateTime start, Pageable pageable);

    //PAST
    Page<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    //FUTURE
    Page<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long userId, LocalDateTime time, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND " +
            "((b.start < :newEnd AND b.end > :newStart) OR (b.start > :newStart AND b.start < :newEnd))")
    List<Booking> findByItemIdAndStartOrEndBetween(Long itemId, LocalDateTime newStart, LocalDateTime newEnd);

    List<Booking> findByItemIdAndStartBeforeAndEndAfter(Long itemId, LocalDateTime newEnd, LocalDateTime newStart);
}