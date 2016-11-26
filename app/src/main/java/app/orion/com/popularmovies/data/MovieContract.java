package app.orion.com.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by syedaamir on 22-11-2016.
 */

public class MovieContract {
    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie_details";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASED_ON = "release_date";
        public static final String COLUMN_POSTER_URL = "poster_url";
        public static final String COLUMN_BACKDROP_URL = "backdrop_url";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TRAILER_PATH = "trailer_path";
        public static final String COLUMN_IS_FAVOURITE = "is_favourite";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
    }
    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_MOVIE_ID ="movie_id";
        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
    }
}
