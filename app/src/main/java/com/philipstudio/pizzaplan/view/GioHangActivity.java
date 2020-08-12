package com.philipstudio.pizzaplan.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.adapter.GioHangAdapter;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class GioHangActivity extends AppCompatActivity {

    ImageButton imgButtonQuaylai;
    RecyclerView recyclerView;
    TextView txtTongtien;
    Button btnDathang;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    NguoiDungUtils utils;
    double giatien, tongtienmonan;
    int soluong;
    ArrayList<GioHang> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();

        setUpRecyclerView();

        String id = utils.getIdUser();
        setUpDataGioHang(id);

        imgButtonQuaylai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnDathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("danhSachGioHang", arrayList);
                bundle.putDouble("tongtien", tongtienmonan);
                Intent intent = new Intent(GioHangActivity.this, MyOrderActivity.class);
                intent.putExtra("data", bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setUpDataGioHang(String idNguoiDung){
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("GioHang");
        dataRef.child(idNguoiDung).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    GioHang gioHang = dataSnapshot.getValue(GioHang.class);
                    arrayList.add(gioHang);
                }

                GioHangAdapter adapter = new GioHangAdapter(arrayList, GioHangActivity.this);
                recyclerView.setAdapter(adapter);

                hienThiTongTien(arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void hienThiTongTien(ArrayList<GioHang> gioHangs){
        double tongtien = 0;
        for (int i=0; i<gioHangs.size(); i++){
            giatien = gioHangs.get(i).getMonAn().getGia();
            soluong = gioHangs.get(i).getSoluong();
            tongtien = tongtien + giatien * soluong;
        }

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedGiatien = formatter.format(tongtien);
        txtTongtien.setText(formattedGiatien + " " + "đồng");
        tongtienmonan = tongtien;
    }

    private void setUpRecyclerView(){
        recyclerView = findViewById(R.id.recyclerview_giohang);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GioHangActivity.this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void initView(){
        imgButtonQuaylai = findViewById(R.id.imgbutton_quaylai);
        recyclerView = findViewById(R.id.recyclerview_giohang);
        txtTongtien = findViewById(R.id.textview_tongtien);
        btnDathang = findViewById(R.id.button_dathang);

        utils = new NguoiDungUtils(GioHangActivity.this);
    }
}