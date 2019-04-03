package com.chengfu.android.fuplayer.demo.audio.ui.home.song;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chengfu.android.fuplayer.audio.MusicContract;
import com.chengfu.android.fuplayer.demo.audio.R;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private List<MediaDescriptionCompat> mList;
    private MediaControllerCompat controls;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaDescriptionCompat music = mList.get(position);
        holder.title.setText(music.getTitle());
        holder.artist.setText(music.getSubtitle());
        holder.duration.setText(music.getDescription());
    }

    public void setControls(MediaControllerCompat controls) {
        this.controls = controls;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void submitList(List<MediaDescriptionCompat> list) {
        if (mList == list) {
            return;
        }
        mList = list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView albumArt;
        TextView title;
        TextView artist;
        TextView duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumArt = itemView.findViewById(R.id.albumArt);
            title = itemView.findViewById(R.id.song_title);
            artist = itemView.findViewById(R.id.song_artist);
            duration = itemView.findViewById(R.id.song_duration);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (controls != null) {
                        MediaDescriptionCompat music = mList.get(getAdapterPosition());
                        Bundle bundle = new Bundle();
                        ArrayList<MediaDescriptionCompat> list = new ArrayList<>();
                        list.add(music);
                        bundle.putParcelableArrayList(MusicContract.KEY_QUEUE_ITEMS, list);
                        controls.sendCommand(MusicContract.COMMAND_SET_QUEUE_ITEMS, bundle, null);
                        controls.getTransportControls().playFromMediaId(music.getMediaId(), null);
                    }

                }
            });

        }
    }
}
