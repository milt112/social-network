package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Activities.MainActivity;
import com.example.socialnetwork.Activities.PeopleActivity;
import com.example.socialnetwork.Enum.TYPE_REQUEST;
import com.example.socialnetwork.Fragment.FriendFragment;
import com.example.socialnetwork.Model.FriendRequest;
import com.example.socialnetwork.Model.User;
import com.example.socialnetwork.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder>{
    DatabaseReference requestRef;
    DatabaseReference friendRef;
    FirebaseUser currentUser;
    Context context;
    List<User> userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false);
        requestRef = FirebaseDatabase.getInstance().getReference("Requests");
        friendRef = FirebaseDatabase.getInstance().getReference("Friends");
        currentUser = checkUserStatus();
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String userImage = userList.get(position).getImage();
        String userName = userList.get(position).getName();
        String userEmail = userList.get(position).getEmail();
        String userId = userList.get(position).getUid();

        holder.uName.setText(userName);
        holder.uEmail.setText(userEmail);
        try {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(holder.uAvt);
        }
        catch (Exception e) {

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + userEmail, Toast.LENGTH_SHORT).show();
            }
        });

        holder.uAvt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PeopleActivity.class);
                intent.putExtra("uid", userId);
                context.startActivity(intent);
            }
        });
        //add request friend button
        updateRequestButton(holder, currentUser.getUid(), userId);
    }

    private void sendRequestTo(MyHolder holder, String mUid, String uId) {
        FriendRequest currentUserRequest = buildRequest(mUid, uId, TYPE_REQUEST.SENDING);
        DatabaseReference currentUserRequestRef = friendRef.child(mUid);
        currentUserRequestRef.child(uId).setValue(currentUserRequest)
                .addOnSuccessListener(aVoid -> {
                    updateRequestOnAnotherUser(holder, mUid, uId);
                })
                .addOnFailureListener(e -> makeToast(""+e));

        }

    private void updateRequestOnAnotherUser(MyHolder holder, String mUid, String uId) {
        DatabaseReference anotherUserRequestRef = requestRef.child(uId);
        FriendRequest anotherUserRequest = buildRequest(uId, mUid, TYPE_REQUEST.WAITING_ACCEPT);
        anotherUserRequestRef.child(uId).setValue(anotherUserRequest)
                .addOnSuccessListener(aVoid -> {
                    makeToast("Added Request");
                    muteRequestButton(holder);
                })
                .addOnFailureListener(e -> {
                    makeToast(""+e);
                    friendRef.child(mUid).child(uId).removeValue();
                });
    }

    private void updateRequestButton(MyHolder holder, String mUid, String uId) {
        if(mUid == null || uId == null) {return;}
        friendRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(mUid).hasChild(uId)){
                    muteRequestButton(holder);
                } else {
                    addRequestButton(holder, mUid, uId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addRequestButton(MyHolder holder, String mUid, String uId) {
        holder.addFriendImageBtn.setVisibility(View.VISIBLE);
        holder.addFriendImageBtn.setClickable(true);
        holder.addFriendImageBtn.setOnClickListener(view -> {
            sendRequestTo(holder, mUid, uId);
        });
    }

    private void muteRequestButton(MyHolder holder) {
        holder.addFriendImageBtn.setClickable(false);
        holder.addFriendImageBtn.setVisibility(View.INVISIBLE);
    }

    private FriendRequest buildRequest(String mUid, String uId, TYPE_REQUEST typeRequest) {
        FriendRequest request = new FriendRequest();
        request.setmUid(mUid);
        request.setuId(uId);
        request.setCreatedDate(String.valueOf(System.currentTimeMillis()));
        request.setTypeRequest(typeRequest);
        return request;
    }

    protected FirebaseUser checkUserStatus() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser==null) {
            return null;
        }
        return currentUser;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView uAvt;
        TextView uName, uEmail;
        ImageButton addFriendImageBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uAvt = itemView.findViewById(R.id.avatar_fri);
            uName = itemView.findViewById(R.id.name_fri);
            uEmail = itemView.findViewById(R.id.email_fri);
            addFriendImageBtn = itemView.findViewById(R.id.send_friend_request_btn);
        }
    }

    protected void makeToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
