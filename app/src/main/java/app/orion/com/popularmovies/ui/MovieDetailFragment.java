package app.orion.com.popularmovies.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import app.orion.com.popularmovies.BuildConfig;
import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.data.MovieContract;
import app.orion.com.popularmovies.data.MovieDbHelper;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.model.Reviews;
import app.orion.com.popularmovies.util.MovieDetilsStorageHelper;

/**
 * Created by syedaamir on 23-11-2016.
 */

public class MovieDetailFragment extends Fragment {
    final ArrayList<Reviews> mReviews = new ArrayList();
    private MovieDetail movieDetail;
    private Reviews movieReviews;
    static long movieId;
    private static int isFavourite;
    private View rootView;
    private String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private ImageView favouriteBtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getActivity().getIntent();
        movieDetail = (MovieDetail) i.getParcelableExtra("movie_detail_parcelable");
        isFavourite = checkMovieIsFavourite(movieDetail.getMid());
        Log.d(LOG_TAG," is Favourite Value " + isFavourite);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent i = getActivity().getIntent();
        movieDetail = (MovieDetail) i.getParcelableExtra("movie_detail_parcelable");
        movieId = movieDetail.getMid();
        Double ratingVal = movieDetail.getRating();
        Long voteCount = movieDetail.getVoteCount();
        String ratingStr = ratingVal.toString();
        if (i != null) {
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(movieDetail.getTitle());
            ((TextView) rootView.findViewById(R.id.movie_desc)).setText(movieDetail.getOverview());
            ((TextView) rootView.findViewById(R.id.movie_rating)).setText(ratingStr);
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(movieDetail.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.movie_votes)).setText(voteCount.toString() + "+ Votes");
            ImageView myImageView = (ImageView) rootView.findViewById(R.id.movie_poster);

