package com.perusudroid.player;

import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.perusudroid.player.visibility.items.ListItem;
import com.perusudroid.player.visibility.utils.Config;
import com.perusudroid.player.visibility.utils.Logger;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;

import static com.pierfrancescosoffritti.youtubeplayer.player.PlayerConstants.PlayerState.*;

/**
 * Created by danylo.volokh on 06.01.2016.
 */
public class VisibilityItem implements ListItem {

    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
    private static final String TAG = VisibilityItem.class.getSimpleName();

    private String mTitle;
    private String youtubeVideo;
    private String youtubeImage;
    private int type;
    private final Rect mCurrentViewRect = new Rect();
    private ItemCallback mItemCallback;


    public VisibilityItem(int type) {
        this.type = type;
    }


    public VisibilityItem(String title, String videoId, String videoImage, ItemCallback callback, int type) {
        mTitle = title;
        this.youtubeVideo = videoId;
        this.youtubeImage = videoImage;
        this.mItemCallback = callback;
        this.type = type;
    }

    public interface ItemCallback {
        void makeToast(String text);

        void onActiveViewChangedActive(View newActiveView, int activeViewPosition, int visibility);
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getYoutubeImage() {
        return youtubeImage;
    }

    public void setYoutubeImage(String youtubeImage) {
        this.youtubeImage = youtubeImage;
    }


    @Override
    public int getVisibilityPercents(View view) {
        if (SHOW_LOGS) Logger.v(TAG, ">> getVisibilityPercents view " + view);

        int percents = 100;

        view.getLocalVisibleRect(mCurrentViewRect);
        if (SHOW_LOGS)
            Logger.v(TAG, "getVisibilityPercents mCurrentViewRect top " + mCurrentViewRect.top + ", left " + mCurrentViewRect.left + ", bottom " + mCurrentViewRect.bottom + ", right " + mCurrentViewRect.right);

        int height = view.getHeight();
        if (SHOW_LOGS) Logger.v(TAG, "getVisibilityPercents height " + height);

        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = (height - mCurrentViewRect.top) * 100 / height;
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }

        //  ( (TextView)  view.findViewById(R.id.tvPercent)).setText(String.valueOf(percents));

        // setVisibilityPercentsText(view, percents);

        if (SHOW_LOGS) Logger.v(TAG, "<< getVisibilityPercents, percents " + percents);

        return percents;
    }


    public String getYoutubeVideo() {
        return youtubeVideo;
    }

    public void setYoutubeVideo(String youtubeVideo) {
        this.youtubeVideo = youtubeVideo;
    }

    @Override
    public void setActive(View newActiveView, int newActiveViewPosition) {
        if (SHOW_LOGS) Logger.v(TAG, "setActive, newActiveViewPosition " + newActiveViewPosition);


        TextView textWaveTitle = newActiveView.findViewById(R.id.video_id);
        ImageView imageViewItems = newActiveView.findViewById(R.id.thumbnail);
        ImageView playButton = newActiveView.findViewById(R.id.btnPlay);
        YouTubePlayerView youTubePlayerView = newActiveView.findViewById(R.id.youtube_view);


        imageViewItems.setVisibility(View.GONE);
        youTubePlayerView.setVisibility(View.VISIBLE);
        playButton.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady() {
                        initializedYouTubePlayer.loadVideo(getYoutubeVideo(), 0);
                    }
                }), true);
            }
        }, 1000);


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady() {
                                initializedYouTubePlayer.loadVideo(getYoutubeVideo(), 0);
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
                }, 1000);
            }
        });

        mItemCallback.onActiveViewChangedActive(newActiveView, newActiveViewPosition, getVisibilityPercents(newActiveView));
    }


    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }


    @Override
    public void deactivate(View currentView, int position) {
        YouTubePlayerView youTubePlayerView = currentView.findViewById(R.id.youtube_view);
        if (youTubePlayerView != null) {
            youTubePlayerView.release();
            Log.d("VisibilityX", "Item Released");
        }
        mItemCallback.makeToast("Deactivate View");
    }
}
