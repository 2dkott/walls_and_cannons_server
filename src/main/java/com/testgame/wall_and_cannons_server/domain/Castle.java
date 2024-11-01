package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Castle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PlayerUser_id", nullable = false)
    private PlayerUser playerUser;

    @OneToOne()
    @JoinColumn(unique = true, nullable = false)
    private Wall wall;

}
