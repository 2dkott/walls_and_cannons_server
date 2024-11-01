package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Wall {
    @Id
    private Long id;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wall")
    private List<Cell> cellList;
}
