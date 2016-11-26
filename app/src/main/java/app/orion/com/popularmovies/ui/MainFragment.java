package app.orion.com.popularmovies.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import app.orion.com.popularmovies.BuildConfig;
import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.util.ImageAdapter;

/**
 * Created by syedaamir on 28-10-2016.
 */

public class MainFragment extends Fragment {
    public ImageAdapter moviesPosterAdapter;
    public MainFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main,container,false);
        getActivity().setTitle("Popular Movies");
        moviesPosterAdapter = new ImageAdapter(getActivity(),R.layout.list_item_movies,R.id.poster_list_image_view,new ArrayList<MovieDetail>());
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
        this.setHasOptionsMenu(true);
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_popular) {
            initialiseMovies("popular");
            return true;
        }else if(id == R.id.action_top_ratings){
            initialiseMovies("top_rated");
            return true;
        }else if ( id == R.id.action_watchlist){
            Fragment favouriteMoviesFragment = new FavouritesMovieFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,favouriteMoviesFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isInternetAvailable(){
        boolean isConnected = false;
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
    private void initialiseMovies(String sortBy){
        FetchMovies fetchMoviesTask =new FetchMovies();
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String sortBy = sharedPreferences.getString(getString(R.string.pref_sort_by_key),getString(R.string.pref_sort_by_popular));
        fetchMoviesTask.execute(sortBy);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public class FetchMovies extends AsyncTask<String,Void,ArrayList<MovieDetail>>{
        private final String LOG_TAG = FetchMovies.class.getSimpleName();
        private String readableDateString(String dateStr){
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateRtn ="Not Found";
            try{
                Date date = shortenedDateFormat.parse(dateStr);
                SimpleDateFormat readableDate = new SimpleDateFormat("d MMM yyyy");
                dateRtn =readableDate.format(date);
                return dateRtn;
            }
            catch (ParseException e){
                return dateRtn;
            }
            finally {
                return dateRtn;
            }
        }
        private ArrayList<MovieDetail> getMoviesDetailsFromJson(String moviesJsonStr) throws JSONException{

            final ArrayList<MovieDetail> mDetails = new ArrayList<>();
            final String MY_ID = "id";
            final String MY_RESULTS = "results";
            final String MY_TITLE = "title";
            final String MY_OVERVIEW = "overview";
            final String MY_POSTER_PATH = "poster_path";
            final String MY_BACKDROP_PATH = "backdrop_path";
            final String MY_RELEASE_DATE = "release_date";
            final String MY_RATING = "vote_average";
            final String MY_VOTE_COUNT = "vote_count";
            final String MOVIES_POSTER_BASE_URL ="https://image.tmdb.org/t/p/w500";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(MY_RESULTS);
            for(int i = 0; i < moviesArray.length(); i++) {
                JSONObject movieDetail = moviesArray.getJSONObject(i);
                mDetails.add(new MovieDetail(
                        movieDetail.getLong(MY_ID),
                        movieDetail.getString(MY_TITLE),
                        MOVIES_POSTER_BASE_URL + movieDetail.getString(MY_POSTER_PATH),
                        MOVIES_POSTER_BASE_URL + movieDetail.getString(MY_BACKDROP_PATH),
                        movieDetail.getString(MY_OVERVIEW),
                        movieDetail.getDouble(MY_RATING),
                        movieDetail.getLong(MY_VOTE_COUNT),
                        readableDateString(movieDetail.getString(MY_RELEASE_DATE))
                ));
//                Log.v(LOG_TAG,"MovieDetailsR"+"https://image.tmdb.org/t/p/w500/"+movieDetail.getString(MY_POSTER_PATH));
            }
            return mDetails;
        }


        @Override
        protected ArrayList<MovieDetail> doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }
            HttpsURLConnection urlConnection = null;
            BufferedReader reader = null;
            String moviesJsonStr = null;
            String language ="en-US";
            String apiKey = BuildConfig.MOVIEDB_API_KEY;
            String type = params[0];
            Log.v(LOG_TAG,"Type  "+type);
            try{
                final String MOVIES_API_URL = "https://api.themoviedb.org/3/movie/"+type+"?";
                final String LANGUAGE_PARAM = "language";
               // final String SORT_ORDER_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIES_API_URL).buildUpon()
                        .appendQueryParameter(LANGUAGE_PARAM,language)
                 //       .appendQueryParameter(SORT_ORDER_PARAM,params[0])
                        .appendQueryParameter(API_KEY_PARAM,apiKey)
                        .build();
                Log.v(LOG_TAG,"Built Uri "+builtUri.toString());
                URL url = new URL(builtUri.toString());
                urlConnection =(HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null){
                    moviesJsonStr =null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if(buffer.length() == 0){
                    moviesJsonStr = null;
                }
                moviesJsonStr = buffer.toString();
                Log.v(LOG_TAG,"Movies Json Str =  "+moviesJsonStr.toString());
            }
            catch (IOException e){
                Log.e(LOG_TAG,"Error"+e);
                moviesJsonStr =null;
            }
            finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }
                    catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try{
                return getMoviesDetailsFromJson(moviesJsonStr);
            } catch (JSONException e){
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(ArrayList<MovieDetail> result) {
            moviesPosterAdapter.clear();
            for (MovieDetail s : result)
            {
                moviesPosterAdapter.add(s);
                Log.v(LOG_TAG,"OnPost"+s.getTitle());
            }

        }
    }

    @Override
    public void onStart() {
        if(isInternetAvailable()==true) {
            initialiseMovies("popular");
        }
        else {
            Toast.makeText(getActivity(),"Internet Connectivity Issue",Toast.LENGTH_SHORT).show();
        }
        super.onStart();

    }
}
