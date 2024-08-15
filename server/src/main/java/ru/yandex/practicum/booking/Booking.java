package ru.yandex.practicum.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.item.Item;
import ru.yandex.practicum.user.User;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;
    @Enumerated(EnumType.ORDINAL)
    @JsonIgnore
    private Status status;
}