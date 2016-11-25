package app.orion.com.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static app.orion.com.popularmovies.data.MovieContract.MoviesEntry;

/**
 * Created by syedaamir on 22-11-2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies";
    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +
                MoviesEntry._ID + " INTEGER PRIMARY KEY," +
                MoviesEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE," +
                MoviesEntry.COLUMN_TITLE + " TEXT," +
                MoviesEntry.COLUMN_RELEASED_ON + " TEXT," +
                MoviesEntry.COLUMN_POSTER_PATH + " TEXT," +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT," +
                MoviesEntry.COLUMN_TRAILER_PATH + " TEXT," +
                MoviesEntry.COLUMN_IS_FAVOURITE + " INTEGER," +
                MoviesEntry.COLUMN_RATING + " INTEGER " +
                " );";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
