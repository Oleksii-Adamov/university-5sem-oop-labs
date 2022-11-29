package com.example.mazegame;

import com.example.mazegame.maze.Cell;

public class Player {
    private Cell posCell;
    private Cell[][] maze;
    private boolean escaped = false;

    public Player(Cell[][] maze) {
        this.maze = maze;
        this.posCell = maze[0][0];
    }

    public int getRow() {
        return posCell.getRow();
    }

    public int getCol() {
        return posCell.getCol();
    }

    public boolean moveLeft() {
        if (!posCell.hasLeftWall()) {
            if (posCell.getCol() - 1 < 0) {
                escaped = true;
            }
            else {
                posCell = maze[posCell.getRow()][posCell.getCol() - 1];
            }
            return true;
        }
        else {
            return false;
        }
    }

    public boolean moveUp() {
        if (!posCell.hasTopWall()) {
            if (posCell.getRow() - 1 < 0) {
                escaped = true;
            }
            else {
                posCell = maze[posCell.getRow() - 1][posCell.getCol()];
            }
            return true;
        }
        else {
            return false;
        }
    }

    public boolean moveRight() {
        if (!posCell.hasRightWall()) {
            if (posCell.getCol() + 1 >= maze[0].length) {
                escaped = true;
            }
            else {
                posCell = maze[posCell.getRow()][posCell.getCol() + 1];
            }
            return true;
        }
        else {
            return false;
        }
    }

    public boolean moveDown() {
        if (!posCell.hasBottomWall()) {
            if (posCell.getRow() + 1 >= maze.length) {
                escaped = true;
            }
            else {
                posCell = maze[posCell.getRow() + 1][posCell.getCol()];
            }
            return true;
        }
        else {
            return false;
        }
    }

    public boolean isEscaped() {
        return escaped;
    }

    public void reset(Cell[][] newMaze) {
        maze = newMaze;
        posCell = newMaze[0][0];
        escaped = false;
    }
}
