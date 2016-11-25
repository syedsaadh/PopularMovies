package app.orion.com.popularmovies.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.orion.com.popularmovies.R;
import app.orion.com.popularmovies.model.MovieDetail;
import app.orion.com.popularmovies.model.Reviews;

/**
 * Created by syedaamir on 28-10-2016.
 */

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Reviews> details = new ArrayList();
    public ReviewListAdapter(Context c, ArrayList<Reviews> details) {
        mContext = c;
        this.details = details;
    }
    private int lastPosition = -1;

    @Override
    public int getItemCount() {

        return details.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView authorTv, contentTv;
        public ViewHolder(View v){
            super(v);
            authorTv = (TextView) v.findViewById(R.id.review_heading);
            contentTv = (TextView) v.findViewById(R.id.review_subheading);

        }
    }
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item_reviews, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reviews reviewsDetails = details.get(position);
        holder.authorTv.setText(reviewsDetails.getAuthor());
        holder.contentTv.setText(reviewsDetails.getContent());
    }
}