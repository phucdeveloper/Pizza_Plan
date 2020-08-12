package com.philipstudio.pizzaplan.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.ChiTietDonHang;
import com.philipstudio.pizzaplan.model.DonHang;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

import java.util.ArrayList;

public class ThongTinThanhToanFragment extends Fragment {
    EditText edtSothe, edtMabaove, edtNgaycap, edtSotien, edtTaikhoanthuhuong;
    Button btnThanhtoan;
    TextView txtKiemtra, txtKetqua;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef, dataRefChiTiet;
    DonHang donHang;
    ArrayList<GioHang> arrayList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thongtinthanhtoan, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("data");
            if (bundle != null) {
                donHang = (DonHang) bundle.getSerializable("donHang");
                arrayList = bundle.getParcelableArrayList("danhSachGioHang");
            }
        }

        edtSothe = view.findViewById(R.id.edittext_sothe);
        edtMabaove = view.findViewById(R.id.edittext_mabaove);
        edtSotien = view.findViewById(R.id.edittext_nhapsotien);
        btnThanhtoan = view.findViewById(R.id.button_thanhtoan);
        edtTaikhoanthuhuong = view.findViewById(R.id.edittext_sotaikhoanthuhuong);
        txtKiemtra = view.findViewById(R.id.textview_kiemtra);
        txtKetqua = view.findViewById(R.id.textview_ketqua);
        edtNgaycap = view.findViewById(R.id.edittext_ngaycap);
        firebaseDatabase = FirebaseDatabase.getInstance();

        edtSothe.addTextChangedListener(new TextWatcher() {
            private static final int TOTAL_SYMBOLS = 19; // size of pattern 0000-0000-0000-0000
            private static final int TOTAL_DIGITS = 16; // max numbers of digits in pattern: 0000 x 4
            private static final int DIVIDER_MODULO = 5; // means divider position is every 5th symbol beginning with 1
            private static final int DIVIDER_POSITION = DIVIDER_MODULO - 1; // means divider position is every 4th symbol beginning with 0
            private static final char DIVIDER = '-';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isInputCorrect(s, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                    s.replace(0, s.length(), buildCorrectString(getDigitArray(s, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER));
                }
            }

            private boolean isInputCorrect(Editable s, int totalSymbols, int dividerModulo, char divider) {
                boolean isCorrect = s.length() <= totalSymbols; // check size of entered string
                for (int i = 0; i < s.length(); i++) { // check that every element is right
                    if (i > 0 && (i + 1) % dividerModulo == 0) {
                        isCorrect &= divider == s.charAt(i);
                    } else {
                        isCorrect &= Character.isDigit(s.charAt(i));
                    }
                }
                return isCorrect;
            }

            private String buildCorrectString(char[] digits, int dividerPosition, char divider) {
                final StringBuilder formatted = new StringBuilder();

                for (int i = 0; i < digits.length; i++) {
                    if (digits[i] != 0) {
                        formatted.append(digits[i]);
                        if ((i > 0) && (i < (digits.length - 1)) && (((i + 1) % dividerPosition) == 0)) {
                            formatted.append(divider);
                        }
                    }
                }

                return formatted.toString();
            }

            private char[] getDigitArray(final Editable s, final int size) {
                char[] digits = new char[size];
                int index = 0;
                for (int i = 0; i < s.length() && index < size; i++) {
                    char current = s.charAt(i);
                    if (Character.isDigit(current)) {
                        digits[index] = current;
                        index++;
                    }
                }
                return digits;
            }
        });

        btnThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edtSothe.getText().toString()) || TextUtils.isEmpty(edtNgaycap.getText())
                        || TextUtils.isEmpty(edtMabaove.getText().toString()) || TextUtils.isEmpty(edtSotien.getText().toString())
                        || TextUtils.isEmpty(edtTaikhoanthuhuong.getText().toString())) {
                    Toast.makeText(getContext(), "Hãy nhập đầy đủ dữ liệu để thanh toán đơn hàng", Toast.LENGTH_SHORT).show();
                } else {
                    NguoiDungUtils nguoiDungUtils = new NguoiDungUtils(getContext());
                    String id = nguoiDungUtils.getIdUser();
                    dataRef = firebaseDatabase.getReference().child("DonHang");
                    dataRef.child(id).child(donHang.getIdDonHang()).setValue(donHang)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    final String idNguoiDung = donHang.getIdNguoiDung();
                                    final String idDonHang = donHang.getIdDonHang();
                                    final String thoigian = donHang.getThoigian();
                                    ChiTietDonHang chiTietDonHang = new ChiTietDonHang(idNguoiDung, idDonHang, arrayList, thoigian);
                                    dataRefChiTiet = firebaseDatabase.getReference().child("ChiTietDonHang");
                                    dataRefChiTiet.child(idNguoiDung).child(idDonHang).setValue(chiTietDonHang);
                                    Toast.makeText(getContext(), "Thanh toán dơn hàng thành công", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), "Không thể thanh toán cho đơn hàng này", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        return view;
    }
}
