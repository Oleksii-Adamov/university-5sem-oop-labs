package com.example.mazegame.maze.wilsonmazegenerator;

import com.example.mazegame.maze.Cell;

public class AlgoCell extends Cell {

    private CellState state = CellState.NOT_IN_MAZE;

    private AlgoCell nextCell = null;

    public AlgoCell(int row, int col) {
        super(row, col);
    }

    public void putInMaze() {
        state = CellState.IN_MAZE;
    }

    public boolean isInMaze() {
        return state == CellState.IN_MAZE;
    }

    public boolean isNotInMaze() {
        return state == CellState.NOT_IN_MAZE;
    }

    public AlgoCell getNextCell() {
        return nextCell;
    }

    public void setNextCell(AlgoCell nextCell) {
        this.nextCell = nextCell;
    }

    public void removeWall(AlgoCell otherCell) {
        if (this.col - 1 == otherCell.col && this.row == otherCell.row) {
            this.removeLeftWall();
            otherCell.removeRightWall();
        }
        if (this.col == otherCell.col && this.row - 1 == otherCell.row) {
            this.removeTopWall();
            otherCell.removeBottomWall();
        }
        if (this.col + 1 == otherCell.col && this.row == otherCell.row) {
            this.removeRightWall();
            otherCell.removeLeftWall();
        }
        if (this.col == otherCell.col && this.row + 1 == otherCell.row) {
            this.removeBottomWall();
            otherCell.removeTopWall();
        }
    }

    @Override
    public String toString() {
        return super.toString() + "AlgoCell{" +
                "state=" + state +
                '}';
    }
}
