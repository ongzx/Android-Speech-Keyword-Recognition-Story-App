package com.example.chihiong.bedtimestories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chihi.ong on 18/10/15.
 */
public class StoryAdapter extends RecyclerView.Adapter <StoryAdapter.StoryViewHolder> {

    private static String LOG_TAG = StoryAdapter.class.getSimpleName();

    Context mContext;
    OnItemClickListener mItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public StoryAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public int getItemCount() {
        return new StoryData().storyList().size();
    }

    @Override
    public void onBindViewHolder (final StoryViewHolder storyViewHolder, int position) {

        final Story story = new StoryData().storyList().get(position);
        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), story.getImageResourceId(mContext));

        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int bgColor = palette.getVibrantColor(mContext.getResources().getColor(android.R.color.black));
                storyViewHolder.placeNameHolder.setBackgroundColor(bgColor);
            }
        });

        storyViewHolder.storyTitle.setText(story.title);
        Picasso.with(mContext).load(story.getImageResourceId(mContext)).into(storyViewHolder.storyPhoto);

    }

    @Override
    public StoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.storycard, viewGroup, false);
        return new StoryViewHolder(itemView);
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout placeHolder;
        public LinearLayout placeNameHolder;
        public TextView storyTitle;
        public ImageView storyPhoto;


        public StoryViewHolder (View v) {
            super(v);
            v.setOnClickListener(this);

            placeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            storyTitle = (TextView) itemView.findViewById(R.id.story_title);
            placeNameHolder = (LinearLayout) itemView.findViewById(R.id.placeNameHolder);
            storyPhoto = (ImageView) itemView.findViewById(R.id.story_image);

        }

        @Override
        public void onClick(View view) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getAdapterPosition());
            }


        }
    }

}
