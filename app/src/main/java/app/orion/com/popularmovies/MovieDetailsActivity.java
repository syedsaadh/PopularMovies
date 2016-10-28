package app.orion.com.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.renderscript.Double2;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Movie Details");
        Intent i =this.getIntent();
        String MY_TITLE = "title";
        String MY_OVERVIEW = "overview";
        String MY_POSTER_PATH = "poster_path";
        String MY_RELEASE_DATE = "release_date";
        String MY_RATING = "vote_average";
        Double a = i.getDoubleExtra(MY_RATING,0);
        String aa = a.toString();
        if(i!=null){
            ((TextView)findViewById(R.id.movie_title)).setText(i.getStringExtra(MY_TITLE));
            ((TextView)findViewById(R.id.movie_desc)).setText(i.getStringExtra(MY_OVERVIEW));
            ((TextView)findViewById(R.id.movie_rating)).setText(aa);
            ((TextView)findViewById(R.id.movie_release_date)).setText(i.getStringExtra(MY_RELEASE_DATE));
            ImageView myImageView = (ImageView)findViewById(R.id.movie_poster);
            Glide
                    .with(this)
                    .load(i.getStringExtra(MY_POSTER_PATH))
                    .centerCrop()
                    .crossFade()
                    .into(myImageView);

        }
    }
}
