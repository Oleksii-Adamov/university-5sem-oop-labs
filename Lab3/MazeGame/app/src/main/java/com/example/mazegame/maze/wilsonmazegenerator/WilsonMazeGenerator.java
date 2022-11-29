package com.example.mazegame.maze.wilsonmazegenerator;

import com.example.mazegame.maze.Cell;
import com.example.mazegame.maze.MazeGenerator;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WilsonMazeGenerator implements MazeGenerator {
    private AlgoCell[][] maze;
    int numRows;
    int numCols;

    private AlgoCell randomNotMazeCell() {
        AlgoCell retCell = null;
        while (retCell == null || !retCell.isNotInMaze()) {
            int row = ThreadLocalRandom.current().nextInt(0, numRows);
            int col = ThreadLocalRandom.current().nextInt(0, numCols);
            retCell = maze[row][col];
        }
        return retCell;
    }

    private AlgoCell randomNeighbour(AlgoCell cell) {
        List<AlgoCell> neighbours = new ArrayList<>();
        if (cell.getCol() > 0)
            neighbours.add(maze[cell.getRow()][cell.getCol() - 1]);
        if (cell.getRow() < numRows - 1)
            neighbours.add(maze[cell.getRow() + 1][cell.getCol()]);
        if (cell.getCol() < numCols - 1)
            neighbours.add(maze[cell.getRow()][cell.getCol() + 1]);
        if (cell.getRow() > 0)
            neighbours.add(maze[cell.getRow() - 1][cell.getCol()]);
        return neighbours.get(ThreadLocalRandom.current().nextInt(0, neighbours.size()));
    }

    private void initMaze() {
        maze = new AlgoCell[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                maze[row][col] = new AlgoCell(row, col);
            }
        }
    }

    @Override
    public Cell[][] generateMaze(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        initMaze();
        randomNotMazeCell().putInMaze();
        int cellsInMaze = 1;
        int numCells = numRows * numCols;
        while (cellsInMaze < numCells) {
            AlgoCell startingCell = randomNotMazeCell();
            System.out.println("startingCell = " + startingCell);
            AlgoCell prevCell = startingCell;
            boolean continueWalk = true;
            while (continueWalk) {
                AlgoCell nextCell = randomNeighbour(prevCell);
                prevCell.setNextCell(nextCell);
                if (nextCell.isInMaze()) {
                    nextCell.setNextCell(null);
                    continueWalk = false;
                }
                System.out.println("nextCell = " + nextCell);
                prevCell = nextCell;
            }
            AlgoCell curCell = startingCell;
            while (curCell.getNextCell() != null) {
                curCell.putInMaze();
                cellsInMaze++;
                curCell.removeWall(curCell.getNextCell());
                System.out.println("adding to maze " + curCell);
                curCell = curCell.getNextCell();
            }
        }
        // making exit
        maze[numRows - 1][numCols - 1].removeBottomWall();
        System.out.println("Exiting generator");
        return maze;
    }
}
