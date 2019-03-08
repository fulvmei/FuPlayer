package com.chengfu.fuexoplayer2.demo.ui.audio;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chengfu.fuexoplayer2.demo.APP;
import com.chengfu.fuexoplayer2.demo.R;
import com.chengfu.fuexoplayer2.demo.StaticConfig;
import com.chengfu.fuexoplayer2.demo.bean.Media;
import com.chengfu.fuexoplayer2.demo.ui.video.ListVideoImageView;
import com.chengfu.fuexoplayer2.demo.ui.video.ListVideoPlayView;
import com.chengfu.fuexoplayer2.demo.ui.video.VideoControlView;
import com.chengfu.fuexoplayer2.demo.ui.video.VideoPlayErrorView;
import com.chengfu.fuexoplayer2.demo.ui.video.VideoPlayWithoutWifiView;
import com.chengfu.fuexoplayer2.demo.util.NetworkUtil;
import com.chengfu.fuexoplayer2.demo.util.VideoUtil;
import com.chengfu.fuexoplayer2.widget.FuPlayerView;
import com.chengfu.fuexoplayer2.widget.SampleBufferingView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;

import java.util.List;

public class AudioListAdapter extends RecyclerView.Adapter<AudioListAdapter.ViewHolder> {

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
//                        maybeStopPlay();
                    }
                } else if (autoPlayWhenItemVisible && (lastPlayPosition >= firstVisibleItemPosition && lastPlayPosition <= lastVisibleItemPosition)) {
                    notifyItemChanged(lastPlayPosition, PLAY_VH_PAYLOAD_ID);

                }
            }
        }
    };

    public AudioListAdapter() {
    }

    public void setData(List<Media> dataList) {
        this.dataList = dataList;
        lastPlayPosition = -1;
        notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        player = ExoPlayerFactory.newSimpleInstance(recyclerView.getContext());

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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_audio, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position) {
        Media item = dataList.get(position);

        vh.title.setText(item.getName());
        Glide.with(vh.image).load(item.getImage()).into(vh.image);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder vh, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(vh, position);
        } else if (payloads.get(0) instanceof Integer) {
//            Integer payloadId = (Integer) payloads.get(0);
//            if (payloadId.intValue() == PLAY_VH_PAYLOAD_ID.intValue()) {
//                vh.startPlay();
//            }
        }
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        TextView title;
        ImageView image;
        ImageView play;


        ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            image = itemView.findViewById(R.id.image);
            play = itemView.findViewById(R.id.play);


            play.setOnClickListener(v -> {

            });
        }
    }
}
