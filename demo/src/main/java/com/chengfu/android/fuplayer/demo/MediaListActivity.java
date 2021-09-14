package com.chengfu.android.fuplayer.demo;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chengfu.android.fuplayer.demo.bean.Media;
import com.chengfu.android.fuplayer.demo.ui.MediaListAdapter;

import java.util.ArrayList;


public class MediaListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<Media> dataList;
    MediaListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        dataList = getIntent().getParcelableArrayListExtra("list");

        listAdapter = new MediaListAdapter();
        listAdapter.submitList(dataList);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.media_list,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.playAll){
            Intent intent=new Intent(this, PlayerActivity.class);
            intent.putParcelableArrayListExtra("list",dataList);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
