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
        MovieDetail movieDetail = (MovieDetail) i.getParcelableExtra("movie_detail_parcelable");
        Double a = movieDetail.getRating();
        String rating = a.toString();
        if(i!=null){
            ((TextView)findViewById(R.id.movie_title)).setText(movieDetail.getTitle());
            ((TextView)findViewById(R.id.movie_desc)).setText(movieDetail.getOverview());
            ((TextView)findViewById(R.id.movie_rating)).setText(rating);
            ((TextView)findViewById(R.id.movie_release_date)).setText(movieDetail.getReleaseDate());
            ImageView myImageView = (ImageView)findViewById(R.id.movie_poster);
            Glide
                    .with(this)
                    .load(movieDetail.getUrl())
                    .centerCrop()
                    .crossFade()
                    .into(myImageView);

        }
    }
}
