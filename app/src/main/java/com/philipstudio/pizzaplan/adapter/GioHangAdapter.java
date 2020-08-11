package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.GioHang;
import com.philipstudio.pizzaplan.utils.NguoiDungUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.ViewHolder> {

    ArrayList<GioHang> arrayList;
    Context context;
    int vitri;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference dataRef = firebaseDatabase.getReference().child("GioHang");

    public GioHangAdapter(ArrayList<GioHang> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        NguoiDungUtils utils = new NguoiDungUtils(context);
        final String id = utils.getIdUser();

        Glide.with(context).load(arrayList.get(position).getMonAn().getAnh()).into(holder.imgAnh);
        holder.txtTenMonAn.setText(arrayList.get(position).getMonAn().getTenMonAn());
        NumberFormat formatter = new DecimalFormat("#,###");
        double giatien = arrayList.get(position).getMonAn().getGia();
        String formattedGiatien = formatter.format(giatien);
        holder.txtGia.setText(formattedGiatien + " " + "đồng");
        holder.txtNguyenlieu.setText(arrayList.get(position).getMonAn().getNguyenLieu());

        holder.txtSoluong.setText(String.valueOf(arrayList.get(position).getSoluong()));

        holder.imgButtonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluong = arrayList.get(position).getSoluong();
                soluong++;
                holder.txtSoluong.setText(String.valueOf(soluong));
                dataRef.child(id).child(arrayList.get(position).getId()).child("soluong").setValue(soluong);
            }
        });

        holder.imgButtonGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluong = arrayList.get(position).getSoluong();
                soluong--;
                if(soluong >= 0){
                    holder.txtSoluong.setText(String.valueOf(soluong));
                    dataRef.child(id).child(arrayList.get(position).getId()).child("soluong").setValue(soluong);
                }
            }
        });

        holder.imgButtonXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(context, id, arrayList);
                vitri = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAnh;
        TextView txtTenMonAn, txtGia, txtNguyenlieu, txtSoluong;
        ImageButton imgButtonThem, imgButtonGiam, imgButtonXoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnh = itemView.findViewById(R.id.item_imageview_anh);
            txtTenMonAn = itemView.findViewById(R.id.item_textview_tenmonan);
            txtGia = itemView.findViewById(R.id.item_textview_gia);
            txtNguyenlieu = itemView.findViewById(R.id.item_textview_nguyenlieu);
            txtSoluong = itemView.findViewById(R.id.item_textview_soluong);
            imgButtonThem = itemView.findViewById(R.id.item_imagebutton_them);
            imgButtonGiam = itemView.findViewById(R.id.item_imagebutton_giam);
            imgButtonXoa = itemView.findViewById(R.id.imagebutton_xoa);
        }
    }

    private void showAlertDialog(Context context, final String id, final ArrayList<GioHang> gioHangs){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn đã chắc chắn muốn xoá item này khỏi giỏ hàng không ?");
        builder.setPositiveButton("Có, đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataRef.child(id).child(gioHangs.get(vitri).getId()).removeValue();
                gioHangs.remove(vitri);
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
