package app.orion.com.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import app.orion.com.popularmovies.data.MovieDbHelper;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.model.Reviews;
import app.orion.com.popularmovies.util.NetworkUtillity;
import app.orion.com.popularmovies.util.ReviewListAdapter;

/**
 * Created by syedaamir on 23-11-2016.
 */

public class MovieReviewsFragment extends Fragment {
    private ArrayList<Reviews> mReviews = new ArrayList();
    private View rootView;
    private long movieId ;
    private NetworkUtillity networkUtillity;
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

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_list);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        MovieDbHelper movieDbHelper = new MovieDbHelper(getContext());
        mReviews = movieDbHelper.readMovieReviewsFromDb(movieId);
        if(mReviews == null){
            mReviews = new ArrayList<Reviews>();
            reviewListCreate(movieId);
        }else{
            mAdapter = new ReviewListAdapter(getContext(),mReviews);
            mRecyclerView.setAdapter(mAdapter);
        }

        return rootView;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkUtillity = new NetworkUtillity(getContext());
    }

    private void initialiseList(){
        if(mReviews.isEmpty()){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new EmptyErrorFragment())
                    .commit();
        }else{
            mAdapter = new ReviewListAdapter(getContext(),mReviews);
            mRecyclerView.setAdapter(mAdapter);
        }
    }
    private void reviewListCreate(long movieId){
        String API_KEY_PARAM = "api_key";
        String apiKey = BuildConfig.MOVIEDB_API_KEY;
        String type = "reviews";
        String REVIEW_API_BASE_URL = "https://api.themoviedb.org/3/movie/";

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
        if(networkUtillity.isInternetAvailable()){
            Volley.newRequestQueue(getContext()).add(jsonRequest);
        }else{
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new NetworkErrorFragment())
                    .commit();
            networkUtillity.showError();
        }
    }
}
