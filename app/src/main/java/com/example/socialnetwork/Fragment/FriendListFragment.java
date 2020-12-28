package com.example.socialnetwork.Fragment;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Activities.PostDetailActivity;
import com.example.socialnetwork.Adapter.CommentAdapter;
import com.example.socialnetwork.Adapter.FriendAdapter;
import com.example.socialnetwork.Adapter.RequestAdapter;
import com.example.socialnetwork.Adapter.UserAdapter;
import com.example.socialnetwork.Model.Comment;
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

import java.util.ArrayList;
import java.util.List;

public class FriendListFragment extends Fragment {
    //Recycler view for friend list
    RecyclerView friendListRecyclerView;
    FriendAdapter FriendAdapter;
    ArrayList<FriendRequest> friendList; // FRIEND
    //Recycler view for friend Request
    RecyclerView friendRequestRecyclerView;
    LinearLayoutManager friendRequestLayoutManager;
    RequestAdapter friendRequestAdapter;
    ArrayList<FriendRequest> newFriendRequestList;
    //Check current user
    FirebaseUser currentUser;
    //Firebase Database Reference
    DatabaseReference userRef;
    DatabaseReference requestRef;
    DatabaseReference friendRef;
    DatabaseReference currentUserRequestRef;
    DatabaseReference currentUserFriendRef;
    DatabaseReference anotherUserRequestRef;
    DatabaseReference anotherUserFriendRef;
    //Search menu item
    MenuItem searchItem;
    SearchView searchView;

    public FriendListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        //Initial
        this.initField();
        this.initView(view);
        this.initDataBaseReference();
        this.initFireBaseAuth();
        //Get current User
        this.getCurrentUserStatus();
        this.getCurrentUser();
        //Setup
        this.setUpViewConfig();

        //Loading
        this.loadAllFriend();
        this.loadAllRequest();

        return view;
    }

    private void loadAllRequest() {
        this.currentUserRequestRef = this.requestRef.child(currentUser.getUid());
        this.currentUserRequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newFriendRequestList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FriendRequest request = ds.getValue(FriendRequest.class);
                    newFriendRequestList.add(request);
                }
                friendRequestRecyclerView.setAdapter(friendRequestAdapter);
             }
             @Override
             public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initField() {
        this.newFriendRequestList = new ArrayList<>();
        this.friendRequestAdapter = new RequestAdapter(newFriendRequestList, getActivity());

    }

    private void initView(View view) {
        this.friendRequestRecyclerView = view.findViewById(R.id.friend_request_recycler_view);
        this.friendRequestLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
    }

    private void initDataBaseReference() {
        this.userRef = FirebaseDatabase.getInstance().getReference("Users");
        this.requestRef = FirebaseDatabase.getInstance().getReference("Requests");
        this.friendRef = FirebaseDatabase.getInstance().getReference("Friends");
    }

    private void initFireBaseAuth() {
        this.currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void getCurrentUserStatus() {

    }

    private void getCurrentUser() {

    }

    private void setUpViewConfig() {
        friendRequestRecyclerView.setHasFixedSize(true);
        friendRequestRecyclerView.setLayoutManager(friendRequestLayoutManager);
        friendRequestRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), friendRequestLayoutManager.getOrientation()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //inflate options menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_post).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);

        this.searchItem = menu.findItem(R.id.action_search);
        this.searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchFriend(query);
                }
                else {
                    loadAllFriend();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchFriend(query);
                }
                else {
                    loadAllFriend();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchFriend(String query) {
    }

    private void loadAllFriend() {

    }


}