package com.example.mana1.sampletest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

public class Transit_Adapter extends RecyclerView.Adapter implements View.OnClickListener {

    private List<Transit_Get> list;
    private Context context;
    public Transit_Adapter(List<Transit_Get> list, Context context) {
        this.list = list;
        this.context=context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    private OnItemClickListener mOnItemClickListener = null;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transit_recyclerview, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int itemPosition = position;
        final Transit_Get get_Route_Method = list.get(itemPosition);

        final ViewHolder vh = (ViewHolder) holder;
        vh.routeMethod.setText(get_Route_Method.getTitle());
        Log.d("test Height", String.valueOf(MapsActivity.AlertDialog_Height));
        vh.mframe_Route.setLayoutParams(new FrameLayout.LayoutParams(1195, 1608/list.size()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView routeMethod;
        public FrameLayout mframe_Route;
        public ViewHolder(View view){
            super(view);
            routeMethod=(TextView)view.findViewById(R.id.txtRoute_Choose);
            mframe_Route=(FrameLayout)view.findViewById(R.id.Frame_Route);
        }
    }
}
