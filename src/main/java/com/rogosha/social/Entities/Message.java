package com.rogosha.social.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dialog")
    private Dialog dialog;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime timeStamp;

    public Message(String content, LocalDateTime timeStamp) {
        this.content = content;
        this.timeStamp = timeStamp;
    }
}
