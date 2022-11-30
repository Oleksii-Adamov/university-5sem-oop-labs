package com.example.mazegame;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
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
    private final int EXIT_BUTTON_TEXIT_SIZE_SP = 20;
    private final int LEVEL_BUTTON_TEXT_SIZE_SP = 60;

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
        exitButtonRow.addView(createExitButton());
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
            Resources res = getResources();
            levelButton.setText(String.format(res.getString(R.string.level_info), rows, cols, score));
            levelButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MazeActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("rows", rows);
                intent.putExtra("cols", cols);
                intent.putExtra("score", score);
                 // Or some number you choose
                startActivityForResult(intent, mazeRequestCode);
            });
            levelButton.setTextSize(COMPLEX_UNIT_SP, LEVEL_BUTTON_TEXT_SIZE_SP);
            levelButton.setAllCaps(false);

            levelButtonsMap.put(id, levelButton);
            levelsButtons.addView(levelButton);
        }

        scrollView.addView(levelsButtons);
        layout.addView(scrollView);

        // changing view
        runOnUiThread(() -> setContentView(layout));
    }

    private Button createExitButton() {
        Button exitButton = new Button(this);
        exitButton.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        exitButton.setText(R.string.exit);
        exitButton.setTextSize(COMPLEX_UNIT_SP, EXIT_BUTTON_TEXIT_SIZE_SP);
        exitButton.setOnClickListener(v -> exit());
        return exitButton;
    }

    public void exit() {
        finishAndRemoveTask();
//        finish();System.exit(0);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mazeRequestCode && resultCode == RESULT_OK) {
            executorService.submit(() -> updateLevelUI(data.getIntExtra("id", 0)));
        }
    }

    public void updateLevelUI(int id) {
        Cursor levelCursor = dbHelper.getLevel(id);
        levelCursor.moveToNext();
        int rows = levelCursor.getInt(levelCursor.getColumnIndexOrThrow(
                LevelsContract.LevelsEntry.COLUMN_NAME_ROWS));
        int cols = levelCursor.getInt(levelCursor.getColumnIndexOrThrow(
                LevelsContract.LevelsEntry.COLUMN_NAME_COLS));
        int score = levelCursor.getInt(levelCursor.getColumnIndexOrThrow(
                LevelsContract.LevelsEntry.COLUMN_NAME_SCORE));
        Button button = levelButtonsMap.get(id);
        if (button != null) {
            Resources res = getResources();
            runOnUiThread(() -> button.setText(String.format(res.getString(R.string.level_info), rows, cols, score)));
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}