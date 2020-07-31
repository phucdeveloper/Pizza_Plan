package com.philipstudio.pizzaplan.utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.model.GioHang;

import java.util.ArrayList;

public class GioHangUtils {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    ArrayList<GioHang> arrayList = new ArrayList<>();

    private ArrayList<GioHang> getDanhSachGioHang(String id){
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("GioHang");
        dataRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GioHang gioHang = dataSnapshot.getValue(GioHang.class);
                    arrayList.add(gioHang);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return arrayList;
    }
}
