package com.testgame.wall_and_cannons_server.persistance;

import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CastleRepository extends JpaRepository<Castle, Long> {

    List<Castle> findByPlayerUser(PlayerUser playerUser);
}
