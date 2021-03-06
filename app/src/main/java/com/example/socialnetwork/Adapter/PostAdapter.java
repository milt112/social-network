package com.example.socialnetwork.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Activities.EditPostActivity;
import com.example.socialnetwork.Activities.PeopleActivity;
import com.example.socialnetwork.Activities.PostDetailActivity;
import com.example.socialnetwork.Model.Post;
import com.example.socialnetwork.Model.User;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder>{

    Context context;
    List<Post> postList;
    String mUid;

    boolean processLike;

    DatabaseReference likeRef;
    DatabaseReference postRef;
    DatabaseReference userRef;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
        mUid = FirebaseAuth.getInstance().getUid();
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_post, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String uid = postList.get(position).getUid();
        String pId = postList.get(position).getpId();
        String pTitle = postList.get(position).getpTitle();
        String pDescr = postList.get(position).getpDescr();
        String pImage = postList.get(position).getpImage();
        String pLike = postList.get(position).getLikeCount();
        String pComment = postList.get(position).getCommentCount();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pId));
        String postTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        DatabaseReference postedUserRef = userRef.child(uid);
        postedUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                holder.usName.setText(user.getName());
                try {
                    Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_default_img).into(holder.usPic);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.postTime.setText(postTime);
        holder.postTitle.setText(pTitle);
        holder.postDes.setText(pDescr);
        holder.postLike.setText(String.format("%s Likes", pLike));
        holder.postComment.setText(String.format("%s Comments", pComment));

        if (pImage.equals("noImage")) {
            holder.postImage.setVisibility(View.GONE);
        }
        else {

            try {
                Picasso.get().load(pImage).into(holder.postImage);
            }
            catch (Exception e) {

            }
        }

        if(context.getClass().equals(PostDetailActivity.class)
                ||context.getClass().equals(PeopleActivity.class)){
            holder.usPic.setActivated(false);
        }else{
            holder.usPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PeopleActivity.class);
                    intent.putExtra("uid", uid);
                    context.startActivity(intent);
                }
            });
        }

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions(holder.moreBtn, uid, mUid, pId, pImage);
            }
        });

        if(context.getClass().equals(PostDetailActivity.class)) {
            holder.commentBtn.setActivated(false);
            holder.commentBtn.setText("");
            holder.commentBtn.setClickable(false);
            holder.commentBtn.setCompoundDrawables(null, null, null, null);
        } else {
            holder.commentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailActivity.class);
                    intent.putExtra("postId", pId);
                    Log.d("!111111111", pId);
                    context.startActivity(intent);
                }
            });
        }

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processLike = true;
                DatabaseReference clickedPostLikeRef = likeRef.child(pId);
                clickedPostLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (processLike) {
                            if (snapshot.hasChild(mUid)) {
                                clickedPostLikeRef.child(mUid).removeValue();
                            } else {
                                //Like newLike = buildLike(mUid, pId); //UPDATE IN FUTURE
                                clickedPostLikeRef.child(mUid).setValue("Liked");
                            }
                            setLikeButton(holder, clickedPostLikeRef);
                            updateLikesCount(holder, pId,  clickedPostLikeRef);
                            processLike = false;
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    /*
    private Like buildLike(String mUid, String pId){
        Like like = new Like();
        like.setCreatedDate(String.valueOf(System.currentTimeMillis()));
        like.setuId(mUid);
        like.setpId(pId);
        return like;
    }
    */

    private void updateLikesCount(MyHolder holder, String pId, DatabaseReference clickedPostLikeRef) {
        DatabaseReference clickedPostRef = postRef.child(pId);
        clickedPostLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String likes = ""+ snapshot.getChildrenCount();
                clickedPostRef.child("likeCount").setValue(""+likes);
                holder.postLike.setText(String.format("%s Likes", likes));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setLikeButton(MyHolder holder, DatabaseReference clickedPostLikeRef) {
        clickedPostLikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(mUid)) {
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked, 0, 0, 0);
                    holder.likeBtn.setText("Liked");
                }
                else {
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    holder.likeBtn.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMoreOptions(ImageButton moreBtn, String uid, String mUid, String pId, String pImage) {
        PopupMenu popupMenu = new PopupMenu(context, moreBtn, Gravity.END);
        if(uid.equals(mUid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case 0 :
                        attemptDeletePost(pId, pImage);
                        break;
                    case 1 :
                        Intent intent = new Intent(context, EditPostActivity.class);
                        intent.putExtra("editPostId", pId);
                        context.startActivity(intent);
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void attemptDeletePost(String pId, String pImage) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Deleting post...");

        if(pImage.equals("noImage")) {
            deletePost(pId);
        } else {
            deletePost(pId, pImage);
        }
        progressDialog.dismiss();
    }

    private void deletePost(String pId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Posts").child(pId);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Delete successfully!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deletePost(String pId, String pImage) {
        String filePathAndName = "Posts/" + "post_" + pId;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(filePathAndName);
        storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                deletePost(pId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView usPic, postImage;
        TextView usName, postTime, postTitle, postDes, postLike, postComment;
        ImageButton moreBtn;
        Button likeBtn, commentBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            usPic = itemView.findViewById(R.id.uPic);
            postImage = itemView.findViewById(R.id.pImage);
            usName = itemView.findViewById(R.id.uName);
            postTime = itemView.findViewById(R.id.pTime);
            postTitle = itemView.findViewById(R.id.pTitle);
            postDes = itemView.findViewById(R.id.pDes);
            postLike = itemView.findViewById(R.id.pLike);
            postComment = itemView.findViewById(R.id.pComment);
            moreBtn = itemView.findViewById(R.id.more_btn);
            likeBtn = itemView.findViewById(R.id.like_btn);
            commentBtn = itemView.findViewById(R.id.comment_btn);

        }
    }
}
