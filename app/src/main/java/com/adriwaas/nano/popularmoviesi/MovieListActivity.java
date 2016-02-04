package com.adriwaas.nano.popularmoviesi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adriwaas Works - Himadri Pant
 */


/**
 * An activity representing a list of Movies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MovieDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class MovieListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private final List<Movie> mMovies = new ArrayList<>();

    private final static String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w92/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        new FetchMovieListTask().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mMovies));
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Movie> mValues;

        public SimpleItemRecyclerViewAdapter(List<Movie> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            Picasso.with(getBaseContext()).load(BASE_IMAGE_URL + holder.mItem.posterPath)
                    .resize(50, 50).into(holder.mImage);
//            holder.mIdView.setText(mValues.get(position).id);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        MovieDetailFragment fragment = new MovieDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(MovieDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView mImage;
            public Movie mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImage = (ImageView) view.findViewById(R.id.image);
            }
        }
    }

    private class FetchMovieListTask extends AsyncTask<Void, Void, Movie[]> {

        final String TAG = FetchMovieListTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader reader = null;
            String responseJson = null;
            Movie[] movies = null;

            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String REQUEST_SORT = "sort_by";
                final String SORT_BY_POPULARITY = "popularity.desc";
                final String APPID_PARAM = "api_key";
                final String API_KEY = "7f4efd471a8ad17ebc13eba127e770d8";

                Uri buildUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                                    .appendQueryParameter(REQUEST_SORT, SORT_BY_POPULARITY)
                                    .appendQueryParameter(APPID_PARAM, API_KEY)
                                    .build();
                URL url = new URL(buildUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                responseJson = buffer.toString();
                Log.v(TAG, "json response:: " + responseJson);
                JSONObject jsonObject = new JSONObject(responseJson);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                Gson gson = new Gson();
                movies = gson.fromJson(jsonArray.toString(), Movie[].class);
                Log.v(TAG, "Movie object :: " + movies[0]);
            } catch (IOException ioex) {
                Log.e(TAG, ioex.getLocalizedMessage(), ioex);
            } catch (JSONException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
            return movies;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            for (Movie movie : movies)
                mMovies.add(movie);
//            mMovies.addAll(Arrays.asList(movies));
        }
    }
}
