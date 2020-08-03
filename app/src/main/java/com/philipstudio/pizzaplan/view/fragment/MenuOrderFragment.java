package com.philipstudio.pizzaplan.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.adapter.MenuOrderAdapter;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.model.TheLoaiMonAn;
import com.philipstudio.pizzaplan.utils.Constant;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;
import com.philipstudio.pizzaplan.view.GioHangActivity;

import java.util.ArrayList;

import ru.nikartm.support.ImageBadgeView;

public class MenuOrderFragment extends Fragment {

    RecyclerView rViewDanhSachMonAn;
    ImageBadgeView imageBadgeViewGioHang;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dateRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_order, container, false);
        rViewDanhSachMonAn = view.findViewById(R.id.recyclerview_danhsach_monan);
        imageBadgeViewGioHang = view.findViewById(R.id.imagebadgeview_giohang);

        setUpRecyclerView(rViewDanhSachMonAn);

        setUpDanhSachMonAn(rViewDanhSachMonAn);

        NguoiDungUtils nguoiDungUtils = new NguoiDungUtils(getContext());
        String idNguoiDung = nguoiDungUtils.getIdUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dateRef = firebaseDatabase.getReference().child("GioHang");
        dateRef.child(idNguoiDung).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<GioHang> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GioHang gioHang = dataSnapshot.getValue(GioHang.class);
                    arrayList.add(gioHang);
                }

                    int soluong = arrayList.size();
                    imageBadgeViewGioHang.setBadgeValue(soluong)
                            .setBadgeBackground(getResources().getDrawable(R.drawable.custom_background_badgeview));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        imageBadgeViewGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpRecyclerView(rViewDanhSachMonAn);
        setUpDanhSachMonAn(rViewDanhSachMonAn);

//        NguoiDungUtils nguoiDungUtils = new NguoiDungUtils(getContext());
//        String idNguoiDung = nguoiDungUtils.getIdUser();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//        dateRef = firebaseDatabase.getReference().child("GioHang");
//        dateRef.child(idNguoiDung).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<GioHang> arrayList = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    GioHang gioHang = dataSnapshot.getValue(GioHang.class);
//                    arrayList.add(gioHang);
//                }
//
//                    int soluong = arrayList.size();
//                    imageBadgeViewGioHang.setBadgeValue(soluong)
//                            .setBadgeBackground(getResources().getDrawable(R.drawable.custom_background_badgeview));
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    private void setUpRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setUpDanhSachMonAn(final RecyclerView recyclerView) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dateRef = firebaseDatabase.getReference().child("MonAn");
        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<MonAn> monAnArrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MonAn monAn = dataSnapshot.getValue(MonAn.class);
                    monAnArrayList.add(monAn);
                }

                ArrayList<TheLoaiMonAn> arrayList = new ArrayList<>();
                arrayList.add(new TheLoaiMonAn("Các món Pizza phổ biến", monAnArrayList, Constant.ITEM_POPULAR));
                arrayList.add(new TheLoaiMonAn("Xu hướng", monAnArrayList, Constant.ITEM_TRENDING));
                MenuOrderAdapter adapter = new MenuOrderAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
