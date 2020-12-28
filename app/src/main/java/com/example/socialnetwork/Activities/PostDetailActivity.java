package com.example.socialnetwork.Activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.socialnetwork.Adapter.CommentAdapter;
import com.example.socialnetwork.Adapter.PostAdapter;
import com.example.socialnetwork.Model.Comment;
import com.example.socialnetwork.Model.Post;
import com.example.socialnetwork.Model.User;
import com.example.socialnetwork.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity {
    //Reference
    private DatabaseReference userRef;
    private DatabaseReference postsRef;
    private DatabaseReference commentsRef;
    //private DatabaseReference likesRef;
    //Current user info
    private User currentUserInfo;
    //Clicked Post
    private ArrayList<Post> postArrayList;
    private ArrayList<Comment> commentArrayList;
    //RecyclerView
    private LinearLayoutManager commentLayoutManager;
    private LinearLayoutManager postLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private CommentAdapter commentAdapter;
    private PostAdapter postAdapter;
    private RecyclerView commentRecyclerView, postRecyclerView;
    //post on click
    private String postId;
    //Edit comment view
    private EditText commentEt;
    private ImageButton sendBtn;
    private CircularImageView cAvatarIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_post_detail);
        //Check current user status
        this.checkUserStatus();

        //Initial
        this.initActionBar();
        this.initField();
        this.initRef();
        this.initView();

        //Setup
        this.setUpViewProperties();
        this.setUpOnClick();

        //Loading
        this.loadUserInfo();
        this.loadClickedPost();
        this.loadComments();
    }

    private void initRef() {
        this.userRef = FirebaseDatabase.getInstance().getReference("Users");
        this.postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        this.commentsRef = FirebaseDatabase.getInstance().getReference("Comments");
        //this.likesRef = FirebaseDatabase.getInstance().getReference("Likes");
    }

    private void initActionBar() {
        this.setActionBarTitle("Post Detail","SignedIn as "+ currentUser.getEmail());
        this.actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initView() {
        //user view
        this.cAvatarIv = findViewById(R.id.cAvatarIv);

        //comment post
        this.commentEt = findViewById(R.id.commentEt);
        this.sendBtn = findViewById(R.id.sendBtn);

        //Comment Recycler View
        this.commentRecyclerView = findViewById(R.id.comment_recycler_view);
        this.postRecyclerView = findViewById(R.id.post_comment_recyclerView);
    }

    private void initField() {
        //Extra
        this.postId = this.getIntent().getStringExtra("postId");
        //clickedPost for postAdapter
        this.postArrayList = new ArrayList<>();
        this.postArrayList.add(new Post());
        //Array for commentAdapter
        this.commentArrayList = new ArrayList<>();
        //Adapter
        this.commentAdapter = new CommentAdapter(commentArrayList, this);
        this.postAdapter = new PostAdapter(this, postArrayList);
        //Layout manager
        this.commentLayoutManager = new LinearLayoutManager(this);
        this.postLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        //Decoration
        this.dividerItemDecoration = new DividerItemDecoration(this, commentLayoutManager.getOrientation());
    }

    private void setUpViewProperties() {
        //layout manager
        this.commentLayoutManager.setStackFromEnd(true);
        this.commentLayoutManager.setReverseLayout(true);
        //Comment Recycler view
        this.commentRecyclerView.setHasFixedSize(true);
        this.commentRecyclerView.setLayoutManager(commentLayoutManager);
        this.commentRecyclerView.addItemDecoration(dividerItemDecoration);
        //Post Recycler view
        this.postRecyclerView.setHasFixedSize(true);
        this.postRecyclerView.setLayoutManager(postLayoutManager);
    }

    private void loadImage(String picUri, ImageView imageViewHolder){
        try{
            Picasso.get().load(picUri).placeholder(R.drawable.ic_default_img).into(imageViewHolder);
        }
        catch(Exception e) {
            makeToast(""+e);
        }
    }

    private void loadComments() {
        DatabaseReference clickedPostCommentRef = this.commentsRef.child(this.postId);
        clickedPostCommentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Comment comment = ds.getValue(Comment.class);
                    commentArrayList.add(comment);
                }
                commentRecyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostDetailActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadClickedPost() {
        DatabaseReference clickedPostRef = postsRef.child(this.postId);
        clickedPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postArrayList.set(0, dataSnapshot.getValue(Post.class));
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeToast("Fail loading clicked post...");

            }
        });

    }

    private void loadUserInfo() {
        DatabaseReference currentUserRef = userRef.child(currentUser.getUid());
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserInfo = snapshot.getValue(User.class);
                if (currentUserInfo != null) {
                    loadImage(currentUserInfo.getImage(), cAvatarIv);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpOnClick() {

        //Send button on comment post bar
        this.sendBtn.setOnClickListener(view -> postComment());
    }


    private void postComment() {
        //progress bar
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding new comment..");

        //Build Comment
        Comment newComment = buildComment();
        if(newComment == null){
            return;
        }
        //Add Comment into database
        DatabaseReference clickedPostCommentRef = commentsRef.child(postId);
        DatabaseReference postedCommentRef = clickedPostCommentRef.child(newComment.getCommentId());
        postedCommentRef.setValue(newComment.toMap())
                .addOnSuccessListener(aVoid -> {
                    updateCommentsCount();
                    makeToast("Comment added");
                })
                .addOnFailureListener(e -> makeToast(""+e.getMessage()));

        progressDialog.dismiss();
        this.commentEt.setText("");
    }

    private Comment buildComment(){
        Comment comment = new Comment();
        //Current user
        comment.setUserId(this.currentUser.getUid());
        //Time
        comment.setTime(String.valueOf(System.currentTimeMillis()));
        //Comment id
        comment.setCommentId(String.format("%s%s", comment.getTime(), comment.getUserId()));
        //Comment description
        String commentDescription = commentEt.getText().toString().trim();
        if(commentDescription.isEmpty()){
            makeToast("Comment is empty..");
            return null;
        } else {
            comment.setDescription(commentDescription);
        }
        //Parent post id
        comment.setPostId(postId);
        return comment;
    }


    private void updateCommentsCount() {
        DatabaseReference clickedPostRef = postsRef.child(postId);
        DatabaseReference clickedPostCommentRef = commentsRef.child(postId);
        clickedPostCommentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postArrayList.get(0).setCommentCount(""+ snapshot.getChildrenCount());
                clickedPostRef.child("commentCount").setValue(""+postArrayList.get(0).getCommentCount());
                loadClickedPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_post).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


}