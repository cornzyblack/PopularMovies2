package com.example.android.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.Model.Review;
import com.example.android.popularmovies.R;

import java.util.List;

/**
 * Created by Precious on 7/22/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> list;

    public ReviewsAdapter(List<Review> listReviews){
        this.list = listReviews;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutForListItem = R.layout.reviews_list_item;
        boolean attachImmediatelyToParent = false;

        View view = inflater.inflate(layoutForListItem, parent, attachImmediatelyToParent);
        ReviewsViewHolder viewHolder = new ReviewsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.bind(list.get(position).getAuthor(), list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewAuthorName;
        TextView tvReviewContent;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            tvReviewAuthorName = (TextView) itemView.findViewById(R.id.tv_review_author_name);
            tvReviewContent = (TextView) itemView.findViewById(R.id.tv_review_content);
        }

        // TODO Set Review Content and Review Author name
        void bind(String reviewAuthorName, String reviewContent) {
            tvReviewAuthorName.setText(reviewAuthorName);
            tvReviewContent.setText(reviewContent);
        }

    }
}
