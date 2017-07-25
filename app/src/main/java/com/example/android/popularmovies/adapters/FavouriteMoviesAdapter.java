package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.FavouriteMovieContract;

/**
 * Created by Precious on 7/25/2017.
 */

public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteMovieViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public FavouriteMoviesAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        mCursor = cursor;
    }

    @Override
    public FavouriteMovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favourite_list_item, parent, false);
        return new FavouriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteMovieViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        // Call getString on the cursor to get the Movie's name
        String name = mCursor.getString(mCursor.getColumnIndex(FavouriteMovieContract.MovieEntry.COLUMN_MOVIE_NAME));

        holder.movieName.setText(name);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class FavouriteMovieViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        TextView movieId;

        public FavouriteMovieViewHolder(View viewItem) {
            super(viewItem);
            movieName = (TextView) viewItem.findViewById(R.id.tv_fav_movie_name);
        }
    }
}
