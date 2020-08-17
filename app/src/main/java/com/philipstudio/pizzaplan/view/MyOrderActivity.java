package com.philipstudio.pizzaplan.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.DonHang;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.model.NguoiDung;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;
import com.vnpay.qr.VnpayQRReturnEntity;
import com.vnpay.qr.activity.QRActivity;
import com.vnpay.qr.utils.Constants;
import com.vnpay.qr.utils.VNPAYTags;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyOrderActivity extends AppCompatActivity {

    TextView txtTenNguoiDung, txtNgayThang, txtGiophut, txtIDOrder, txtTongtien;
    EditText edtEmailOrPhonenumber, edtThoigiangiaohang, edtDiachi;
    ImageButton imgButtonGooglemap;
    Button btnTieptuc, btnMenuorder;
    Toolbar toolbar;
    Spinner spinnerPhuongThucThanhToan;

    NguoiDungUtils nguoiDungUtils;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    double tongtienhoadon;
    String diachi;
    static final int REQUEST_CODE_GET_LOACTION_SUCCESS = 1;
    boolean isTheATM = false, isQRCode = false, isViDienTu = false;
    String[] arrayPhuongThucThanhToan = {"Chọn phương thức thanh toán", "Thẻ ATM", "QR code", "Ví điện tử"};
    ArrayList<GioHang> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initView();

        final Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("data");
            if (bundle != null){
                tongtienhoadon = bundle.getDouble("tongtien", 100);
                thietLapThongTinHoaDon(tongtienhoadon);
                arrayList = bundle.getParcelableArrayList("danhSachGioHang");
            }
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

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayPhuongThucThanhToan);
        spinnerPhuongThucThanhToan.setAdapter(arrayAdapter);

        spinnerPhuongThucThanhToan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        isTheATM = true;
                        isQRCode = false;
                        isViDienTu = false;
                        break;
                    case 2:
                        isQRCode = true;
                        isViDienTu = false;
                        isTheATM = false;
                        break;
                    case 3:
                        isViDienTu = true;
                        isQRCode = false;
                        isTheATM = false;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(MyOrderActivity.this, "Bạn chưa chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String idOrder = txtIDOrder.getText().toString();
            String idNguoiDung = nguoiDungUtils.getIdUser();
            String thoigian = txtGiophut.getText().toString() + " " + txtNgayThang.getText().toString();
            String diadiem = edtDiachi.getText().toString();
//            String text = txtTongtien.getText().toString();
//            String[] data = text.split(" ");
//            double tongtien = Double.parseDouble(tongtienhoadon);
            DonHang donHang = new DonHang(idOrder, idNguoiDung, thoigian, diadiem, tongtienhoadon, "Đang đặt hàng");
            switch (v.getId()) {
                case R.id.button_thanhtoan:
                    if (isTheATM) {
                        if (!TextUtils.isEmpty(diadiem)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("donHang", donHang);
                            bundle.putParcelableArrayList("danhSachGioHang", arrayList);
                            Intent intent = new Intent(MyOrderActivity.this, ThanhToanActivity.class);
                            intent.putExtra("data", bundle);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MyOrderActivity.this, "Bạn chưa chọn địa chỉ", Toast.LENGTH_SHORT).show();
                        }

                    } else if (isQRCode) {
                        if (!TextUtils.isEmpty(diadiem)) {
                            QRActivity.setupQR(MyOrderActivity.this, VNPAYTags.CURRENCY_TYPE1,
                                    "http://mobile.vnpay.vn/IVB/Merchant.html", VNPAYTags.LANG_VN, R.style.MyCustomQRTheme);
                        } else {
                            Toast.makeText(MyOrderActivity.this, "Bạn chưa chọn địa chỉ", Toast.LENGTH_SHORT).show();
                        }

                    } else if (isViDienTu) {
                        if (!TextUtils.isEmpty(diadiem)) {
                            Toast.makeText(MyOrderActivity.this, "Ban đã chọn thanh toán bằng ví điện tử", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MyOrderActivity.this, "Bạn chưa chọn địa chỉ", Toast.LENGTH_SHORT).show();
                        }
                    }
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

    private void showGoogleMapDeLayDiaChiGiaoHang() {
        Intent intent = new Intent(MyOrderActivity.this, MapsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_GET_LOACTION_SUCCESS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_GET_LOACTION_SUCCESS && data != null) {
                diachi = data.getStringExtra("diachi");
                edtDiachi.setText(diachi);
            } else if (requestCode == VNPAYTags.REQUEST_VNPAY_QR && data != null) {
                String dataV = data.getStringExtra(VNPAYTags.QR_RESPONSE);
                VnpayQRReturnEntity returnEntity = Constants.g().getGsonInstance().fromJson(dataV, VnpayQRReturnEntity.class); // Sử dụng Gson parse lại Entity từ Dữ liệu SDK
//                Intent intent = new Intent(this, ThanhToanActivity.class);
//                startActivity(intent);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void thietLapThongTinHoaDon(double number) {
        String idOrder = String.valueOf(System.currentTimeMillis());
        txtIDOrder.setText(idOrder);

        String ngaythang = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        txtNgayThang.setText(ngaythang);

        String giophut = new SimpleDateFormat("HH:mm").format(new Date());
        txtGiophut.setText(giophut);

        NumberFormat formatter = new DecimalFormat("#,###");
        String formattedGiatien = formatter.format(number);
        txtTongtien.setText(formattedGiatien + " " + "đồng");
    }

    private void hienThiThongTinNguoiDung(final TextView textView, final EditText editText) {
        String idNguoiDung = nguoiDungUtils.getIdUser();
        dataRef.child(idNguoiDung).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                if (nguoiDung != null) {
                    textView.setText(nguoiDung.getTennguoidung());
                    editText.setText(nguoiDung.getEmailorPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        txtTenNguoiDung = findViewById(R.id.textview_tennguoidung);
        txtNgayThang = findViewById(R.id.textview_ngaythang);
        txtGiophut = findViewById(R.id.textview_giophut);
        txtTongtien = findViewById(R.id.textview_tongtien);
        txtIDOrder = findViewById(R.id.textview_iddathang);
        toolbar = findViewById(R.id.toolbar);

        edtDiachi = findViewById(R.id.edittext_diachi);
        edtThoigiangiaohang = findViewById(R.id.edittext_thoigiangiaohang);
        edtEmailOrPhonenumber = findViewById(R.id.edittext_diachilienhe);
        spinnerPhuongThucThanhToan = findViewById(R.id.spinner_phuongthucthanhtoan);

        btnTieptuc = findViewById(R.id.button_thanhtoan);
        btnMenuorder = findViewById(R.id.button_menuorder);

        imgButtonGooglemap = findViewById(R.id.imagebutton_googlemap);

        nguoiDungUtils = new NguoiDungUtils(MyOrderActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("NguoiDung");
    }
}