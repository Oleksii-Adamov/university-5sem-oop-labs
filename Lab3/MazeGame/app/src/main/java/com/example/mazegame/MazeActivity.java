package com.example.mazegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MazeActivity extends AppCompatActivity {

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);
        executorService = Executors.newFixedThreadPool(4);
//        executorService.submit(this::launchMazeView);
        Bundle extras = getIntent().getExtras();
        int score = 0, rows = 0, cols = 0;
        if (extras != null) {
            score = extras.getInt("score", 0);
            rows = extras.getInt("rows", 0);
            cols = extras.getInt("cols", 0);
        }
        int finalRows = rows;
        int finalCols = cols;
        executorService.submit(() -> setupMazeView(finalRows, finalCols));
        changeScore(score);
    }

    private void setupMazeView(int rows, int cols) {
        MazeView mazeView = new MazeView(this, null);
        mazeView.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout gameLinearLayout = (LinearLayout) findViewById(R.id.gamelinearlayout);
        runOnUiThread(() -> gameLinearLayout.addView(mazeView));
        mazeView.setNumRows(rows);
        mazeView.setNumCols(cols);
        mazeView.generateMaze();
    }

    public void changeScore(int score) {
        TextView scoreView = (TextView) findViewById(R.id.scoreView);
        Resources res = getResources();
        String scoreText = String.format(res.getString(R.string.score), score);
        scoreView.setText(scoreText);
    }

    public void back(View v) {
        finish();
    }

//    private void launchMazeView() {
//        MazeView mazeView = new MazeView(this);
//        mazeView.setLayoutParams(new View.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT));
//    }
}