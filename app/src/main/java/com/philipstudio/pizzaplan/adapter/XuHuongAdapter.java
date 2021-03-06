package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.view.ChiTietMonAnActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class XuHuongAdapter extends RecyclerView.Adapter<XuHuongAdapter.ViewHolder> {

    ArrayList<MonAn> arrayList;
    Context context;

    public XuHuongAdapter(ArrayList<MonAn> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theloai_xuhuong, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txtTenMonAn.setText(arrayList.get(position).getTenMonAn());
        NumberFormat formatter = new DecimalFormat("#,###");
        double giatien = arrayList.get(position).getGia();
        String formattedGiatien = formatter.format(giatien);
        holder.txtGia.setText(formattedGiatien + " " + "đồng");
        Glide.with(context).load(arrayList.get(position).getAnh()).into(holder.imgAnh);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnh;
        TextView txtTenMonAn, txtGia;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imgAnh = itemView.findViewById(R.id.item_imageview_anh);
            txtTenMonAn = itemView.findViewById(R.id.item_textview_tenmonan);
            txtGia = itemView.findViewById(R.id.item_textview_gia);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();
                    Intent intent = new Intent(context, ChiTietMonAnActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("dataMonAn", arrayList.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }
}
