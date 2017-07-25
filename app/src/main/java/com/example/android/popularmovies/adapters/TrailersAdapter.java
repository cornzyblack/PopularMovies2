package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Trailer;
import com.example.android.popularmovies.R;

import java.util.List;

/**
 * Created by Precious on 7/22/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {
    final private ListItemClickListener mListItemClickListener;
    private List<Trailer> list;

    // Interface that helps with clicking Items
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public TrailersAdapter(List<Trailer> trailerList, ListItemClickListener listener) {
        this.list = trailerList;
        mListItemClickListener = listener;
    }

    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.trailers_list_item;
        boolean attachImmediatelyToParent = false;

        View view = inflater.inflate(layoutForListItem, parent, attachImmediatelyToParent);
        TrailersViewHolder viewHolder = new TrailersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {
        holder.bind(list.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTrailerSite;
        ImageView playButton;

        public TrailersViewHolder(View itemView) {
            super(itemView);
            tvTrailerSite = (TextView) itemView.findViewById(R.id.tv_trailer_site);
            playButton = (ImageView) itemView.findViewById(R.id.play_button);
            itemView.setOnClickListener(this);
        }

        void bind(String trailerSite) {
            tvTrailerSite.setText(trailerSite);
        }

        @Override
        public void onClick(View v) {
            int itemClickedIndex = getAdapterPosition();
            mListItemClickListener.onListItemClick(itemClickedIndex);
        }
    }
}
