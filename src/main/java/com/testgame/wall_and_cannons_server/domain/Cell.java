package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
public class Cell {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "wall_id", nullable = false)
    public Wall wall;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "cell")
    private List<RoundCell> roundCellList;

    @Column
    public int cellRow;

    @Column
    public int cellColumn;

    @Column
    public boolean isHit;
}
