package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "battle_round")
public class BattleRound {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column
    private int roundNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Battle_id", nullable = false)
    private Battle battle;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "battleRound")
    private List<RoundCell> roundCells;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "round")
    private List<PlayerParty> playerParties;

    @Column
    private boolean isActive;
}
