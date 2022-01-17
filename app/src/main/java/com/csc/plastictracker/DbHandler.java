package com.csc.plastictracker;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DbHandler {
    private DatabaseReference databaseReference;
    private boolean exists;
    public DbHandler() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://plastic-tracker-bfb8b-default-rtdb.europe-west1.firebasedatabase.app");
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

    public interface onGetRecyclable {
        void onSuccess(Recyclable rec);
    }

    public interface onExistsRecyclable {
        void onSuccess(boolean exists);
    }

    public Task<Void> addUserRecyclable(String barcodeId, String uid) {
        UserRecyclable userRecyclable = new UserRecyclable(barcodeId, uid);
        return databaseReference.child("user_recyclables").child(uid).push().setValue(userRecyclable);
    }

}
