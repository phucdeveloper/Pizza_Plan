package com.philipstudio.pizzaplan.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.adapter.GioHangAdapter;
import com.philipstudio.pizzaplan.adapter.LichSuThanhToanAdapter;
import com.philipstudio.pizzaplan.adapter.MonAnAdapter;
import com.philipstudio.pizzaplan.model.ChiTietDonHang;
import com.philipstudio.pizzaplan.model.DonHang;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class LichSuThanhToanFragment extends Fragment {

    RecyclerView recyclerView;
    NguoiDungUtils nguoiDungUtils;
    String id;
    LichSuThanhToanAdapter adapter;
    double giatien;
    int soluong;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_thanh_toan, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_danhsach_thanhtoan);

        nguoiDungUtils = new NguoiDungUtils(getContext());
        id = nguoiDungUtils.getIdUser();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("DonHang");
        dataRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DonHang> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    arrayList.add(donHang);
                }

                adapter = new LichSuThanhToanAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickLichSuThanhToanListener(new LichSuThanhToanAdapter.OnItemClickLichSuThanhToanListener() {
                    @Override
                    public void OnItemClick(Context context, int position) {
                        showDialog(getContext(), position);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void showDialog(Context context, final int position){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_dialog);

        dialog.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;

        final TextView txtOrder, txtTrangthaihoadon, txtThoigian, txtTongtien;
        final RecyclerView recyclerViewMonAn;

        txtOrder = dialog.findViewById(R.id.textview_order);
        txtThoigian = dialog.findViewById(R.id.textview_thoigian);
        txtTongtien = dialog.findViewById(R.id.textview_tongtien);
        txtTrangthaihoadon = dialog.findViewById(R.id.textview_trangthai);
        recyclerViewMonAn = dialog.findViewById(R.id.recyclerview_danhsach_monan);
        if (recyclerViewMonAn != null){
            recyclerViewMonAn.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            recyclerViewMonAn.setLayoutManager(layoutManager);

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference dataRef = firebaseDatabase.getReference().child("ChiTietDonHang");

            dataRef.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ChiTietDonHang> chiTietDonHangArrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        ChiTietDonHang chiTietDonHang = dataSnapshot.getValue(ChiTietDonHang.class);
                        chiTietDonHangArrayList.add(chiTietDonHang);
                    }

                    MonAnAdapter adapter = new MonAnAdapter(chiTietDonHangArrayList.get(position).getListDanhSachGioHang(), getContext());
                    recyclerViewMonAn.setAdapter(adapter);

                    txtOrder.setText(chiTietDonHangArrayList.get(position).getIdDonHang());
                    txtThoigian.setText(chiTietDonHangArrayList.get(position).getThoigiandathang());
                    txtTrangthaihoadon.setText("Đã giao hàng");

                    NumberFormat formatter = new DecimalFormat("#,###");
                    double tongtien = hienThiTongTien(chiTietDonHangArrayList.get(position).getListDanhSachGioHang());
                    String formattedGiatien = formatter.format(tongtien);
                    txtTongtien.setText(formattedGiatien + " đồng");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        dialog.show();
    }

    private double hienThiTongTien(ArrayList<GioHang> gioHangs){
        double tongtien = 0;
        for (int i=0; i<gioHangs.size(); i++){
            giatien = gioHangs.get(i).getMonAn().getGia();
            soluong = gioHangs.get(i).getSoluong();
            tongtien = tongtien + giatien * soluong;
        }
        return tongtien;
    }
}
