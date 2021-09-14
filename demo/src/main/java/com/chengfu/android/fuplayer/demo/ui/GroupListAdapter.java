package com.chengfu.android.fuplayer.demo.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chengfu.android.fuplayer.demo.MediaListActivity;
import com.chengfu.android.fuplayer.demo.PlayerActivity;
import com.chengfu.android.fuplayer.demo.R;
import com.chengfu.android.fuplayer.demo.bean.Media;
import com.chengfu.android.fuplayer.demo.bean.MediaGroup;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    List<MediaGroup> dataList;

    public void submitList(List<MediaGroup> list) {
        dataList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MediaGroup group = dataList.get(position);
        holder.title.setText(group.getName());
        holder.subTitle.setText(group.mediaList.size() + "");
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            subTitle = itemView.findViewById(R.id.subTitle);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), MediaListActivity.class);
                intent.putParcelableArrayListExtra("list", dataList.get(getAdapterPosition()).mediaList);
                view.getContext().startActivity(intent);
            });
        }
    }
}
