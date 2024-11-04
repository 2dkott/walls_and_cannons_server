package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PlayerUser_id", nullable = false)
    private PlayerUser playerUserA;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PlayerUser_id", nullable = false)
    public PlayerUser playerUserB;

    @Column
    public int isFinished;

    @OneToOne()
    @JoinColumn(unique = true, nullable = false)
    private PlayerUser winner;

}
