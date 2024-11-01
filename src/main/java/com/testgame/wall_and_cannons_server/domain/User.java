package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    private String loginName;

    @Column
    private String gameName;

    @Column
    private String password;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Castle> castle;
}
