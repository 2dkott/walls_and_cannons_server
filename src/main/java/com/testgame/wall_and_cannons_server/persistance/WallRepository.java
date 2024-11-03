package com.testgame.wall_and_cannons_server.persistance;

import com.testgame.wall_and_cannons_server.domain.Wall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WallRepository extends JpaRepository<Wall, Long> {

    Optional<Wall> findWallByCastleId(Long castleId);
}
