package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

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
    private List<Castle> castles;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "playerUser")
    private List<PlayerParty> playerParties;

    @Override
    public String toString() {
        return "Player [id=" + id + ", loginName=" + loginName + ", gameName=" + gameName + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof PlayerUser) {
            PlayerUser playerUser = (PlayerUser) obj;
            return Objects.equals(id, playerUser.id) && loginName.equals(playerUser.loginName) && gameName.equals(playerUser.gameName);
        }
        return false;
    }
}
