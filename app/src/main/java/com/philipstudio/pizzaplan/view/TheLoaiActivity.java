package com.philipstudio.pizzaplan.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.adapter.XuHuongAdapter;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.model.TheLoaiMonAn;

import java.util.ArrayList;

public class TheLoaiActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;

    TheLoaiMonAn theLoaiMonAn;
    ArrayList<MonAn> anArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_loai);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recyclerview_danhsachmonantheotheloai);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null){
           theLoaiMonAn = (TheLoaiMonAn) intent.getSerializableExtra("theLoaiMonAn");
           if (theLoaiMonAn != null){
               toolbar.setTitle(theLoaiMonAn.getTenTheLoai());
               anArrayList = theLoaiMonAn.getMonAnArrayList();
           }
        }

        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(TheLoaiActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        XuHuongAdapter xuHuongAdapter = new XuHuongAdapter(anArrayList, TheLoaiActivity.this);
        recyclerView.setAdapter(xuHuongAdapter);
    }
}