package com.philipstudio.pizzaplan.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.adapter.LichSuThanhToanAdapter;
import com.philipstudio.pizzaplan.adapter.MonAnAdapter;
import com.philipstudio.pizzaplan.model.DonHang;
import com.philipstudio.pizzaplan.model.MonAn;

import java.util.ArrayList;

public class LichSuThanhToanFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<DonHang> arrayList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_thanh_toan, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_danhsach_thanhtoan);
        recyclerView.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("DonHang");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        LichSuThanhToanAdapter adapter = new LichSuThanhToanAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickLichSuThanhToanListener(new LichSuThanhToanAdapter.OnItemClickLichSuThanhToanListener() {
            @Override
            public void OnItemClick(Context context, int position) {
                showDialog(context, position);
            }
        });
        return view;
    }

    private void showDialog(Context context, int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(R.layout.layout_alertdialog);

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        TextView txtOrder, txtTrangthaihoadon, txtThoigian, txtTongtien;
        RecyclerView recyclerView;

        txtOrder = alertDialog.findViewById(R.id.textview_order);
        txtThoigian = alertDialog.findViewById(R.id.textview_thoigian);
        txtTongtien = alertDialog.findViewById(R.id.textview_tongtien);
        txtTrangthaihoadon = alertDialog.findViewById(R.id.textview_trangthai);
        recyclerView = alertDialog.findViewById(R.id.recyclerview_danhsach_monan);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference dataRef = firebaseDatabase.getReference().child("ChiTietDonHang");

        ArrayList<MonAn> monAnArrayList = new ArrayList<>();


        MonAnAdapter anAdapter = new MonAnAdapter(monAnArrayList, context);
        recyclerView.setAdapter(anAdapter);

        txtOrder.setText(arrayList.get(position).getIdDonHang());
        txtThoigian.setText(arrayList.get(position).getThoigian());
        txtTongtien.setText(String.valueOf(arrayList.get(position).getTongtien()));
        txtTrangthaihoadon.setText("Đã giao hàng");

        alertDialog.show();
    }
}
