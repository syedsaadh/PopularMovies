package app.orion.com.popularmovies.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.orion.com.popularmovies.model.MovieDetail;

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
        Point size = new Point();
        int deviceWidth,imageSizeX;
        ((Activity)mContext).getWindowManager().getDefaultDisplay().getSize(size);
        deviceWidth = size.x;
        imageSizeX = deviceWidth/4;
        if(deviceWidth/2 < 400){
            imageSizeX = deviceWidth/2;
        }
        if (recycled == null) {
            myImageView = new ImageView(mContext);
        } else {
            myImageView = (ImageView) recycled;
        }
        myImageView.setLayoutParams(new GridView.LayoutParams(imageSizeX, 500));
        myImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        MovieDetail movieDetail = details.get(position);

        MovieDetilsStorageHelper movieDetilsStorageHelper = new MovieDetilsStorageHelper(mContext);
        Uri mThumb = movieDetilsStorageHelper.getBitmapFromDir(movieDetail.getPosterName(movieDetail.getPosterUrl()));
        if(mThumb != null){
            Glide
                    .with(mContext)
                    .load(mThumb)
                    .crossFade()
                    .into(myImageView);
        }else {
            Glide
                    .with(mContext)
                    .load(movieDetail.getPosterUrl())
                    .crossFade()
                    .into(myImageView);
        }
        return myImageView;
    }
}