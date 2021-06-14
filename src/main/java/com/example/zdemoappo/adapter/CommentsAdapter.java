package com.example.zdemoappo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zdemoappo.R;
import com.example.zdemoappo.model.CommentsModel;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter {

    Context context;
    List<CommentsModel> list = new ArrayList<>();

    public CommentsAdapter(Context context, List<CommentsModel> list) {
        this.context = context;
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView commentsTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commentsTV = (TextView) itemView.findViewById(R.id.commentTv);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_list_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        CommentsModel comm = list.get(position);

        viewHolder.commentsTV.setText(comm.getComment());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
