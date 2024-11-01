package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;

@Entity
public class Castle {

    @Id
    private Long id;

    @Column
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;

    @OneToOne()
    @JoinColumn(unique = true, nullable = false)
    private Wall wall;

}
