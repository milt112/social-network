package com.example.socialnetwork.Model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.socialnetwork.Enum.TYPE_REQUEST;

public class FriendRequest {
    // requested user id
    String uId;
    // send request user id
    String mUid;
    // Type Request include : SENDING , ACCEPT , FRIEND
    TYPE_REQUEST typeRequest;

    String createdDate;

    public FriendRequest(){

    }

    public FriendRequest(String uId, String mUid, TYPE_REQUEST typeRequest, String createdDate) {
        this.uId = uId;
        this.mUid = mUid;
        this.typeRequest = typeRequest;
        this.createdDate = createdDate;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getmUid() {
        return mUid;
    }

    public void setmUid(String mUid) {
        this.mUid = mUid;
    }

    public TYPE_REQUEST getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(TYPE_REQUEST typeRequest) {
        this.typeRequest = typeRequest;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public int hashCode() {
        return 31 + this.uId.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof FriendRequest){
            FriendRequest another = (FriendRequest) obj;
            if(this.uId.equals(another.uId)){
                return true;
            }
        }
        return this == obj;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
