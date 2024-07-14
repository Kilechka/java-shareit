package ru.yandex.practicum.request;

import jakarta.persistence.*;
import lombok.Data;
import ru.yandex.practicum.user.User;

@Data
@Entity
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;
}