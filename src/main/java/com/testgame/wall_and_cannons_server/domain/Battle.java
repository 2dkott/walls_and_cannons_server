package com.testgame.wall_and_cannons_server.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@Entity
@Table
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "battle")
    private List<PlayerParty> playerParties;

    @Column
    public boolean isFinished;

    @OneToOne()
    @JoinColumn(unique = true, nullable = false)
    private PlayerUser winner;

    public Optional<PlayerParty> getPartyByPlayer(PlayerUser playerUser) {
        List<PlayerParty> listWIthPlayer = playerParties.stream()
                .filter(playerParty -> playerParty.getPlayerUser().equals(playerUser)).toList();
        return listWIthPlayer.isEmpty() ? Optional.empty() : Optional.ofNullable(listWIthPlayer.get(0));
    }

    public boolean isByPlayer(PlayerUser playerUser) {
        return !playerParties.stream().filter(playerParty -> playerParty.getPlayerUser().equals(playerUser)).toList().isEmpty();
    }

    public List<PlayerUser> getPlayerUsers() {
        return playerParties.stream().map(PlayerParty::getPlayerUser).toList();
    }

    @Override
    public String toString() {
        return "Battle [id=" + id + ", playerParties=" + playerParties + ", isFinished=" + isFinished + "]";
    }

}
