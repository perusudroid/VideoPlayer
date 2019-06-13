package com.perusudroid.player;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import java.util.List;

import static com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants.PlayerState.*;


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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d("VisibilityUtils", "viewType " + viewType);

        if (viewType == 2) {
            //loading
            return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflater_loading, parent, false));
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visibility_adapter_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = view.getResources().getDisplayMetrics().widthPixels - 180;
        return new Holder(view);
    }

    private class AdViewHolder extends RecyclerView.ViewHolder {
        public AdViewHolder(View inflate) {
            super(inflate);
        }

        public void bind() {

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Holder) {
            VisibilityItem item = mList.get(position);
            ((Holder) viewHolder).bind(item, position);
        } else if (viewHolder instanceof AdViewHolder) {
            ((AdViewHolder) viewHolder).bind();
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
            textWaveTitle = itemView.findViewById(R.id.video_id);
            imageViewItems = itemView.findViewById(R.id.thumbnail);
            playButton = itemView.findViewById(R.id.btnPlay);
            youTubePlayerView = itemView.findViewById(R.id.youtube_view);

            /*playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady() {
                            initializedYouTubePlayer.loadVideo(mList.get(getAdapterPosition()).getYoutubeVideo(), 0);
                        }

                        @Override
                        public void onStateChange(int state) {
                            super.onStateChange(state);
                            switch (state) {
                                case UNKNOWN:
                                    break;
                                case UNSTARTED:
                                    break;
                                case ENDED:
                                    youTubePlayerView.setVisibility(View.GONE);
                                    imageViewItems.setVisibility(View.VISIBLE);
                                    playButton.setVisibility(View.VISIBLE);
                                    break;
                                case PLAYING:
                                    youTubePlayerView.setVisibility(View.VISIBLE);
                                    imageViewItems.setVisibility(View.INVISIBLE);
                                    playButton.setVisibility(View.INVISIBLE);
                                    break;
                                case PAUSED:
                                    youTubePlayerView.setVisibility(View.GONE);
                                    imageViewItems.setVisibility(View.VISIBLE);
                                    playButton.setVisibility(View.VISIBLE);
                                    break;
                                case BUFFERING:
                                    break;
                                case VIDEO_CUED:
                                    break;

                            }
                        }
                    }), true);
                }
            });*/
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

            int color = itemView.getResources().getColor(android.R.color.white);
            textWaveTitle.setText(item.getTitle());
            itemView.setBackgroundColor(color);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }


}
