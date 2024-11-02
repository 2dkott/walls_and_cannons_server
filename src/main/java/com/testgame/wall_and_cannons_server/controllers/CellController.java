package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.domain.Cell;
import com.testgame.wall_and_cannons_server.services.CellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cell")
public class CellController {

    @Autowired
    CellService cellService;

    @GetMapping("/hit/{wall_id}")
    public List<CellRequestEntity> getHitCells(@PathVariable("wall_id") Long wallId) {
        return cellService.getCellsByWall(wallId).stream().map(CellRequestEntity::new).toList();
    }


}
