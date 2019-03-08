package com.chengfu.fuexoplayer2.demo;

import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.chengfu.fuexoplayer2.MusicService;
import com.chengfu.fuexoplayer2.demo.adapter.MediaGroupListAdapter;
import com.chengfu.fuexoplayer2.demo.bean.Media;
import com.chengfu.fuexoplayer2.demo.bean.MediaGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ExpandableListView.OnChildClickListener {


    private ExpandableListView expandableListView;

    private MediaGroupListAdapter mediaGroupListAdapter;
    private List<MediaGroup> mediaGroupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expandableListView = findViewById(R.id.expandableListView);

        expandableListView.setOnChildClickListener(this);

        mediaGroupList = getMediaGroupList();
        mediaGroupListAdapter = new MediaGroupListAdapter(mediaGroupList);

        expandableListView.setAdapter(mediaGroupListAdapter);

        mediaGroupListAdapter.notifyDataSetChanged();


        new MediaBrowserCompat(
                this,
                new ComponentName(this, MusicService.class),//绑定浏览器服务
                new MediaBrowserCompat.ConnectionCallback(),//设置连接回调
                null
        ).connect();
    }

    private List<MediaGroup> getMediaGroupList() {
        List<MediaGroup> mediaGroups = new ArrayList<>();
        JSONArray ja = null;
        try {
            ja = new JSONArray(getMediaGroupListString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ja != null) {
            for (int i = 0; i < ja.length(); i++) {
                try {
                    mediaGroups.add(parsedMediaGroup(ja.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return mediaGroups;
    }

    private MediaGroup parsedMediaGroup(JSONObject jo) {
        MediaGroup mediaGroup = null;
        if (jo != null) {
            mediaGroup = new MediaGroup();
            mediaGroup.setName(jo.optString("name"));
            mediaGroup.setMediaList(parsedMediaList(jo.optJSONArray("media_list")));
        }
        return mediaGroup;
    }

    private List<Media> parsedMediaList(JSONArray ja) {
        List<Media> mediaList = null;
        if (ja != null) {
            mediaList = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                try {
                    mediaList.add(parsedMedia(ja.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return mediaList;
    }

    private Media parsedMedia(JSONObject jo) {
        Media media = null;
        if (jo != null) {
            media = new Media();
            media.setName(jo.optString("name"));
            media.setPath(jo.optString("path"));
            media.setType(jo.optString("type"));
            media.setTag(jo.optString("tag"));
        }
        return media;
    }

    private String getMediaGroupListString() {
        InputStream inputStream = null;
        String media_list = null;
        try {
            inputStream = getAssets().open("media_list.json");
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            media_list = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return media_list;
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                int childPosition, long id) {
        Media media = mediaGroupList.get(groupPosition).getMediaList().get(childPosition);
        if ("audio".equals(media.getType())) {
            Intent intent = new Intent(this, AudioPlayerActivity.class);
            intent.putExtra("media", media);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, VideoPlayerActivity.class);
            intent.putExtra("media", media);
            startActivity(intent);
        }
        return true;
    }
}
