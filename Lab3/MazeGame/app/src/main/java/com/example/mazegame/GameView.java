package com.example.mazegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.mazegame.maze.Cell;
import com.example.mazegame.maze.MazeGenerator;
import com.example.mazegame.maze.wilsonmazegenerator.AlgoCell;
import com.example.mazegame.maze.wilsonmazegenerator.WilsonMazeGenerator;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GameView extends View {
    private Cell[][] maze;
    private final MazeGenerator mazeGenerator = new WilsonMazeGenerator();
    private int numRows = 10;
    private int numCols = 7;
    private float cellSize;
    private float verticalMargin;
    private float horizontalMargin;
    private static final float WALL_PAINT_THICKNESS = 8;
    private int width;
    private int height;
    private static final float TEXT_PAINT_THICKNESS = 8;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Future mazeGeneration;
    private Player player;
    private int loadingDotCounter = 1;
    private Paint wallPaint;
    private Paint textPaint;
    private Paint playerPaint;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaints();
        mazeGeneration = executorService.submit(() -> {
            maze = mazeGenerator.generateMaze(numRows, numCols);
            player = new Player(maze);
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        drawBackground(canvas);
        System.out.println("Checking for done");
        try {
            mazeGeneration.get(1, TimeUnit.MILLISECONDS);
            System.out.println("Done");
            System.out.println(Arrays.deepToString(maze));
            drawMaze(canvas);
            drawPlayer(canvas);
//            if (maze != null && mazeGeneration.isDone()) {
//                System.out.println("Done");
//                System.out.println(Arrays.deepToString(maze));
//                drawMaze(canvas);
//                drawPlayer(canvas);
//            }
//            else {
//                System.out.println("Not Done");
//                StringBuilder stringBuilder = new StringBuilder("Creating maze");
//                for (int i = 0; i < loadingDotCounter; i++) {
//                    stringBuilder.append('.');
//                }
//                loadingDotCounter++;
//                if (loadingDotCounter > 3) loadingDotCounter = 1;
//                canvas.drawText(stringBuilder.toString(), width / 2 - 50, height / 2, textPaint);
//                //canvas.drawText(stringBuilder.toString(), 5, 5, textPaint);
//                invalidate();
//            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        catch (TimeoutException e) {
            System.out.println("Not Done");
            StringBuilder stringBuilder = new StringBuilder("Creating maze");
            for (int i = 0; i < loadingDotCounter; i++) {
                stringBuilder.append('.');
            }
            loadingDotCounter++;
            if (loadingDotCounter > 3) loadingDotCounter = 1;
            canvas.drawText(stringBuilder.toString(), width / 2 - 50, height / 2, textPaint);
            //canvas.drawText(stringBuilder.toString(), 5, 5, textPaint);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(movePlayer(event)) {
                if (player.isEscaped()) {
                    maze = null;
                    mazeGeneration = executorService.submit(() -> {
                        maze = mazeGenerator.generateMaze(numRows, numCols);
                        player.reset(maze);
                    });
                }
                invalidate();
            }
        }
        return true;
    }

    private boolean movePlayer(MotionEvent event) {
        float playerX = horizontalMargin + (player.getCol() + 0.5f)*cellSize;
        float playerY = verticalMargin + (player.getRow() + 0.5f)*cellSize;
        float deltaX = event.getX() - playerX;
        float deltaY = event.getY() - playerY;
        float absDeltaX = Math.abs(deltaX);
        float absDeltaY = Math.abs(deltaY);

        if (absDeltaX > cellSize || absDeltaY > cellSize) {
            if (absDeltaX > absDeltaY) {
                // move in x directing
                if (deltaX > 0) {
                    return player.moveRight();
                }
                else {
                    return player.moveLeft();
                }
            }
            else {
                // move in y direction
                if (deltaY > 0) {
                    return player.moveDown();
                }
                else {
                    return player.moveUp();
                }
            }
        }
        else {
            return false;
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.GREEN);
    }

    private void drawMaze(Canvas canvas) {
        if (width / height > numCols / numRows) {
            cellSize = height / (float) (numRows + 2);
        }
        else {
            cellSize = width / (float) (numCols + 2);
        }
        verticalMargin = (height - cellSize * numRows) / 2;
        horizontalMargin = (width - cellSize * numCols) / 2;
        canvas.translate(horizontalMargin, verticalMargin);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (maze[row][col].hasLeftWall()) {
                    //System.out.println(row + " " + col + " left wall");
                    canvas.drawLine(col * cellSize, row * cellSize, col * cellSize, (row+1) * cellSize, wallPaint);
                }
                if (maze[row][col].hasTopWall()) {
                    //System.out.println(row + " " + col + " top wall");
                    canvas.drawLine(col * cellSize, row * cellSize, (col+1) * cellSize, row * cellSize, wallPaint);
                }
                if (maze[row][col].hasRightWall()) {
                    //System.out.println(row + " " + col + " right wall");
                    canvas.drawLine((col+1) * cellSize, row * cellSize, (col+1) * cellSize, (row+1) * cellSize, wallPaint);
                }
                if (maze[row][col].hasBottomWall()) {
                    //System.out.println(row + " " + col + " bottom wall");
                    canvas.drawLine(col * cellSize, (row+1) * cellSize, (col+1) * cellSize, (row+1) * cellSize, wallPaint);
                }
            }
        }
    }

    private void drawPlayer(Canvas canvas) {
        float margin = cellSize / 10;
        canvas.drawRect(player.getCol()*cellSize + margin,
                player.getRow()*cellSize + margin,
                (player.getCol()+1) * cellSize - margin,
                (player.getRow()+1)*cellSize - margin,
                playerPaint);
    }

    private void initPaints() {
        initWallPaint();
        initTextPaint();
        initPlayerPaint();
    }

    private void initWallPaint() {
        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_PAINT_THICKNESS);
    }

    private void initTextPaint() {
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setStrokeWidth(TEXT_PAINT_THICKNESS);
        textPaint.setTextSize(100);
    }

    private void initPlayerPaint() {
        playerPaint = new Paint();
        playerPaint.setColor(Color.RED);
    }
}
