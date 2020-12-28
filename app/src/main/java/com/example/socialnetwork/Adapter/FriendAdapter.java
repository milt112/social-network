package com.example.socialnetwork.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Model.FriendRequest;
import com.example.socialnetwork.R;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyHolder>{
    ArrayList<FriendRequest> friendArrayList;
    Context context;

    public FriendAdapter(ArrayList<FriendRequest> friendArrayList, Context context) {
        this.friendArrayList = friendArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public FriendAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_new_friend_request, parent, false);
        return new FriendAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return friendArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{

        public MyHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}