package app.orion.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by syedaamir on 28-10-2016.
 */

public class MovieDetail implements Parcelable {
    public long mid;
    private String title;
    private String posterUrl;
    private String backdropUrl;
    private String overview;
    private double rating;
    private String releaseDate;
    private String trailerPath = null;
    private int isFavourite;
    private long voteCount;
    private String posterName;
    private String backdropName;
    private String movie_base_url ="https://image.tmdb.org/t/p/w500/";

    public MovieDetail(long mid, String title, String posterUrl, String backdropUrl, String overview,double rating,long voteCount, String releaseDate){
        super();
        this.mid = mid;
        this.title=title;
        this.posterUrl=posterUrl;
        this.backdropUrl=backdropUrl;
        this.overview=overview;
        this.rating=rating;
        this.voteCount =voteCount;
        this.releaseDate =releaseDate;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }
    public String getPosterUrl() {
        return posterUrl;
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

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public int getIsFavourite() {
        return isFavourite;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public void setIsFavourite(int isFavourite) {
        this.isFavourite = isFavourite;
    }

    public void setTrailerPath(String trailerPath) {
        this.trailerPath = trailerPath;
    }

    public String getBackdropName(String backdropUrl) {
        this.backdropName = backdropUrl.replace(movie_base_url,"");
        return backdropName;
    }

    public String getPosterName(String posterUrl) {
        this.posterName = posterUrl.replace(movie_base_url,"");
        return posterName;
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

    public void setPosterUrl(String url) {
        this.posterUrl = posterUrl;
    }

    protected MovieDetail(Parcel in) {
        mid = in.readLong();
        title = in.readString();
        posterUrl = in.readString();
        backdropUrl = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        voteCount = in.readLong();
        releaseDate = in.readString();
        trailerPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mid);
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(backdropUrl);
        dest.writeString(overview);
        dest.writeDouble(rating);
        dest.writeLong(voteCount);
        dest.writeString(releaseDate);
        dest.writeString(trailerPath);
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