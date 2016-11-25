package app.orion.com.popularmovies.ui;

import android.content.Intent;
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

import java.util.ArrayList;

import app.orion.com.popularmovies.BuildConfig;
import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.model.Reviews;
import app.orion.com.popularmovies.util.ReviewListAdapter;

/**
 * Created by syedaamir on 23-11-2016.
 */

public class MovieReviewsFragment extends Fragment {
    final ArrayList<Reviews> mReviews = new ArrayList();
    private View rootView;
    private long movieId ;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String LOG_TAG = MovieReviewsFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             movieId = bundle.getLong("movie_id", 0);
        }
        Log.v(LOG_TAG, "Movie Id " + movieId);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        reviewListCreate(movieId);

    }
    private void initialiseList(){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_list);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ReviewListAdapter(getContext(),mReviews);
        mRecyclerView.setAdapter(mAdapter);
    }
    private void reviewListCreate(long movieId){
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
                                mReviews.add(new Reviews(
                                        reviewDetail.getString(MY_AUTHOR),
                                        reviewDetail.getString(MY_CONTENT)
                                ));
                            } Log.v(MovieReviewsFragment.class.getSimpleName()," MREVIEW" + mReviews.size());
                            initialiseList();
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
