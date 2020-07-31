package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philipstudio.pizzaplan.R;
import com.philipstudio.pizzaplan.model.MonAn;
import com.philipstudio.pizzaplan.model.TheLoaiMonAn;
import com.philipstudio.pizzaplan.utils.Constant;
import com.philipstudio.pizzaplan.view.TheLoaiActivity;

import java.util.ArrayList;

public class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.ViewHolder> {

    ArrayList<TheLoaiMonAn> arrayList;
    Context context;
    private int viewType;

    public MenuOrderAdapter(ArrayList<TheLoaiMonAn> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position).getViewType();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.viewType = viewType;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txtTenTheLoai.setText(arrayList.get(position).getTenTheLoai());
        switch (viewType){
            case Constant.ITEM_POPULAR:
                holder.rVDanhSachMonAn.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
                holder.rVDanhSachMonAn.setLayoutManager(linearLayoutManager);
                //Lay cac mon an theo the loai la pho bien
                ArrayList<MonAn> monAnArrayList1 = arrayList.get(position).getMonAnArrayList();
                PhoBienAdapter phobienAdapter = new PhoBienAdapter(monAnArrayList1, context);
                holder.rVDanhSachMonAn.setAdapter(phobienAdapter);
                break;


            case Constant.ITEM_TRENDING:
                holder.rVDanhSachMonAn.setHasFixedSize(true);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                holder.rVDanhSachMonAn.setLayoutManager(gridLayoutManager);
                //Lay cac mon an theo the loai la xu huong
                ArrayList<MonAn> monAnArrayList2 = arrayList.get(position).getMonAnArrayList();
                XuHuongAdapter xuHuongAdapter = new XuHuongAdapter(monAnArrayList2, context);
                holder.rVDanhSachMonAn.setAdapter(xuHuongAdapter);
                break;
        }

        holder.txtXemThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TheLoaiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("theLoaiMonAn", arrayList.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtTenTheLoai, txtXemThem;
        RecyclerView rVDanhSachMonAn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTenTheLoai = itemView.findViewById(R.id.item_textview_tentheloai);
            txtXemThem = itemView.findViewById(R.id.item_textview_xemthem);
            rVDanhSachMonAn = itemView.findViewById(R.id.item_recyclerview_danhsach_theloai);
        }
    }
}
