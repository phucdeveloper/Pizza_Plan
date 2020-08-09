package com.philipstudio.pizzaplan.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.adapter.KetQuaAdapter;
import com.philipstudio.pizzaplan.adapter.LichSuTimKiemAdapter;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.model.NguoiDung;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

import java.util.ArrayList;

public class TimKiemFragment extends Fragment {

    EditText edtNhapTukhoa;
    RecyclerView rVDanhsachketqua, rVDanhSachTuKhoa;
    TextView txtXoaLichSu;
    RelativeLayout relativeLayout;
    AvatarImageView avatarImageView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef, dataNguoiDungRef, dataLichSuTimKiemRef;

    NguoiDungUtils nguoiDungUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        initView(view);

        final String id = nguoiDungUtils.getIdUser();
        dataNguoiDungRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                if (nguoiDung != null){
                    String anhDaiDien = nguoiDung.getAnhdaidien();
                    if (anhDaiDien != null){
                        avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
                        Glide.with(getContext()).load(anhDaiDien).into(avatarImageView);
                    }
                    else{
                        avatarImageView.setState(AvatarImageView.SHOW_INITIAL);
                        avatarImageView.setText(nguoiDung.getTennguoidung());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        edtNhapTukhoa.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))||(actionId == EditorInfo.IME_ACTION_DONE)) {
                    final String tukhoa = edtNhapTukhoa.getText().toString();
                    if (TextUtils.isEmpty(tukhoa)) {
                        Toast.makeText(getContext(), "Bạn cần phải nhập từ khoá để tìm kiếm", Toast.LENGTH_SHORT).show();
                    } else {
                        themTuKhoaVaoLichSuTimKiem(id, tukhoa);
                        timKiemVaHienThiKetQua(tukhoa);
                    }
                }
                return true;
            }
        });

        txtXoaLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(getContext());
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String id = nguoiDungUtils.getIdUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataLichSuTimKiemRef = firebaseDatabase.getReference().child("LichSuTimKiem");
        dataLichSuTimKiemRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String tukhoa = dataSnapshot.getValue(String.class);
                    arrayList.add(tukhoa);
                }

                if (arrayList.size() != 0){
                    relativeLayout.setVisibility(View.VISIBLE);
                }

                rVDanhSachTuKhoa.setHasFixedSize(true);

                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
                rVDanhSachTuKhoa.setLayoutManager(layoutManager);

                LichSuTimKiemAdapter lichSuTimKiemAdapter = new LichSuTimKiemAdapter(arrayList, getContext());
                rVDanhSachTuKhoa.setAdapter(lichSuTimKiemAdapter);

                lichSuTimKiemAdapter.setOnItemChipCliclListener(new LichSuTimKiemAdapter.OnItemChipCliclListener() {
                    @Override
                    public void onItem(String text) {
                        timKiemVaHienThiKetQua(text);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void themTuKhoaVaoLichSuTimKiem(String id, String tukhoa){
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataLichSuTimKiemRef = firebaseDatabase.getReference().child("LichSuTimKiem");
        dataLichSuTimKiemRef.child(id).push().setValue(tukhoa);
    }

    private void timKiemVaHienThiKetQua(final String tukhoa) {
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<MonAn> arrayList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    MonAn monAn = dataSnapshot.getValue(MonAn.class);
                    if (monAn != null){
                        String tenMonAn = monAn.getTenMonAn();
                        if (tenMonAn.contains(tukhoa)){
                            arrayList.add(monAn);

                        }
                    }
                }

                setUpRecyclerView(rVDanhsachketqua, arrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setUpRecyclerView(RecyclerView recyclerView, ArrayList<MonAn> monAns) {
        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        KetQuaAdapter ketQuaAdapter = new KetQuaAdapter(monAns, getContext());
        rVDanhsachketqua.setAdapter(ketQuaAdapter);
    }

    private void showAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo")
                .setMessage("Bạn có muốn xoá tất cả lịch sử tìm kiếm của mình không ?");
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setPositiveButton("Có, đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataLichSuTimKiemRef = firebaseDatabase.getReference().child("LichSuTimKiem");
                dataLichSuTimKiemRef.removeValue();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void initView(View view) {
        rVDanhsachketqua = view.findViewById(R.id.recyclerview_danhsach_ketqua);
        edtNhapTukhoa = view.findViewById(R.id.edittext_nhaptukhoa);
        txtXoaLichSu = view.findViewById(R.id.textview_xoalichsu);
        relativeLayout = view.findViewById(R.id.layout);
        avatarImageView = view.findViewById(R.id.avatarimageview_anhdaidien);
        rVDanhSachTuKhoa = view.findViewById(R.id.recyclerview_danhsach_tukhoatimkiem);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("MonAn");
        dataNguoiDungRef = firebaseDatabase.getReference().child("NguoiDung");
        nguoiDungUtils = new NguoiDungUtils(getContext());

        relativeLayout.setVisibility(View.GONE);
    }
}
