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
    private static FirebaseDatabase firebaseDatabase;
    private boolean exists;
    public DbHandler() {
        if(firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance("https://plastic-tracker-bfb8b-default-rtdb.europe-west1.firebasedatabase.app");
            firebaseDatabase.setPersistenceEnabled(true);
        }
        databaseReference = firebaseDatabase.getReference();
    }

    //adds a new recyclable item to the database
    public Task<Void> addRecyclable(Recyclable rec) {
        return databaseReference.child("recyclables").child(rec.getBarcodeId()).setValue(rec);
    }

    //returns a recyclable item when given a valid barcode
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

    //checks if a given barcode matches a recyclable item in the database
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

    //returns all items a given user has recycled
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

    //interfaces for asynchronous database returns
    public interface onGetRecyclable {
        void onSuccess(Recyclable rec);
    }
    public interface onExistsRecyclable {
        void onSuccess(boolean exists);
    }
    public interface onGetUserRecyclables {
        void onSuccess(UserRecyclable[] uRecs);
    }
    public Task<Void> addUserRecyclable(String barcodeId, String uid) {
        UserRecyclable userRecyclable = new UserRecyclable(barcodeId, uid);
        return databaseReference.child("user_recyclables").child(uid).push().setValue(userRecyclable);
    }
}
