package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
public class Wall {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column
    private int wallRows;

    @Column
    private int wallColumns;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wall")
    private List<Cell> cellList;

    public boolean isShortRow(int row) {
        return row%2 == 0;
    }
}
