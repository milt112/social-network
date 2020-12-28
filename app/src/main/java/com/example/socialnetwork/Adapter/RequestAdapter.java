package com.example.socialnetwork.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Enum.TYPE_REQUEST;
import com.example.socialnetwork.Model.FriendRequest;
import com.example.socialnetwork.Model.User;
import com.example.socialnetwork.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyHolder>{
    private DatabaseReference userRef;
    private DatabaseReference requestRef;
    private DatabaseReference friendRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference anotherUserRef;
    private DatabaseReference currentUserRequestRef;
    private DatabaseReference currentUserFriendRef;
    private DatabaseReference anotherUserRequestRef;
    private DatabaseReference anotherUserFriendRef;
    private ArrayList<FriendRequest> requestArrayList;
    private Context context;

    public RequestAdapter(ArrayList<FriendRequest> requestArrayList, Context context) {
        this.requestArrayList = requestArrayList;
        this.context = context;
        this.initDatabaseRef();
    }

    private void initDatabaseRef() {
        this.userRef = FirebaseDatabase.getInstance().getReference("Users");
        this.requestRef = FirebaseDatabase.getInstance().getReference("Requests");
        this.friendRef = FirebaseDatabase.getInstance().getReference("Friends");
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
        FriendRequest request = this.requestArrayList.get(position);
        this.anotherUserRef = this.userRef.child(request.getuId());
        this.currentUserFriendRef = this.friendRef.child(request.getmUid());
        this.currentUserRequestRef = this.requestRef.child(request.getmUid());
        this.anotherUserFriendRef = this.friendRef.child(request.getuId());
        this.anotherUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.nameTv.setText(user.getName());
                try {
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_default_img).into(holder.userAvatarIv);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.acceptRequestBtn.setVisibility(View.VISIBLE);
        holder.acceptRequestBtn.setClickable(true);
        holder.acceptRequestBtn.setOnClickListener(view -> {
            acceptRequest(holder, request.getmUid(), request.getuId());
        });
    }

    private void acceptRequest(MyHolder holder, String mUid, String uId) {
        if(mUid == null || uId == null) {return;}
        this.currentUserRequestRef.child(uId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    addConfirmToAnotherUser(holder, mUid, uId);
                })
                .addOnFailureListener(e -> {
                    this.makeToast(""+e);
                });
    }

    private void addConfirmToAnotherUser(MyHolder holder, String mUid, String uId) {
        this.anotherUserFriendRef.child(mUid).child("typeRequest").setValue(TYPE_REQUEST.FRIEND)
                .addOnSuccessListener(aVoid -> {
                    addConfirmToCurrentUser(holder, mUid, uId);
                })
                .addOnFailureListener(e -> {
                    this.makeToast(""+e);
                    this.anotherUserFriendRef.child(mUid).removeValue();
                    this.muteAcceptButton(holder);
                });
    }

    private void addConfirmToCurrentUser(MyHolder holder, String mUid, String uId) {
        FriendRequest confirmFriend = buildConfirm(mUid, uId, TYPE_REQUEST.FRIEND);
        this.currentUserFriendRef.child(uId).setValue(confirmFriend)
                .addOnSuccessListener(aVoid -> {
                    this.makeToast("~Congratulation You Are Friend Forever!~");
                    this.muteAcceptButton(holder);
                })
                .addOnFailureListener(e -> {
                    this.makeToast(""+e);
                    this.anotherUserFriendRef.child(mUid).removeValue();
                });
    }

    private void muteAcceptButton(MyHolder holder) {
        holder.acceptRequestBtn.setVisibility(View.INVISIBLE);
        holder.acceptRequestBtn.setClickable(false);
    }


    private FriendRequest buildConfirm(String mUid, String uId, TYPE_REQUEST typeRequest) {
        FriendRequest request = new FriendRequest();
        request.setmUid(mUid);
        request.setuId(uId);
        request.setCreatedDate(String.valueOf(System.currentTimeMillis()));
        request.setTypeRequest(typeRequest);
        return request;
    }


    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView userAvatarIv;
        TextView nameTv;
        ImageButton acceptRequestBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            userAvatarIv = itemView.findViewById(R.id.avatar_fri);
            nameTv = itemView.findViewById(R.id.name_fri);
            acceptRequestBtn = itemView.findViewById(R.id.send_friend_request_btn);
        }
    }

    protected void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
