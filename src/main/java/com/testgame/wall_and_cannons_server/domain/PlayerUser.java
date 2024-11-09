package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
public class PlayerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column
    private String loginName;

    @Column
    private String gameName;

    @Column
    private String password;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "playerUser")
    private List<Castle> castle;

    @Override
    public String toString() {
        return "Player [id=" + id + ", loginName=" + loginName + ", gameName=" + gameName + "]";
    }
}
