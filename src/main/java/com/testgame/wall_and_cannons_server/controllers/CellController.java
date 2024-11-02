package com.testgame.wall_and_cannons_server.controllers;

import com.testgame.wall_and_cannons_server.services.CellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cell")
public class CellController {

    @Autowired
    CellService cellService;

    @GetMapping("/hit")
    public List<CellRequestEntity> getHitCells() {
        return cellService.getCellsByWall(2).stream().map(CellRequestEntity::new).toList();
    }


}
