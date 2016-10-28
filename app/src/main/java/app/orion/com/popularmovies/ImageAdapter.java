package app.orion.com.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by syedaamir on 28-10-2016.
 */

public class ImageAdapter extends ArrayAdapter<MovieDetail> {
    private Context mContext;
    private int resource;
    private int imageViewResourceId;
    private ArrayList<MovieDetail> details = new ArrayList();
    public ImageAdapter(Context c,int resource,int imageViewResourceId,ArrayList details) {
        super(c,resource,imageViewResourceId,details);
        mContext = c;
        this.resource=resource;
        this.imageViewResourceId=imageViewResourceId;
        this.details = details;
    }

    public int getCount() {
       return details.size();
    }

    public long getItemId(int position) {
       return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View recycled, ViewGroup container) {
        final ImageView myImageView;
        final int deviceWidth;
        final  int deviceHeight;
        deviceHeight = ((Activity)mContext).getWindowManager().getDefaultDisplay().getHeight();
        deviceWidth = ((Activity)mContext).getWindowManager().getDefaultDisplay().getWidth();

        if (recycled == null) {
            myImageView = new ImageView(mContext);
            myImageView.setLayoutParams(new GridView.LayoutParams(deviceWidth/2,deviceHeight/2));
            myImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            myImageView = (ImageView) recycled;
        }

        MovieDetail movieDetail = details.get(position);
        Glide
                .with(mContext)
                .load(movieDetail.getUrl())
                .centerCrop()
                .crossFade()
                .into(myImageView);


        return myImageView;
    }

}