package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class BattleCell {

    public BattleCell() {
        isHit = false;
    }

    public BattleCell(Battle battle, Cell cell) {
        this();
        this.battle = battle;
        this.cell = cell;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "Battle_id", nullable = false)
    private Battle battle;

    @OneToOne()
    @JoinColumn(unique = true, nullable = false)
    private Cell cell;

    @Column
    private boolean isHit;

}
