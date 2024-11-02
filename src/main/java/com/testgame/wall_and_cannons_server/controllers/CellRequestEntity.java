package com.testgame.wall_and_cannons_server.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testgame.wall_and_cannons_server.domain.Cell;
import lombok.Data;

@Data
public class CellRequestEntity {

    public CellRequestEntity() {}

    public CellRequestEntity(Cell cell) {
        this.id = cell.getId();
        this.cellColumn = cell.getCellColumn();
        this.cellRow = cell.getCellRow();
        this.wallId = cell.getWall().getId();
    }

    @JsonProperty("id")
    private Long id;

    @JsonProperty("wall_id")
    private Long wallId;

    @JsonProperty("cell_row")
    public int cellRow;

    @JsonProperty("cell_column")
    public int cellColumn;

    @JsonProperty("is_hit")
    public boolean isHit;

}
