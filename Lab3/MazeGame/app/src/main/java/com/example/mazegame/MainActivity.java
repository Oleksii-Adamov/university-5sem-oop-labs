package com.example.mazegame;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.mazegame.database.LevelsDbHelper;
import com.example.mazegame.database.contracts.LevelsContract;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private LevelsDbHelper dbHelper;
    private Map<Integer, Button> levelButtonsMap;
    private final int mazeRequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newFixedThreadPool(4);
        executorService.submit(this::createMenu);
    }

    private void createMenu() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // exit button
        LinearLayout exitButtonRow = new LinearLayout(this);
        exitButtonRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        exitButtonRow.setGravity(Gravity.START);
        Button exitButton = new Button(this);
        exitButton.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        exitButton.setText("Exit");
        exitButton.setOnClickListener(v -> {/*finish();System.exit(0);*/finishAndRemoveTask();});
        exitButtonRow.addView(exitButton);
        layout.addView(exitButtonRow);

        // levels buttons
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout levelsButtons = new LinearLayout(this);
        levelsButtons.setOrientation(LinearLayout.VERTICAL);

        levelButtonsMap = new HashMap<>();

        dbHelper = new LevelsDbHelper(this);

        Cursor levelsCursor = dbHelper.getLevels();
        while (levelsCursor.moveToNext()) {
            int id = levelsCursor.getInt(levelsCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry._ID));
            int rows = levelsCursor.getInt(levelsCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry.COLUMN_NAME_ROWS));
            int cols = levelsCursor.getInt(levelsCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry.COLUMN_NAME_COLS));
            int score = levelsCursor.getInt(levelsCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry.COLUMN_NAME_SCORE));

            Button levelButton = new Button(this);
            levelButton.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            levelButton.setText(rows + "x" + cols + " Score: " + score);
            levelButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MazeActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("rows", rows);
                intent.putExtra("cols", cols);
                intent.putExtra("score", score);
                 // Or some number you choose
                startActivityForResult(intent, mazeRequestCode);
            });
            levelButtonsMap.put(id, levelButton);
            levelsButtons.addView(levelButton);
        }

        scrollView.addView(levelsButtons);
        layout.addView(scrollView);

        // changing view
        runOnUiThread(() -> setContentView(layout));
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mazeRequestCode && resultCode == RESULT_OK) {
            changeUILevelScore();
        }
    }

    public void changeUILevelScore() {
        for (Map.Entry<Integer,Button> entry : levelButtonsMap.entrySet()) {
            int id = entry.getKey();
            Cursor levelCursor = dbHelper.getLevel(id);
            levelCursor.moveToNext();
            int rows = levelCursor.getInt(levelCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry.COLUMN_NAME_ROWS));
            int cols = levelCursor.getInt(levelCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry.COLUMN_NAME_COLS));
            int score = levelCursor.getInt(levelCursor.getColumnIndexOrThrow(
                    LevelsContract.LevelsEntry.COLUMN_NAME_SCORE));
            entry.getValue().setText(rows + "x" + cols + " Score: " + score);
        }
    }
}