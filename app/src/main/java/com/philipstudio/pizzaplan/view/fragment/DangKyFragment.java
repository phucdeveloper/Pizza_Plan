package com.philipstudio.pizzaplan.view.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.NguoiDung;

public class DangKyFragment extends Fragment {
    EditText edtEmailOrPhonenumber, edtMatkhau, edtHo, edtTen;
    Button btnDangky;
    Spinner spinnerNgay, spinnerThang, spinnerNam;
    RadioButton rBNam, rBNu;

    String gioitinh, ngay, thang, nam;
    String[] ngayStrings = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
    "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    String[] thangStrings = {"tháng 1", "tháng 2", "tháng 3", "tháng 4", "tháng 5", "tháng 6", "tháng 7", "tháng 8",
    "tháng 9", "tháng 10", "tháng 11", "tháng 12"};

    String[] namStrings = {"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999", "2000",
    "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013",
    "2014", "2015", "2016", "2017", "2018", "2019", "2020"};

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_ky, container, false);

        initView(view);
        if (getContext() != null){
            ArrayAdapter<String> ngayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, ngayStrings);
            spinnerNgay.setAdapter(ngayAdapter);

            ArrayAdapter<String> thangAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, thangStrings);
            spinnerThang.setAdapter(thangAdapter);

            ArrayAdapter<String> namAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, namStrings);
            spinnerNam.setAdapter(namAdapter);
        }

        spinnerNgay.setOnItemSelectedListener(onItemSelectedListener);
        spinnerThang.setOnItemSelectedListener(onItemSelectedListener);
        spinnerNam.setOnItemSelectedListener(onItemSelectedListener);

        rBNam.setOnClickListener(listener);
        rBNu.setOnClickListener(listener);

        btnDangky.setOnClickListener(listener);

        return view;
    }

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (view.getId()){
                case R.id.spinner_ngay:
                    ngay = ngayStrings[position];
                    Toast.makeText(getContext(), ngay, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.spinner_thang:
                    String data = thangStrings[position];
                    String[] str = data.split(" ");
                    thang = str[1];
                    Toast.makeText(getContext(), thang, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.spinner_nam:
                    nam = namStrings[position];
                    Toast.makeText(getContext(), nam, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_dangky:
                    Toast.makeText(getContext(), ngay + ", " + thang + ", " + nam, Toast.LENGTH_SHORT).show();
             //       dangkyTaiKhoan(edtEmailOrPhonenumber.getText().toString(), edtMatkhau.getText().toString(), edtHo.getText().toString() + " " + edtTen.getText().toString(), ngay + "/" + thang + "/" + nam, gioitinh);
                    break;
                case R.id.radio_button_nam:
                    gioitinh = rBNam.getText().toString();
                    break;
                case R.id.radio_button_nu:
                    gioitinh = rBNu.getText().toString();
                    break;
            }
        }
    };

    private void dangkyTaiKhoan(final String emailOrPhonenumber, final String matkhau, final String hoten, final String ngaysinh, final String gioitinh) {
        if (TextUtils.isEmpty(emailOrPhonenumber) || TextUtils.isEmpty(matkhau) || TextUtils.isEmpty(hoten) || TextUtils.isEmpty(ngaysinh) || TextUtils.isEmpty(gioitinh)) {
            Toast.makeText(getContext(), "Mời bạn nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(emailOrPhonenumber, matkhau).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (authResult != null) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser != null) {
                            String idNguoiDung = firebaseUser.getUid();
                            NguoiDung nguoiDung = new NguoiDung(idNguoiDung, emailOrPhonenumber, " ", matkhau, hoten, " ", ngaysinh, gioitinh);
                            themNguoiDung(nguoiDung.getIdNguoiDung(), nguoiDung);
                            Toast.makeText(getContext(), "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Đăng ký tài khoản thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "Địa chỉ email hoặc số diện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void themNguoiDung(String id, NguoiDung nguoiDung) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("NguoiDung");
        dataRef.child(id).setValue(nguoiDung);
    }

    private void initView(View view) {
        edtEmailOrPhonenumber = view.findViewById(R.id.edittext_sodienthoai_or_email);
        edtMatkhau = view.findViewById(R.id.edittext_matkhau);
        edtHo = view.findViewById(R.id.edittext_ho);
        edtTen = view.findViewById(R.id.edittext_ten);
        spinnerNgay = view.findViewById(R.id.spinner_ngay);
        spinnerThang = view.findViewById(R.id.spinner_thang);
        spinnerNam = view.findViewById(R.id.spinner_nam);
        rBNam = view.findViewById(R.id.radio_button_nam);
        rBNu = view.findViewById(R.id.radio_button_nu);
        btnDangky = view.findViewById(R.id.button_dangky);
    }
}
