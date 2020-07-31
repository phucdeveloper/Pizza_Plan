package com.philipstudio.pizzaplan.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

public class ChiTietMonAnActivity extends AppCompatActivity {

    TextView txtTenMonAn, txtGia, txtNguyenlieu, txtSoluong;
    ImageButton imgButtonThem, imgButtonGiam;
    ImageView imgAnh;
    Button btnGiohang;
    Toolbar toolbar;

    int dem = 0, number = 0, dem1 = 0;
    MonAn monAn;
    NguoiDungUtils nguoiDungUtils;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_monan);
        initView();

        Intent intent = getIntent();
        if (intent != null) {
            monAn = (MonAn) intent.getSerializableExtra("dataMonAn");
            if (monAn != null) {
                txtTenMonAn.setText(monAn.getTenMonAn());
                txtGia.setText(String.valueOf(monAn.getGia()));
                txtNguyenlieu.setText(monAn.getNguyenLieu());
                Glide.with(ChiTietMonAnActivity.this).load(monAn.getAnh()).into(imgAnh);
                txtSoluong.setText(String.valueOf(dem));
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String idNguoiDung = nguoiDungUtils.getIdUser();
        kiemTraTonTaiMonAn(idNguoiDung, monAn);

    }

    private void kiemTraTonTaiMonAn(final String id, final MonAn monAn) {
        dataRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean laTonTai = false;
                GioHang gioHang = null;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    gioHang = dataSnapshot.getValue(GioHang.class);
                    if (gioHang != null) {
                        if (gioHang.getMonAn().getTenMonAn().equals(monAn.getTenMonAn())) {
                            txtSoluong.setText(String.valueOf(gioHang.getSoluong()));
                            laTonTai = true;
                            break;
                        } else {
                            laTonTai = false;
                        }
                    } else {
                        laTonTai = false;
                    }
                }

                final boolean finalLaTonTai = laTonTai;
                imgButtonThem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalLaTonTai) {
                            number = Integer.parseInt(txtSoluong.getText().toString());
                            number++;
                            txtSoluong.setText(String.valueOf(number));
                        }
                        else{
                            dem++;
                            txtSoluong.setText(String.valueOf(dem));
                        }
                    }
                });

                final boolean finalLaTonTai1 = laTonTai;
                imgButtonGiam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalLaTonTai1) {
                            number = Integer.parseInt(txtSoluong.getText().toString());
                            number--;
                            if (number < 0){
                                number = 0;
                            }
                            txtSoluong.setText(String.valueOf(number));
                        } else {
                            dem--;
                            if (dem < 0) {
                                dem = 0;
                            }
                            txtSoluong.setText(String.valueOf(dem));
                        }
                    }
                });

                if (laTonTai) {
                    final GioHang finalGioHang = gioHang;
                    btnGiohang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int soluong = Integer.parseInt(txtSoluong.getText().toString());
                            capNhatItemGioHang(id, finalGioHang.getId(), soluong);
                            dem = 0;
                        }
                    });
                }
                else{
                    btnGiohang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            taoItemTrongGioHang();
                            dem = 0;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //id1 la id nguoi dung, id2 la id gio hang
    private void capNhatItemGioHang(String id1, String id2, int soluong) {
        dataRef.child(id1).child(id2).child("soluong").setValue(soluong);
    }

    private void taoItemTrongGioHang() {
        String idNguoiDung = nguoiDungUtils.getIdUser();
        String id = String.valueOf(System.currentTimeMillis());
        int soluong = Integer.parseInt(txtSoluong.getText().toString());
        GioHang gioHang = new GioHang(id, monAn, soluong);
        dataRef.child(idNguoiDung).child(id).setValue(gioHang);
    }

    private void initView() {
        txtTenMonAn = findViewById(R.id.textview_tenmonan);
        txtGia = findViewById(R.id.textview_gia);
        txtNguyenlieu = findViewById(R.id.textview_nguyenlieu);
        imgAnh = findViewById(R.id.imageview_anh);
        imgButtonThem = findViewById(R.id.imagebutton_them);
        imgButtonGiam = findViewById(R.id.imagebutton_giam);
        txtSoluong = findViewById(R.id.textview_soluong);
        btnGiohang = findViewById(R.id.button_giohang);
        toolbar = findViewById(R.id.toolbar);

        nguoiDungUtils = new NguoiDungUtils(ChiTietMonAnActivity.this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("GioHang");
    }
}