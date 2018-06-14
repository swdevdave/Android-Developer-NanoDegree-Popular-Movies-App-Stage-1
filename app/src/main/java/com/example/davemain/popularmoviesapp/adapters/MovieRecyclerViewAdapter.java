package com.example.davemain.popularmoviesapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.davemain.popularmoviesapp.R;
import com.example.davemain.popularmoviesapp.activities.DetailActivity;
import com.example.davemain.popularmoviesapp.model.Movies;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder> {

    private Context mContext;
    private List<Movies> mMovieData;

    public MovieRecyclerViewAdapter(Context mContext, List<Movies> mMovieData) {

        this.mContext = mContext;
        this.mMovieData = mMovieData;


    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.grid_item, parent, false);

        return new MovieViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {

        // Loading img from Internet with Glide
        Glide.with(mContext)
                .load(mMovieData.get(position).getmThumbnailImgUrl())
                .into(holder.thumbImg);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("title", mMovieData.get(position).getmTitle());
                intent.putExtra("overview", mMovieData.get(position).getmOverview());
                intent.putExtra("releaseDate", mMovieData.get(position).getmReleaseDate());
                intent.putExtra("userRating", mMovieData.get(position).getmUserRating());
                intent.putExtra("posterUrl", mMovieData.get(position).getmPosterUrl());


                String itemClicked = mMovieData.get(position).getmTitle();
                Toast.makeText(mContext, "You clicked: " + itemClicked, Toast.LENGTH_SHORT).show();

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMovieData.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView thumbImg;
        RelativeLayout parentLayout;

        MovieViewHolder(View itemView) {
            super(itemView);

            thumbImg = itemView.findViewById(R.id.posterImg);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }

}
