package com.chengfu.fuexoplayer2.demo.ui.video;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chengfu.android.fuplayer.ext.exo.ExoPlayer;
import com.chengfu.android.fuplayer.ui.FuPlayerView;
import com.chengfu.android.fuplayer.ui.SampleBufferingView;
import com.chengfu.fuexoplayer2.demo.APP;
import com.chengfu.fuexoplayer2.demo.R;
import com.chengfu.fuexoplayer2.demo.StaticConfig;
import com.chengfu.fuexoplayer2.demo.bean.Media;
import com.chengfu.fuexoplayer2.demo.util.NetworkUtil;
import com.chengfu.fuexoplayer2.demo.util.VideoUtil;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private static final Integer PLAY_VH_PAYLOAD_ID = 1;
    private List<Media> dataList;
    private ExoPlayer player;
    private int lastPlayPosition = -1;
    private ViewHolder currentPlayVH;
    private boolean autoPlayWhenItemVisible;

    public final OnScrollListener scrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if ((lastPlayPosition < 0 || lastPlayPosition > getItemCount() - 1)
                    && currentPlayVH == null) {
                return;
            }

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (currentPlayVH != null) {
                    int currentPlayPosition = currentPlayVH.getAdapterPosition();
                    if (currentPlayPosition < firstVisibleItemPosition || currentPlayPosition > lastVisibleItemPosition) {
                        maybeStopPlay();
                    }
                } else if (autoPlayWhenItemVisible && (lastPlayPosition >= firstVisibleItemPosition && lastPlayPosition <= lastVisibleItemPosition)) {
                    notifyItemChanged(lastPlayPosition, PLAY_VH_PAYLOAD_ID);

                }
            }
        }
    };

    public VideoListAdapter() {
    }

    public void setData(List<Media> dataList) {
        this.dataList = dataList;
        lastPlayPosition = -1;
        notifyDataSetChanged();
    }

    public boolean isAutoPlayWhenItemVisible() {
        return autoPlayWhenItemVisible;
    }

    public void setAutoPlayWhenItemVisible(boolean autoPlayWhenItemVisible) {
        this.autoPlayWhenItemVisible = autoPlayWhenItemVisible;
    }

    public void maybeStartPlay() {
        if (currentPlayVH == null) {
            return;
        }
        currentPlayVH.startPlay();
    }


    public void maybePausePlay() {
        if (currentPlayVH == null) {
            return;
        }
        currentPlayVH.pausePlay();
    }

    public void maybeResumePlay() {
        if (currentPlayVH == null) {
            return;
        }
        currentPlayVH.resumePlay();
    }

    public void maybeStopPlay() {
        if (currentPlayVH == null) {
            return;
        }
        currentPlayVH.stopPlay();
        currentPlayVH = null;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        player = new ExoPlayer(recyclerView.getContext());

        recyclerView.addOnScrollListener(scrollListener);

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        player.release();
        player = null;

        recyclerView.removeOnScrollListener(scrollListener);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        Media item = dataList.get(position);

        vh.title.setText(item.getName());
        Glide.with(vh.imageView).load(item.getImage()).into(vh.imageView.getImage());

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(vh, position);
        } else if (payloads.get(0) instanceof Integer) {
            Integer payloadId = (Integer) payloads.get(0);
            if (payloadId.intValue() == PLAY_VH_PAYLOAD_ID.intValue()) {
                vh.startPlay();
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        FuPlayerView playerView;
        VideoControlView controlView;
        TextView title;
        SampleBufferingView loadingView;
        VideoPlayErrorView errorView;
        ListVideoPlayView playView;
        ListVideoImageView imageView;
        VideoPlayWithoutWifiView noWifiView;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            playerView = itemView.findViewById(R.id.playerView);
            controlView = itemView.findViewById(R.id.controlView);
            title = itemView.findViewById(R.id.title);

            noWifiView = itemView.findViewById(R.id.noWifiView);

            controlView.setShowBottomProgress(true);

            loadingView = new SampleBufferingView(itemView.getContext());
            errorView = new VideoPlayErrorView(itemView.getContext());
            playView = new ListVideoPlayView(itemView.getContext());
            imageView = new ListVideoImageView(itemView.getContext());


            playerView.addStateView(imageView);
            playerView.addStateView(loadingView);
            playerView.addStateView(playView);
            playerView.addStateView(errorView);

            playView.setOnPlayerClickListener(v -> {
                maybeStopPlay();
                startPlay();
            });

            noWifiView.setOnPlayClickListener(v -> {
                StaticConfig.PLAY_VIDEO_NO_WIFI = true;
                maybeStopPlay();
                startPlay();
            });
        }

        boolean canPlay() {
            if (!NetworkUtil.isConnected(APP.application)) {
                Toast.makeText(APP.application, "网络不可用", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (NetworkUtil.getNetWorkType(APP.application) != NetworkUtil.NETWORK_WIFI && !StaticConfig.PLAY_VIDEO_NO_WIFI) {
                playView.setVisibility(View.GONE);
                noWifiView.show();
                return false;
            }
            return true;
        }

        void startPlay() {
            currentPlayVH = this;
            lastPlayPosition = getAdapterPosition();

            if (!canPlay()) {
                return;
            }

            playerView.setPlayer(player);
            controlView.setPlayer(player);

            player.setDataSource(dataList.get(getAdapterPosition()).getPath());
            player.setPlayWhenReady(true);
        }

        void pausePlay() {
            if (player.getPlayWhenReady()) {
                player.setPlayWhenReady(false);
            }
        }

        void resumePlay() {
            if (!player.getPlayWhenReady()) {
                player.setPlayWhenReady(true);
            }
        }

        void stopPlay() {
            playerView.setPlayer(null);
            controlView.setPlayer(null);
            playView.setVisibility(View.VISIBLE);
            noWifiView.hide();
            player.stop();
        }
    }
}
