package com.csc.plastictracker;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbHandler {
    private Recyclable[] recs;
    private UserRecyclable[] uRecs;
    private DatabaseReference databaseReference;
    private boolean exists;
    public DbHandler() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://plastic-tracker-bfb8b-default-rtdb.europe-west1.firebasedatabase.app");
        db.setPersistenceEnabled(true);
        databaseReference = db.getReference();
    }

    public Task<Void> addRecyclable(Recyclable rec) {
        return databaseReference.child("recyclables").child(rec.getBarcodeId()).setValue(rec);
    }

    public void getRecyclable(String barcodeId, final onGetRecyclable listener) {
        databaseReference.child("recyclables").orderByChild("barcodeId").equalTo(barcodeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Recyclable rec = new Recyclable();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                   rec = ds.getValue(Recyclable.class);
                }
                listener.onSuccess(rec);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void existsRecyclable(String barcodeId, final onExistsRecyclable listener) {
        databaseReference.child("recyclables").orderByChild("barcodeId").equalTo(barcodeId).addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                exists = dataSnapshot.exists();
                listener.onSuccess(exists);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void getAllRecyclable(final onGetRecyclables listener) {
        List<Recyclable> recs = new ArrayList<>();
        databaseReference.child("recyclables").orderByChild("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    Recyclable rec = val.getValue(Recyclable.class);
                    recs.add(rec);
                }
                listener.onSuccess(recs.toArray(new Recyclable[recs.size()]));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllUserRecyclable(String uid, final onGetUserRecyclables listener) {
        List<UserRecyclable> uRecs = new ArrayList<>();
        databaseReference.child("user_recyclables").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot val : dataSnapshot.getChildren()){
                    UserRecyclable uRec = val.getValue(UserRecyclable.class);
                    uRecs.add(uRec);
                }
                listener.onSuccess(uRecs.toArray(new UserRecyclable[uRecs.size()]));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public interface onGetRecyclable {
        void onSuccess(Recyclable rec);
    }

    public interface onExistsRecyclable {
        void onSuccess(boolean exists);
    }
    public interface onGetRecyclables {
        void onSuccess(Recyclable[] recs);
    }

    public interface onGetUserRecyclables {
        void onSuccess(UserRecyclable[] uRecs);
    }

    public Task<Void> addUserRecyclable(String barcodeId, String uid) {
        UserRecyclable userRecyclable = new UserRecyclable(barcodeId, uid);
        return databaseReference.child("user_recyclables").child(uid).push().setValue(userRecyclable);
    }

}
