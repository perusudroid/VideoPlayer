package com.perusudroid.player;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.List;


/**
 * Created by danylo.volokh on 06.01.2016.
 */

public class VisibilityUtilsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<VisibilityItem> mList;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public VisibilityUtilsAdapter(List<VisibilityItem> list) {
        mList = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 2) {
            //loading
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visibility_adapter_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = view.getResources().getDisplayMetrics().widthPixels;
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Holder) {
            VisibilityItem item = mList.get(position);
            ((Holder) viewHolder).bind(item, position);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void releaseCurrentItem(View view, int newActiveViewPosition1) {
        VisibilityItem item = mList.get(newActiveViewPosition1);
        item.deactivate(view, newActiveViewPosition1);
    }


    public class Holder extends RecyclerView.ViewHolder {

        private TextView textWaveTitle;
        private ImageView playButton, imageViewItems;
        private YouTubePlayerView youTubePlayerView;

        private Holder(View itemView) {
            super(itemView);
            textWaveTitle = itemView.findViewById(R.id.textViewTitle);
            imageViewItems = itemView.findViewById(R.id.imageViewItem);
            playButton = itemView.findViewById(R.id.btnPlay);
            youTubePlayerView = itemView.findViewById(R.id.youtube_view);
        }


        public void bind(VisibilityItem item, int position) {

            ((Activity) itemView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            if (item.getYoutubeVideo() != null) {
                Glide.with(itemView.getContext())
                        .load(item.getYoutubeImage()).
                        apply(new RequestOptions().override(width - 36, 200))
                        .into(imageViewItems);
            }

            int color = itemView.getResources().getColor(android.R.color.holo_blue_dark);
            textWaveTitle.setText("Position " + position);
            itemView.setBackgroundColor(color);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }
}
