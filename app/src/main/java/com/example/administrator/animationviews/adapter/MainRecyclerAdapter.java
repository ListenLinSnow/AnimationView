package com.example.administrator.animationviews.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.animationviews.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<String> infoList;
    OnItemClickListener onItemClickListener;

    public MainRecyclerAdapter(Context context, List<String> infoList) {
        this.context = context;
        this.infoList = infoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.view_recycler_item, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MainRecyclerViewHolder) holder).infoTv.setText(infoList.get(position));
    }

    @Override
    public int getItemCount() {
        return infoList.size();
    }

    class MainRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.view_main_recycler_item_view)
        RelativeLayout view;
        @BindView(R.id.view_main_recycler_item_info_tv)
        TextView infoTv;

        private OnItemClickListener onItemClickListener;

        public MainRecyclerViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.onItemClickListener = onItemClickListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                onItemClickListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
