package com.example.socialnetwork.Fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialnetwork.Adapter.FriendAdapter;
import com.example.socialnetwork.Adapter.UserAdapter;
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

class FriendListFragment extends Fragment {
    //Recycler view for friend list
    RecyclerView friendListRecyclerView;
    FriendAdapter FriendAdapter;
    ArrayList<FriendRequest> friendRequestList; // FRIEND
    //Recycler view for friend Request
    RecyclerView friendRequestRecyclerView;
    FriendAdapter FriendRequestAdapter;
    ArrayList<FriendRequest> newFriendRequestList;
    //Check current user
    FirebaseAuth mAuth;
    //Firebase Database Reference
    DatabaseReference userRef;
    DatabaseReference requestRef;
    DatabaseReference currentUserRef;
    DatabaseReference currentUserRequestRef;
    DatabaseReference requestedToUserRef;
    DatabaseReference requestedUserRequestRef;
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
        this.initView();
        this.initDataBaseReference();
        this.initFireBaseAuth();
        //Get current User
        this.getCurrentUserStatus();
        this.getCurrentUser();
        //Setup
        this.setUpViewConfig();

        return view;
    }

    private void initField() {

    }

    private void initView() {

    }

    private void initDataBaseReference() {

    }

    private void initFireBaseAuth() {

    }

    private void getCurrentUserStatus() {

    }

    private void getCurrentUser() {

    }

    private void setUpViewConfig() {

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
                    getAllFriend();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!TextUtils.isEmpty(query.trim())) {
                    searchFriend(query);
                }
                else {
                    getAllFriend();
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchFriend(String query) {
    }

    private void getAllFriend() {

    }


}