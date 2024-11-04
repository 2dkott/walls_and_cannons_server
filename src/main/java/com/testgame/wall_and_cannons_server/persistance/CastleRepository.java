package com.testgame.wall_and_cannons_server.persistance;

import com.testgame.wall_and_cannons_server.domain.Castle;
import com.testgame.wall_and_cannons_server.domain.PlayerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CastleRepository extends JpaRepository<Castle, Long> {

    List<Castle> findByPlayerUser(PlayerUser playerUser);

    Optional<Castle> findCastleByPlayerUserAndAndCurrentIsTrue(PlayerUser playerUser);
}
