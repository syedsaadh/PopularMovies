package app.orion.com.popularmovies.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toolbar;

import java.util.ArrayList;

import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.data.MovieDbHelper;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.util.ImageAdapter;

/**
 * Created by syedaamir on 26-11-2016.
 */

public class FavouritesMovieFragment extends Fragment {
    public ImageAdapter moviesPosterAdapter;
    View rootView;
    MovieDbHelper movieDbHelper;
    ArrayList<MovieDetail> movieDetailoArrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main,container,false);
        getActivity().setTitle("WatchList");
        movieDbHelper = new MovieDbHelper(getContext());
        movieDetailoArrayList = new ArrayList<>();
        moviesPosterAdapter = new ImageAdapter(getActivity(),R.layout.list_item_movies,R.id.poster_list_image_view,movieDetailoArrayList);
        GridView mListView = (GridView) rootView.findViewById(R.id.gridView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieDetail mviDetail = moviesPosterAdapter.getItem(position);
                Intent i = new Intent(getActivity(),MovieDetailsActivity.class)
                        .putExtra("movie_detail_parcelable",mviDetail);
                startActivity(i);
            }
        });
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mListView.setNumColumns(4);
        }else{
            mListView.setNumColumns(2);
        }
        mListView.setAdapter(moviesPosterAdapter);
        return rootView;
    }
    public void initialise(){
        moviesPosterAdapter.clear();
        movieDetailoArrayList = movieDbHelper.readMovieDetailsFromDb();
        if(movieDetailoArrayList == null){
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new EmptyErrorFragment())
                    .commit();
        }else{
            moviesPosterAdapter.addAll(movieDetailoArrayList);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        initialise();
    }
}
