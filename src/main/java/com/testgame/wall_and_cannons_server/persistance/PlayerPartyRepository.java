package com.testgame.wall_and_cannons_server.persistance;

import com.testgame.wall_and_cannons_server.domain.PlayerParty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerPartyRepository extends JpaRepository<PlayerParty, Long> {
}
