package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class BattleRoundCell {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    public BattleRoundCell() {
        isHit = false;
    }

    public BattleRoundCell(BattleRound battleRound, Cell cell) {
        this();
        this.battleRound = battleRound;
        this.cell = cell;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "battle_round_id", nullable = false)
    public BattleRound battleRound;

    @OneToOne()
    @JoinColumn(unique = true, nullable = false)
    private Cell cell;

    @Column
    private boolean isHit;

}
