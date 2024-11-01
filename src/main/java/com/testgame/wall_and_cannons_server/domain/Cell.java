package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;

@Entity
public class Cell {

    @Id
    public Long id;

    @Column
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Wall wall;

    @Column
    public int row;

    @Column
    public int column;

    @Column
    public boolean isHit;
}
