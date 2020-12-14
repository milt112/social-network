package com.example.socialnetwork.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Model.Post;
import com.example.socialnetwork.PostActivity;
import com.example.socialnetwork.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.MyHolder>{

    Context context;
    List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_post, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String usid = postList.get(position).getUid();
        String usEmail = postList.get(position).getuEmail();
        String usName = postList.get(position).getuName();
        String usDp = postList.get(position).getuDp();
        String postId = postList.get(position).getpId();
        String postTitle = postList.get(position).getpTitle();
        String postDes = postList.get(position).getpDes();
        String postImage = postList.get(position).getpImage();
        String postTimeStamp = postList.get(position).getpTime();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(postTimeStamp));
        String postTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.uName.setText(usName);
        holder.pTime.setText(postTime);
        holder.pTitle.setText(postTitle);
        holder.pDes.setText(postDes);


        try {
            Picasso.get().load(usDp).placeholder(R.drawable.ic_default_img).into(holder.uPic);
        }
        catch (Exception e) {

        }

        try {
            Picasso.get().load(postImage).into(holder.pImage);
        }
        catch (Exception e) {

        }

        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();

            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();

            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();

            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView uPic, pImage;
        TextView uName, pTime, pTitle, pDes, pLike;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, shareBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPic = itemView.findViewById(R.id.uPic);
            pImage = itemView.findViewById(R.id.pImage);
            uName = itemView.findViewById(R.id.uName);
            pTime = itemView.findViewById(R.id.pTime);
            pTitle = itemView.findViewById(R.id.pTitle);
            pDes = itemView.findViewById(R.id.pDes);
            pLike = itemView.findViewById(R.id.pLike);
            moreBtn = itemView.findViewById(R.id.more_btn);
            likeBtn = itemView.findViewById(R.id.like_btn);
            commentBtn = itemView.findViewById(R.id.comment_btn);
            shareBtn = itemView.findViewById(R.id.share_btn);



        }
    }
}
