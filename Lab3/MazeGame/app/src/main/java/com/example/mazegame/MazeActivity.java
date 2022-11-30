package com.example.mazegame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mazegame.database.LevelsDbHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MazeActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private LevelsDbHelper dbHelper;
    private int levelId;
    private int levelRows;
    private int levelCols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);
        executorService = Executors.newFixedThreadPool(4);
        Bundle extras = getIntent().getExtras();
        int score = 0;
        if (extras != null) {
            levelId = extras.getInt("id", 0);
            levelRows = extras.getInt("rows", 0);
            levelCols = extras.getInt("cols", 0);
            score = extras.getInt("score", 0);
        }
        int finalRows = levelRows;
        int finalCols = levelCols;
        int finalScore = score;
        executorService.submit(() -> setupMazeView(finalRows, finalCols, finalScore));
        changeScore(score);
    }

    private void setupMazeView(int rows, int cols, int score) {
        MazeView mazeView = new MazeView(this, null);
        mazeView.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout gameLinearLayout = findViewById(R.id.gamelinearlayout);
        runOnUiThread(() -> gameLinearLayout.addView(mazeView));
        dbHelper = new LevelsDbHelper(this);
        mazeView.setMazeParams(rows, cols, score);
        mazeView.generateMaze();
    }

    public void changeScore(int score) {
        TextView scoreView = findViewById(R.id.scoreView);
        Resources res = getResources();
        String scoreText = String.format(res.getString(R.string.score), score);
        scoreView.setText(scoreText);
        executorService.submit(() -> dbHelper.updateScore(score, levelId));
    }

    public void back(View v) {
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.putExtra("id", levelId);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}