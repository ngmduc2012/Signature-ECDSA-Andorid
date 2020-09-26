package com.example.signature.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signature.Modle.InfoApp;
import com.example.signature.R;

import java.util.List;

public class RecycleViewInfoAdapter extends RecyclerView.Adapter<RecycleViewInfoAdapter.MyViewHolder> {
    Context mContext;
    List<InfoApp> mData;

    public RecycleViewInfoAdapter(Context mContext, List<InfoApp> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_row_info, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // set data for item_row_info
        holder.tv_info.setText(mData.get(position).getInfo());
        holder.iv_info.setImageResource(mData.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_info;
        private ImageView iv_info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_info = itemView.findViewById(R.id.iv_info);
            tv_info = itemView.findViewById(R.id.tv_info);
        }
    }
}
