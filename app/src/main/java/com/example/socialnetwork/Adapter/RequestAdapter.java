package com.example.socialnetwork.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Model.FriendRequest;
import com.example.socialnetwork.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyHolder>{
    ArrayList<FriendRequest> requestArrayList;
    Context context;

    public RequestAdapter(ArrayList<FriendRequest> requestArrayList, Context context) {
        this.requestArrayList = requestArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_new_friend_request, parent, false);
        return new RequestAdapter.MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageBtn;
        TextView nameTv;
        ImageButton acceptRequestBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
