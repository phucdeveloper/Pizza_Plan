package com.philipstudio.pizzaplan.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.NguoiDung;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyOrderActivity extends AppCompatActivity {

    TextView txtTenNguoiDung, txtNgayThang, txtGiophut, txtIDOrder, txtTongtien;
    EditText edtEmailOrPhonenumber, edtThoigiangiaohang, edtDiachi;
    ImageButton imgButtonGooglemap;
    Button btnTieptuc, btnMenuorder;
    Toolbar toolbar;

    NguoiDungUtils nguoiDungUtils;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    double tongtienhoadon;
    String diachi;
    static final int REQUEST_CODE_GET_LOACTION_SUCCESS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initView();

        final Intent intent = getIntent();
        if (intent != null){
            tongtienhoadon = intent.getDoubleExtra("tongtien", 100);
            thietLapThongTinHoaDon(tongtienhoadon);
        }

        hienThiThongTinNguoiDung(txtTenNguoiDung, edtEmailOrPhonenumber);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnMenuorder.setOnClickListener(listener);
        btnTieptuc.setOnClickListener(listener);
        imgButtonGooglemap.setOnClickListener(listener);


    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button_tieptuc:
                    Intent intent1 = new Intent(MyOrderActivity.this, ThanhToanActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.button_menuorder:
                    Intent intent2 = new Intent(MyOrderActivity.this, HomeActivity.class);
                    startActivity(intent2);
                    finish();
                    break;

                case R.id.imagebutton_googlemap:
                    showGoogleMapDeLayDiaChiGiaoHang();
                    break;
            }
        }
    };

    private void showGoogleMapDeLayDiaChiGiaoHang(){
        Intent intent = new Intent(MyOrderActivity.this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_GET_LOACTION_SUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_GET_LOACTION_SUCCESS && data != null){
            diachi = data.getStringExtra("diachi");
            edtDiachi.setText(diachi);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void thietLapThongTinHoaDon(double number){
        String idOrder = String.valueOf(System.currentTimeMillis());
        txtIDOrder.setText(idOrder);

        String ngaythang = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        txtNgayThang.setText(ngaythang);

        String giophut = new SimpleDateFormat("HH:mm").format(new Date());
        txtGiophut.setText(giophut);

        txtTongtien.setText(String.valueOf(number));
    }

    private void hienThiThongTinNguoiDung(final TextView textView, final EditText editText){
        String idNguoiDung = nguoiDungUtils.getIdUser();
        dataRef.child(idNguoiDung).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                if (nguoiDung != null){
                    textView.setText(nguoiDung.getTennguoidung());
                    editText.setText(nguoiDung.getEmailorPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView(){
        txtTenNguoiDung = findViewById(R.id.textview_tennguoidung);
        txtNgayThang = findViewById(R.id.textview_ngaythang);
        txtGiophut = findViewById(R.id.textview_giophut);
        txtTongtien = findViewById(R.id.textview_tongtien);
        txtIDOrder = findViewById(R.id.textview_iddathang);
        toolbar = findViewById(R.id.toolbar);

        edtDiachi = findViewById(R.id.edittext_diachi);
        edtThoigiangiaohang = findViewById(R.id.edittext_thoigiangiaohang);
        edtEmailOrPhonenumber = findViewById(R.id.edittext_diachilienhe);

        btnTieptuc = findViewById(R.id.button_tieptuc);
        btnMenuorder = findViewById(R.id.button_menuorder);

        imgButtonGooglemap = findViewById(R.id.imagebutton_googlemap);

        nguoiDungUtils = new NguoiDungUtils(MyOrderActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("NguoiDung");
    }
}