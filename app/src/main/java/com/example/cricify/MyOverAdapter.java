package com.example.cricify;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyOverAdapter extends RecyclerView.Adapter<MyOverAdapter.MyViewHolder> {

    ArrayList<String> mList;
    Context context;

    public MyOverAdapter(Context context , ArrayList<String> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.over_item , parent ,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String over = mList.get(position);
        holder.sno.setText((position+1)+"). ");
        holder.overs_.setText(over);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{
        TextView sno;
        TextView overs_;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sno = itemView.findViewById(R.id.sno);
            overs_ = itemView.findViewById(R.id.over_);
        }
    }
}