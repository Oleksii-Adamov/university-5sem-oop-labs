package com.example.mazegame.maze;

public class Cell {
    protected final int row;
    protected final int col;
    private boolean topWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;
    private boolean rightWall = true;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean hasTopWall() {
        return topWall;
    }

    public boolean hasBottomWall() {
        return bottomWall;
    }

    public boolean hasLeftWall() {
        return leftWall;
    }

    public boolean hasRightWall() {
        return rightWall;
    }

    public void removeTopWall() {topWall = false;}

    public void removeBottomWall() {bottomWall = false;}

    public void removeLeftWall() {leftWall = false;}

    public void removeRightWall() {rightWall = false;}

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }


    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", topWall=" + topWall +
                ", bottomWall=" + bottomWall +
                ", leftWall=" + leftWall +
                ", rightWall=" + rightWall +
                '}';
    }
}
