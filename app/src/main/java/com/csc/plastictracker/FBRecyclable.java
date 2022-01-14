package com.csc.plastictracker;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FBRecyclable {
    private DatabaseReference databaseReference;

    public FBRecyclable() {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://plastic-tracker-bfb8b-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = db.getReference(Recyclable.class.getSimpleName());
    }
    public Task<Void> add(Recyclable rec) {
        return databaseReference.push().setValue(rec);
    }
}
