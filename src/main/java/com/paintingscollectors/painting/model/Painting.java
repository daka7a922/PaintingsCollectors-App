package com.paintingscollectors.painting.model;

import com.paintingscollectors.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Painting {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String Author;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Style style;

    @ManyToOne
    private User owner;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int votes;



}
