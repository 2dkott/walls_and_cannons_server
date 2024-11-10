package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Castle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "PlayerUser_id", nullable = false)
    private PlayerUser playerUser;

    @Column
    private boolean isCurrent;

    @Override
    public String toString() {
        return "Castle [id=" + id + ", playerUser=" + playerUser + ", isCurrent=" + isCurrent + "]";
    }

}
