package com.philipstudio.pizzaplan.view.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.github.abdularis.civ.AvatarImageView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.NguoiDung;
import com.philipstudio.pizzaplan.utils.Constant;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;
import com.philipstudio.pizzaplan.view.PizzaPlantActivity;

public class TrangCaNhanFragment extends Fragment {

    AvatarImageView avatarImageView;
    EditText edtTenNguoiDung, edtEmailOrPhonenumber, edtNgaysinh, edtDiachi, edtMatkhau;
    Button btnCapnhat, btnDangXuat;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;
    FirebaseStorage firebaseStorage;
    StorageReference stoRef;

    String idNguoiDung;
    NguoiDungUtils utils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan, container, false);

        initView(view);

        if (getContext() != null) {
            utils = new NguoiDungUtils(getContext());
            idNguoiDung = utils.getIdUser();
            hienThiThongTinNguoiDung(idNguoiDung, getContext());
        }

        btnCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmailOrPhonenumber.getText().toString();
                String matkhau = edtMatkhau.getText().toString();
                String tennguoidung = edtTenNguoiDung.getText().toString();
                String diachi = edtDiachi.getText().toString();
                String ngaysinh = edtNgaysinh.getText().toString();

                capNhatThongTinTaikhoan(tennguoidung, email, diachi, matkhau, ngaysinh);
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() != null) {
                    utils = new NguoiDungUtils(getContext());
                    utils.setIdUser(null);
                    Intent intent = new Intent(getContext(), PizzaPlantActivity.class);
                    startActivity(intent);
                }
            }
        });

        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  requestPermission();
                chonAnTrongThuVien();
            }
        });

        return view;
    }

    private void requestPermission(){
        Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        chonAnTrongThuVien();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
    }

    private void chonAnTrongThuVien() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, Constant.REQUEST_CODE_OPEN_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && requestCode == Constant.REQUEST_CODE_OPEN_GALLERY && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
                avatarImageView.setImageURI(uri);
                capNhatAnhDaiDien(uri);
            }
        }
    }

    private void capNhatThongTinTaikhoan(String a, String b, String c, String d, String e) {
        dataRef = firebaseDatabase.getReference().child("NguoiDung");
        dataRef.child(idNguoiDung).child("tennguoidung").setValue(a);
        dataRef.child(idNguoiDung).child("emailorPhoneNumber").setValue(b);
        dataRef.child(idNguoiDung).child("diachi").setValue(c);
        dataRef.child(idNguoiDung).child("matkhau").setValue(d);
        dataRef.child(idNguoiDung).child("ngaysinh").setValue(e);

        Toast.makeText(getContext(), "Cập nhật thành công !!!", Toast.LENGTH_SHORT).show();
    }

    private void hienThiThongTinNguoiDung(String id, final Context context) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("NguoiDung");
        dataRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                NguoiDung nguoiDung = snapshot.getValue(NguoiDung.class);
                if (nguoiDung != null) {
                    if (nguoiDung.getAnhdaidien().equals(" ")) {
                        avatarImageView.setState(AvatarImageView.SHOW_INITIAL);
                        avatarImageView.setText(nguoiDung.getTennguoidung());
                    } else {
                        avatarImageView.setState(AvatarImageView.SHOW_IMAGE);
                        Glide.with(context).load(nguoiDung.getAnhdaidien()).into(avatarImageView);
                    }
                    edtTenNguoiDung.setText(nguoiDung.getTennguoidung());
                    edtDiachi.setText(nguoiDung.getDiachi());
                    edtMatkhau.setText(nguoiDung.getMatkhau());
                    edtNgaysinh.setText(nguoiDung.getNgaysinh());
                    edtEmailOrPhonenumber.setText(nguoiDung.getEmailorPhoneNumber());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void capNhatAnhDaiDien(Uri uri) {
        firebaseStorage = FirebaseStorage.getInstance();
        stoRef = firebaseStorage.getReference().child("image_anhdaidien");
        final StorageReference storageReference = stoRef.child(uri.getLastPathSegment());
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (uri != null){
                            String linkImage = uri.toString();
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            dataRef = firebaseDatabase.getReference().child("NguoiDung");
                            dataRef.child(idNguoiDung).child("anhdaidien").setValue(linkImage);
                            Toast.makeText(getContext(), "Ảnh đại diện đã được cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initView(View view) {
        avatarImageView = view.findViewById(R.id.avatarimageview_anhdaidien);
        edtTenNguoiDung = view.findViewById(R.id.edittext_tennguoidung);
        edtDiachi = view.findViewById(R.id.edittext_diachi);
        edtEmailOrPhonenumber = view.findViewById(R.id.edittext_email);
        edtMatkhau = view.findViewById(R.id.edittext_matkhau);
        edtNgaysinh = view.findViewById(R.id.edittext_ngaysinh);
        btnCapnhat = view.findViewById(R.id.button_commit);
        btnDangXuat = view.findViewById(R.id.button_logout);
    }
}
