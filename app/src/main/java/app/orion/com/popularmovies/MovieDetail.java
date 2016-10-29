package app.orion.com.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by syedaamir on 28-10-2016.
 */

public class MovieDetail implements Parcelable {
    private String title;
    private String url;
    private String overview;
    private double rating;
    private String releaseDate;

    public MovieDetail(String title, String url,String overview,double rating,String releaseDate){
        super();
        this.title=title;
        this.url=url;
        this.overview=overview;
        this.rating=rating;
        this.releaseDate =releaseDate;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    protected MovieDetail(Parcel in) {
        title = in.readString();
        url = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(overview);
        dest.writeDouble(rating);
        dest.writeString(releaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MovieDetail> CREATOR = new Parcelable.Creator<MovieDetail>() {
        @Override
        public MovieDetail createFromParcel(Parcel in) {
            return new MovieDetail(in);
        }

        @Override
        public MovieDetail[] newArray(int size) {
            return new MovieDetail[size];
        }
    };
}