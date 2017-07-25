package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Precious on 16/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    final private ListItemClickListener mListClickListener;
    private List<Movie> list;
    private Context context;

    public interface ListItemClickListener {
        void onItemClick(int position);
    }

    public MovieAdapter(List <Movie> list, ListItemClickListener listener) {
        this.list = list;
        mListClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.movie_list_item;
        boolean attachImmediatelyToParent = false;

        View view = inflater.inflate(layoutForListItem, viewGroup, attachImmediatelyToParent);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.rv_image);
            itemView.setOnClickListener(this);
        }

        void bind(int index) {
            Movie movie = list.get(index);
            Picasso.with(context).load(movie.getMoviePosterPath())
                    .resize(185, 195).into(poster);
        }

        @Override
        public void onClick(View v) {
            int itemClickedIndex = getAdapterPosition();
            mListClickListener.onItemClick(itemClickedIndex);
        }
    }
}

