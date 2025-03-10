package com.testgame.wall_and_cannons_server.services;

import com.testgame.wall_and_cannons_server.domain.Cell;
import com.testgame.wall_and_cannons_server.domain.Wall;
import com.testgame.wall_and_cannons_server.persistance.CellRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CellService {

    @Autowired
    CellRepository cellRepository;

    public Wall buildCells(Wall wall) {
        log.info("Building cells for {}", wall);
        int rows = wall.getWallRows();
        int columns = wall.getWallColumns();
        for(int i = 0; i < rows; i++) {
            int temp_columns = columns;
            if(wall.isShortRow(i)) {
                temp_columns++;
            }
            for(int j = 0; j < temp_columns; j++) {
                Cell cell = new Cell();
                cell.setCellRow(i);
                cell.setCellColumn(j);
                cell.setWall(wall);
                cellRepository.save(cell);
                log.info("{} was saved in DataBase", cell);
            }
        }
        return wall;
    }

    public List<Cell> getCellsByWall(long wallId) {
        return cellRepository.findAll().stream().filter(cell -> cell.getWall().getId()==wallId).toList();
    }
}
