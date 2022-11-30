package com.example.mazegame.database.contracts;

import android.provider.BaseColumns;

public final class LevelsContract {
    private LevelsContract() {}
    public static class LevelsEntry implements BaseColumns {
        public static final String TABLE_NAME = "levels";
        public static final String COLUMN_NAME_ROWS = "rows";
        public static final String COLUMN_NAME_COLS = "columns";
        public static final String COLUMN_NAME_SCORE = "score";
        public static final String _ID = "id";
    }
}
