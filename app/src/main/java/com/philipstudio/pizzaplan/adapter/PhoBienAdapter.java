package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.view.ChiTietMonAnActivity;

import java.util.ArrayList;

public class PhoBienAdapter extends RecyclerView.Adapter<PhoBienAdapter.ViewHolder> {

    ArrayList<MonAn> arrayList;
    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dataRef;

    public PhoBienAdapter(ArrayList<MonAn> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theloai_phobien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtTenMonAn.setText(arrayList.get(position).getTenMonAn());
        holder.txtGia.setText(String.valueOf(arrayList.get(position).getGia()));
        Glide.with(context).load(arrayList.get(position).getAnh()).into(holder.imgAnh);

        firebaseDatabase = FirebaseDatabase.getInstance();
        dataRef = firebaseDatabase.getReference().child("GioHang");
        holder.btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpButtonChon(holder.btnChon);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnh;
        TextView txtTenMonAn, txtGia;
        Button btnChon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnh = itemView.findViewById(R.id.item_imageview_anh);
            txtTenMonAn = itemView.findViewById(R.id.item_textview_tenmonan);
            txtGia = itemView.findViewById(R.id.item_textview_gia);
            btnChon = itemView.findViewById(R.id.button_chon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(context, ChiTietMonAnActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("dataMonAn", arrayList.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    private void setUpButtonChon(Button btnClick){
        btnClick.setText("Đã chọn");
        btnClick.setTextColor(Color.BLACK);
        btnClick.setBackgroundResource(R.drawable.custom_button_dachon);
    }
}
