package app.orion.com.popularmovies.ui;

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
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.orion.com.popularmovies.BuildConfig;
import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.model.Reviews;
import app.orion.com.popularmovies.util.ReviewListAdapter;

/**
 * Created by syedaamir on 23-11-2016.
 */

public class EmptyErrorFragment extends Fragment {
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_empty_info, container, false);
        Button goToBackBtn = (Button)rootView.findViewById(R.id.action_btn);
        goToBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFragment();
            }
        });
        return rootView;
    }
    public void closeFragment(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .commit();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

}
