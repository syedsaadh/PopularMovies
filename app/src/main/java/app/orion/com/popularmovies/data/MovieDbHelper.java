package app.orion.com.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.model.Reviews;

import static app.orion.com.popularmovies.data.MovieContract.MoviesEntry;
import static app.orion.com.popularmovies.data.MovieContract.ReviewEntry;

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
                MoviesEntry.COLUMN_POSTER_URL + " TEXT," +
                MoviesEntry.COLUMN_BACKDROP_URL + " TEXT," +
                MoviesEntry.COLUMN_OVERVIEW + " TEXT," +
                MoviesEntry.COLUMN_TRAILER_PATH + " TEXT," +
                MoviesEntry.COLUMN_IS_FAVOURITE + " INTEGER," +
                MoviesEntry.COLUMN_RATING + " INTEGER," +
                MoviesEntry.COLUMN_VOTE_COUNT + " INTEGER " +
                " );";
        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY," +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER," +
                ReviewEntry.COLUMN_AUTHOR + " TEXT," +
                ReviewEntry.COLUMN_CONTENT + " TEXT " +
                " );";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
    public void saveMovieDetailsToDb(MovieDetail movieDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues movieDetailValues = new ContentValues();
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_MOVIE_ID,movieDetail.getMid());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_TITLE,movieDetail.getTitle());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_RELEASED_ON,movieDetail.getReleaseDate());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_POSTER_URL,movieDetail.getPosterUrl());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_BACKDROP_URL,movieDetail.getBackdropUrl());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_OVERVIEW,movieDetail.getOverview());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_TRAILER_PATH,movieDetail.getTrailerPath());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_IS_FAVOURITE,1);
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_RATING,movieDetail.getRating());
        movieDetailValues.put(MovieContract.MoviesEntry.COLUMN_VOTE_COUNT,movieDetail.getVoteCount());
        long rowId = db.insert(MovieContract.MoviesEntry.TABLE_NAME, null, movieDetailValues);
        db.close();
    }
    public void saveMovieReviewsToDb (Reviews movieReview, MovieDetail movieDetail){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues movieReviewValues = new ContentValues();
        movieReviewValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieDetail.getMid());
        movieReviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR,movieReview.getAuthor());
        movieReviewValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, movieReview.getContent());
        long rowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, movieReviewValues);
        db.close();
    }
    public ArrayList<Reviews> readMovieReviewsFromDb (long movieId){
        SQLiteDatabase db = this.getReadableDatabase();
        String movieIdStr = String.valueOf(movieId);
        String selectionArgs[] = new String [1];
        selectionArgs[0] = movieIdStr;
        Cursor cursor = db.query(ReviewEntry.TABLE_NAME,
                null,
                ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                selectionArgs,
                null,
                null,
                null,
                null);
        ArrayList<Reviews> reviewsArrayList = null;
        if (cursor.moveToFirst()){
            reviewsArrayList = new ArrayList<>();
            do {
                Reviews model = new Reviews(cursor.getString(2),cursor.getString(3));
                reviewsArrayList.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reviewsArrayList;
    }
    public ArrayList<MovieDetail> readMovieDetailsFromDb () {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(MoviesEntry.TABLE_NAME,
                null,
                MoviesEntry.COLUMN_IS_FAVOURITE + " = 1 ",
                null,
                null,
                null,
                null,
                null);
        ArrayList<MovieDetail> movieDetailArrayList = null;
        if (cursor.moveToFirst()) {
            movieDetailArrayList = new ArrayList<>();
            do {
                MovieDetail model = new MovieDetail(
                        cursor.getLong(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getDouble(9),
                        cursor.getLong(10),
                        cursor.getString(3)
                );
                model.setTrailerPath(cursor.getString(7));
                movieDetailArrayList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movieDetailArrayList;
    }
}
