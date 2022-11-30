package com.example.mazegame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newFixedThreadPool(4);
        executorService.submit(this::createMenu);
    }

    @SuppressLint("SetTextI18n")
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
        for (int i = 5; i < 8; i++) {
            Button levelButton = new Button(this);
            levelButton.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            int score = 1;
            levelButton.setText(i + "x" + i + " Score: " + score);
            int finalI = i;
            levelButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MazeActivity.class);
                intent.putExtra("rows", finalI);
                intent.putExtra("cols", finalI);
                intent.putExtra("score", score);
                startActivity(intent);
            });
            levelsButtons.addView(levelButton);
        }
        scrollView.addView(levelsButtons);
        layout.addView(scrollView);

        // changing view
        runOnUiThread(() -> setContentView(layout));

    }
}