package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class KetQuaAdapter extends RecyclerView.Adapter<KetQuaAdapter.ViewHolder> {
    ArrayList<MonAn> arrayList;
    Context context;

    public KetQuaAdapter(ArrayList<MonAn> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ketqua, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getAnh()).into(holder.imgAnh);
        holder.txtTenMonAn.setText(arrayList.get(position).getTenMonAn());
        holder.txtNguyenlieu.setText(arrayList.get(position).getNguyenLieu());
        NumberFormat formatter = new DecimalFormat("#,###");
        double giatien = arrayList.get(position).getGia();
        String formattedGiatien = formatter.format(giatien);
        holder.txtGia.setText(formattedGiatien + " " + "đồng");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAnh;
        TextView txtTenMonAn, txtGia, txtNguyenlieu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAnh = itemView.findViewById(R.id.item_imageview_anh);
            txtTenMonAn = itemView.findViewById(R.id.item_textview_tenmonan);
            txtGia = itemView.findViewById(R.id.item_textview_gia);
            txtNguyenlieu = itemView.findViewById(R.id.item_textview_nguyenlieu);

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
}
