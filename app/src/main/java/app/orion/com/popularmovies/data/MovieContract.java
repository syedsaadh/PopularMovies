package app.orion.com.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by syedaamir on 22-11-2016.
 */

public class MovieContract {
    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "text_notes";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASED_ON = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TRAILER_PATH = "trailer_path";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";
    }
}
