package com.example.androidapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<String> mDataList;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener; // Define the OnItemLongClickListener interface
    public MyAdapter(List<String> dataList, OnItemClickListener listener) {
        mDataList = dataList;
        mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String item = mDataList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(String item) {
            mTextView.setText(item);
        }

        @Override
        public void onClick(View v) {
            String item = mDataList.get(getAdapterPosition());
            mListener.onItemClick(item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String item);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(String item);

        void onItemLongClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        mLongListener = listener;
    }

}
