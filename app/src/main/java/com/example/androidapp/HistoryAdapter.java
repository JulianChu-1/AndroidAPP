package com.example.androidapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<String> dataList;

    public HistoryAdapter(List<String> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutRes;
        switch (viewType) {
            case 0:
                layoutRes = R.layout.history_item_layout_right;
                break;
            default:
                layoutRes = R.layout.history_item_layout_left;
                break;
        }
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the item view
        String item = dataList.get(position);
        holder.messageTextView.setText(item);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public int getItemViewType(int position) {
        // 根据数据项的内容判断视图类型
        String item = dataList.get(position);
        if (item.contains("ME")) {
            return 0; // 视图类型 A
        } else {
            return 1; // 视图类型 B
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextView_text);
        }
    }
}
