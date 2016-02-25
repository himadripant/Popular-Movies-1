package com.adriwaas.nano.popularmoviesi;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String MOVIE = "movie";

    private Movie mMovie = null;
    private View mMovieView = null;

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w342/";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(MOVIE)) {
            mMovie = getArguments().getParcelable(MOVIE);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
//                appBarLayout.setTitle(mItem.content);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return mMovieView = inflater.inflate(R.layout.movie_detail, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        ((TextView) mMovieView.findViewById(R.id.movieName)).setText(mMovie.originalTitle);
        Picasso.with(getContext()).load(BASE_IMAGE_URL + mMovie.posterPath)
                .into((ImageView) mMovieView.findViewById(R.id.movieImage));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(mMovie.releaseDate));
            String year = "Year: " + calendar.get(Calendar.YEAR);
            Log.i(TAG, year);
            ((TextView) mMovieView.findViewById(R.id.movieYear)).setText(year);
        } catch (ParseException e) {
            e.printStackTrace();
            mMovieView.findViewById(R.id.movieYear).setVisibility(View.GONE);
        }
        ((TextView) mMovieView.findViewById(R.id.movieLanguage)).setText("Language: " + mMovie.originalLanguage);
        ((TextView) mMovieView.findViewById(R.id.moviePopularity)).setText("Popularity: " + mMovie.popularity);
        ((TextView) mMovieView.findViewById(R.id.movieVoteCount)).setText("Votes: " + mMovie.voteCount);
        ((TextView) mMovieView.findViewById(R.id.movieOverview)).setText(mMovie.overview);
    }
}
