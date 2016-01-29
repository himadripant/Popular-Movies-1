package com.adriwaas.nano.popularmoviesi;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by Himadri Pant on 28/12/2015.
 */
public class Movie implements Parcelable {

    String id;

    String title;

    @SerializedName("original_title") String originalTitle;

    @SerializedName("poster_path") String posterPath;

    @SerializedName("adult") boolean isAdult;

    String overview;

    @SerializedName("release_date") String releaseDate;

    @SerializedName("original_language") String originalLanguage;

    @SerializedName("backdrop_path") String backdropPath;

    double popularity;

    @SerializedName("vote_count") int voteCount;

    @SerializedName("video") boolean hasVideo;

    @SerializedName("vote_average") double voteAvg;

    @SerializedName("genre_ids") int[] genreIds;

    public Movie(String id, String title, String originalTitle, String posterPath, boolean isAdult,
                 String overview, String releaseDate, String originalLanguage, String backdropPath,
                 double popularity, int voteCount, boolean hasVideo, float voteAvg, int[] genreIds) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.isAdult = isAdult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.hasVideo = hasVideo;
        this.voteAvg = voteAvg;
        this.genreIds = genreIds;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.originalTitle = in.readString();
        this.posterPath = in.readString();
        this.isAdult = in.readByte() == 1;
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.originalLanguage = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readDouble();
        this.voteCount = in.readInt();
        this.hasVideo = in.readByte() == 1;
        this.voteAvg = in.readFloat();
        in.readIntArray(genreIds);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeByte((byte) (isAdult ? 1 : 0));
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalLanguage);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (hasVideo ? 1 : 0));
        dest.writeDouble(voteAvg);
        dest.writeIntArray(genreIds);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", isAdult=" + isAdult +
                ", overview='" + overview + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", popularity=" + popularity +
                ", voteCount=" + voteCount +
                ", hasVideo=" + hasVideo +
                ", voteAvg=" + voteAvg +
                ", genreIds=" + Arrays.toString(genreIds) +
                '}';
    }
}
