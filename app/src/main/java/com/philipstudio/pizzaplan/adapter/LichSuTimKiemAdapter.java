package com.philipstudio.pizzaplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.philipstudio.pizzaplan.R;
import com.robertlevonyan.views.chip.Chip;
import com.robertlevonyan.views.chip.OnCloseClickListener;

import java.util.ArrayList;

public class LichSuTimKiemAdapter extends RecyclerView.Adapter<LichSuTimKiemAdapter.ViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    private OnItemChipCliclListener onItemChipCliclListener;

    public LichSuTimKiemAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public void setOnItemChipCliclListener(OnItemChipCliclListener onItemChipCliclListener) {
        this.onItemChipCliclListener = onItemChipCliclListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tukhoatimkiem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.chip.setClosable(true);
        holder.chip.setText(arrayList.get(position));
        holder.chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemChipCliclListener != null){
                    onItemChipCliclListener.onItem(arrayList.get(position));
                }
            }
        });

        holder.chip.setOnCloseClickListener(new OnCloseClickListener() {
            @Override
            public void onCloseClick(View v) {
                arrayList.remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        Chip chip;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip);
        }
    }

    public interface  OnItemChipCliclListener{
        void onItem(String text);
    }
}
