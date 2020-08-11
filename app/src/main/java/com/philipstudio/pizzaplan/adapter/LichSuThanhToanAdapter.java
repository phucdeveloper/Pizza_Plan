package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.abdularis.civ.AvatarImageView;
import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.DonHang;

import java.util.ArrayList;

public class LichSuThanhToanAdapter extends RecyclerView.Adapter<LichSuThanhToanAdapter.ViewHolder> {

    ArrayList<DonHang> arrayList;
    Context context;

    OnItemClickLichSuThanhToanListener onItemClickLichSuThanhToanListener;

    public LichSuThanhToanAdapter(ArrayList<DonHang> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setOnItemClickLichSuThanhToanListener(OnItemClickLichSuThanhToanListener onItemClickLichSuThanhToanListener) {
        this.onItemClickLichSuThanhToanListener = onItemClickLichSuThanhToanListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lichsuthanhtoan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtIdDonhang.setText(arrayList.get(position).getIdDonHang());
        holder.txtThoigian.setText(arrayList.get(position).getThoigian());
        holder.txtTongtien.setText(String.valueOf(arrayList.get(position).getTongtien()));
        holder.txtDiachi.setText(arrayList.get(position).getDiadiem());

        holder.txtChitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickLichSuThanhToanListener != null){
                    onItemClickLichSuThanhToanListener.OnItemClick(context, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtChitiet, txtIdDonhang, txtThoigian, txtDiachi, txtTongtien;
        AvatarImageView avatarImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtChitiet = itemView.findViewById(R.id.item_textview_chitietdonhang);
            txtIdDonhang = itemView.findViewById(R.id.item_textview_iddathang);
            txtDiachi = itemView.findViewById(R.id.item_textview_diachigiaohang);
            txtTongtien = itemView.findViewById(R.id.item_textview_tongtien);
            txtThoigian = itemView.findViewById(R.id.item_textview_thoigian);
            avatarImageView = itemView.findViewById(R.id.item_avatarimageview_anhdaidien);
        }
    }

    public interface OnItemClickLichSuThanhToanListener{
        void OnItemClick(Context context, int position);
    }
}
