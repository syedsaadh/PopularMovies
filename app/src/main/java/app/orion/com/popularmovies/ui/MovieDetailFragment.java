package app.orion.com.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.orion.com.popularmovies.BuildConfig;
import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.model.Reviews;
import app.orion.com.popularmovies.util.ReviewListAdapter;

/**
 * Created by syedaamir on 23-11-2016.
 */

public class MovieDetailFragment extends Fragment {
    final ArrayList<Reviews> mReviews = new ArrayList();
    View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent i = getActivity().getIntent();
        final MovieDetail movieDetail = (MovieDetail) i.getParcelableExtra("movie_detail_parcelable");
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
            Glide
                    .with(this)
                    .load(movieDetail.getBackdropUrl())
                    .centerCrop()
                    .crossFade()
                    .into(myImageView);

        }
//        Log.v(MovieDetailFragment.class.getSimpleName(),"onCreateView");
        final long movieId = movieDetail.getMid();
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
        getTrailerUrl(movieId);
        return rootView;
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
                            String movieTrailerKey = results.getJSONObject(0).getString("key");
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