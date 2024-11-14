package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class PlayerParty {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "PlayerUser_id", nullable = false)
    private PlayerUser playerUser;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "Battle_id", nullable = false)
    private Battle battle;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "battle_round_id", nullable = false)
    private BattleRound battleRound;

    private boolean isConfirmed;

    @Override
    public String toString() {
        return "Party [id=" + id + ", playerUser=" + playerUser + "]";
    }
}
