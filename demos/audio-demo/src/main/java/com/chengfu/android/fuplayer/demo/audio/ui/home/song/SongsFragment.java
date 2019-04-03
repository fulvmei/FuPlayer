package com.chengfu.android.fuplayer.demo.audio.ui.home.song;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chengfu.android.fuplayer.demo.audio.R;
import com.chengfu.android.fuplayer.demo.audio.util.MusicUtil;
import com.chengfu.android.fuplayer.demo.audio.viewmodels.MainActivityViewModel;

import java.util.List;

public class SongsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SongsAdapter adapter;

    private MainActivityViewModel mainActivityViewModel;
    private SongsViewModel songsViewModel;


    public static SongsFragment newInstance(String tag) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString("tag", tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mainActivityViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        songsViewModel = ViewModelProviders.of(this).get(SongsViewModel.class);

        adapter = new SongsAdapter();

        recyclerView.setAdapter(adapter);

        if (getArguments().getString("tag").equals("在线")) {
            onHasData(MusicUtil.getNetMusicList());
        } else {
            new Thread(() -> getActivity().runOnUiThread(() -> {
                List<MediaDescriptionCompat> musicList = MusicUtil.getMusicList(getContext());
                onHasData(musicList);
            })).run();
        }
    }

    private void onHasData(List<MediaDescriptionCompat> musicList) {
        adapter.submitList(musicList);

        mainActivityViewModel.mediaSessionConnection.isConnected.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isConnected) {
                if (isConnected) {
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList(MusicContract.KEY_QUEUE_ITEMS, TypeCorver.musicListToMediaList(musicList));

//                    mainActivityViewModel.mediaSessionConnection.mediaController.addQueueItem(media);
//                    mainActivityViewModel.mediaSessionConnection.mediaController.sendCommand(MusicContract.COMMAND_SET_QUEUE_ITEMS, bundle, null);
                    adapter.setControls(mainActivityViewModel.mediaSessionConnection.mediaController);
//                    mainActivityViewModel.mediaSessionConnection.mediaController.getTransportControls().playFromMediaId(musicList.get(0).id, null);
//                    mainActivityViewModel.mediaSessionConnection.subscribe("1", new MediaBrowserCompat.SubscriptionCallback() {
//                        @Override
//                        public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
//                            super.onChildrenLoaded(parentId, children);
////                            adapter.submitList(castData(children));
//                        }
//                    });
                }
            }
        });
    }
}
