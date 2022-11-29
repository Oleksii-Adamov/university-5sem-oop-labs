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
            Deque<AlgoCell> path = new ArrayDeque<>();
            startingCell.putInPath();
            path.addFirst(startingCell);
            boolean continueWalk = true;

            while (continueWalk) {
                AlgoCell prevCell = path.peekFirst();
                AlgoCell nextCell = randomNeighbour(prevCell);
                System.out.println("prevCell = " + prevCell);
                System.out.println("nextCell = " + nextCell);
                if (nextCell.isNotInMaze()) {
                    nextCell.putInPath();
                    path.addFirst(nextCell);
                }
                else if (nextCell.isInMaze()) {
                    path.addFirst(nextCell);
                    continueWalk = false;
                }
                else if (nextCell.isInPath()) {
                    while (path.peekFirst() != nextCell) {
                        AlgoCell removedCell = path.removeFirst();
                        removedCell.removeFromPath();
                        System.out.println("removing " + removedCell);
                    }
                }
            }
            cellsInMaze += path.size() - 1;
            AlgoCell prevCell = null;
            while (!path.isEmpty()) {
                AlgoCell curCell = path.removeFirst();
                System.out.println("adding to maze" + curCell);
                if (prevCell != null)
                    prevCell.removeWall(curCell);
                curCell.putInMaze();
                prevCell = curCell;
            }
        }
        // making exit
        maze[numRows - 1][numCols - 1].removeBottomWall();
        System.out.println("Exiting generator");
        return maze;
    }
}
