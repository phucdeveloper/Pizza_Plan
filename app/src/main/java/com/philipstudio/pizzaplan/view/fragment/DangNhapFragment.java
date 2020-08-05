package com.philipstudio.pizzaplan.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

public class DangNhapFragment extends Fragment {

    EditText edtEmailOrPhonenumber, edtMatkhau;
    Button btnDangnhap;

    FirebaseAuth firebaseAuth;
    OnSignInClickListener onSignInClickListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_nhap, container, false);
        initView(view);

        btnDangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailOrPhonenumber = edtEmailOrPhonenumber.getText().toString();
                String matkhau = edtMatkhau.getText().toString();

                if (TextUtils.isEmpty(emailOrPhonenumber) || TextUtils.isEmpty(matkhau)){
                    Toast.makeText(getContext(), "Bạn cần nhập đầy đủ thông tin để đăng nhập", Toast.LENGTH_SHORT).show();
                }
                else{
                    dangNhapTaiKhoan(emailOrPhonenumber, matkhau);
                }
            }
        });
        return view;
    }

    private void dangNhapTaiKhoan(String str1, String str2){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(str1, str2).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (authResult != null){
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null){
                        String idNguoiDung = firebaseUser.getUid();
                        if (getContext() != null){
                            NguoiDungUtils nguoiDungUtils = new NguoiDungUtils(getContext());
                            nguoiDungUtils.setIdUser(idNguoiDung);

                            if (onSignInClickListener != null){
                                onSignInClickListener.onSuccess(true);
                            }
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Đăng nhập không thành công " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        onSignInClickListener = (OnSignInClickListener) context;
    }

    private void initView(View view){
        edtEmailOrPhonenumber = view.findViewById(R.id.edittext_sodienthoai_or_email);
        edtMatkhau = view.findViewById(R.id.edittext_matkhau);
        btnDangnhap = view.findViewById(R.id.button_dangnhap);
    }

    public interface OnSignInClickListener{
        void onSuccess(boolean isSuccess);
    }
}