            MovieDetilsStorageHelper movieDetilsStorageHelper = new MovieDetilsStorageHelper(getContext());
            Uri mThumb = movieDetilsStorageHelper.getBitmapFromDir(movieDetail.getBackdropName(movieDetail.getBackdropUrl()));
            if (mThumb != null) {
                Glide
                        .with(this)
                        .load(mThumb)
                        .crossFade()
                        .into(myImageView);
                Log.d(LOG_TAG, " Bitmap Set ");
            } else {
                Glide
                        .with(this)
                        .load(movieDetail.getBackdropUrl())
                        .centerCrop()
                        .crossFade()
                        .into(myImageView);
            }
        }

        TextView checkReview = (TextView) rootView.findViewById(R.id.movie_reviews_btn);
        checkReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("movie_id",movieId);
                Fragment movieReviewsFragment = new MovieReviewsFragment();
                movieReviewsFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,movieReviewsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        if(movieDetail.getTrailerPath() == null){
            getTrailerUrl(movieId);
        } else{
          trailerSet(movieDetail.getTrailerPath());
        }
        favouriteBtn = (ImageView)rootView.findViewById(R.id.movie_favourite);
        if(isFavourite == 1){
            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }else{
            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavourite();
            }
        });
        return rootView;
    }
    private void toggleFavourite(){

        if(isFavourite == 1){
            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            unsetFavourite(movieId);
            isFavourite = 0;
        }else {
            favouriteBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
            new MovieDbHelper(getContext()).saveMovieDetailsToDb(movieDetail);
            new MovieDetilsStorageHelper(getContext()).addBitmapToDir(movieDetail.getPosterName(movieDetail.getPosterUrl()),movieDetail.getPosterUrl());
            new MovieDetilsStorageHelper(getContext()).addBitmapToDir(movieDetail.getBackdropName(movieDetail.getBackdropUrl()),movieDetail.getBackdropUrl());
            MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
            if(movieDbHelper.readMovieReviewsFromDb(movieId) == null){
                getReviewsAndAddToDb(movieId);
            }
            isFavourite = 1;
        }
    }
    private void trailerSet(String key){
        String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
        final String playUrl = YOUTUBE_BASE_URL + key;
        ImageView playBtn =((ImageView)rootView.findViewById(R.id.movie_play));
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(playUrl));
                startActivity(browserIntent);
            }
        });
        movieDetail.setTrailerPath(playUrl);
    }
    private void unsetFavourite(long mid){
        MovieDbHelper dbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] selectionArgs = { String.valueOf(mid) };
        db.delete(MovieContract.MoviesEntry.TABLE_NAME, MovieContract.MoviesEntry.COLUMN_MOVIE_ID + " = ?", selectionArgs);
        db.delete(MovieContract.ReviewEntry.TABLE_NAME, MovieContract.ReviewEntry.COLUMN_MOVIE_ID + " = ?",selectionArgs);
    }
    private int checkMovieIsFavourite(long mid) {
        MovieDbHelper dbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String MY_SEARCH_QUERY = "SELECT " + MovieContract.MoviesEntry.COLUMN_IS_FAVOURITE + " FROM " +
                MovieContract.MoviesEntry.TABLE_NAME + " WHERE " +
                MovieContract.MoviesEntry.COLUMN_MOVIE_ID + " = " + mid;

        Cursor cursor = db.rawQuery(MY_SEARCH_QUERY,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return 0;
        }
        cursor.close();
        db.close();
        dbHelper.close();
        return 1;
    }
    private void getReviewsAndAddToDb(long movieId){
        String API_KEY_PARAM = "api_key";
        String apiKey = BuildConfig.MOVIEDB_API_KEY;
        String type = "reviews";
        String REVIEW_API_BASE_URL = "https://api.themoviedb.org/3/movie/";

        //"https://api.themoviedb.org/3/movie/278/reviews?api_key=fe648f558044e8957058c969649b379a";


        final String REVIEW_API_URL =REVIEW_API_BASE_URL + movieId + "/" + type + "?";
        final String MY_AUTHOR = "author";
        final String MY_CONTENT = "content";

        Uri uri = Uri.parse(REVIEW_API_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM,apiKey)
                .build();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.v(MovieReviewsFragment.class.getSimpleName()," JSON RESPONSE " + response.toString());
                            JSONArray results = response.getJSONArray("results");
                            Log.v(MovieReviewsFragment.class.getSimpleName()," JSON RESPONSE LENGTH" + results.length());
                            for(int i = 0; i < results.length(); i++) {
                                JSONObject reviewDetail = results.getJSONObject(i);
                                Reviews mReviews = new Reviews(reviewDetail.getString(MY_AUTHOR),
                                        reviewDetail.getString(MY_CONTENT));
                                new MovieDbHelper(getContext()).saveMovieReviewsToDb(mReviews, movieDetail);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonRequest);
    }
    private void getTrailerUrl(long movieId){
        String API_KEY_PARAM = "api_key";
        String apiKey = BuildConfig.MOVIEDB_API_KEY;
        String type = "videos";
        String REVIEW_API_BASE_URL = "https://api.themoviedb.org/3/movie/";

        //"https://api.themoviedb.org/3/movie/278/reviews?api_key=fe648f558044e8957058c969649b379a";


        final String REVIEW_API_URL =REVIEW_API_BASE_URL + movieId + "/" + type + "?";
        final String MY_KEY = "key";

        Uri uri = Uri.parse(REVIEW_API_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM,apiKey)
                .build();

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, uri.toString(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        try {
                            Log.v(MovieReviewsFragment.class.getSimpleName()," JSON RESPONSE " + response.toString());
                            JSONArray results = response.getJSONArray("results");
                            Log.v(MovieReviewsFragment.class.getSimpleName()," JSON RESPONSE LENGTH" + results.length());
                            String movieTrailerKey = results.getJSONObject(0).getString(MY_KEY);
                            trailerSet(movieTrailerKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        Volley.newRequestQueue(getContext()).add(jsonRequest);
    }
}