package com.example.cricify;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    ArrayList<Model> mList;
    Context context;

    public MyAdapter(Context context , ArrayList<Model> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item , parent ,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Model model = mList.get(position);
        holder.name.setText(model.getName());
        holder.runs.setText(model.getRuns().toString());
        holder.balls.setText(model.getBalls().toString());
        holder.fours.setText(model.getBoundaries().toString());
        holder.sixes.setText(model.getSixes().toString());
        holder.strikerate.setText(model.getStrikeRate().toString());

        holder.overs.setText(model.getOvers().toString());
        holder.runs_given.setText(model.getRuns().toString());
        holder.wicketsTAken.setText(model.getWicketsTaken().toString());
        holder.economy.setText(model.getEconomyRate().toString());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView runs,balls,fours,sixes;
        TextView strikerate,overs,runs_given,wicketsTAken,economy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.FBatName);
            runs = itemView.findViewById(R.id.RunsOnStrike);
            balls = itemView.findViewById(R.id.BallsOnStrike);
            fours = itemView.findViewById(R.id.Fours_onStrike);
            sixes = itemView.findViewById(R.id.Sixes_onStrike);
            strikerate = itemView.findViewById(R.id.SR_OnStrike);

            overs = itemView.findViewById(R.id.Overs_onStrike);
            runs_given = itemView.findViewById(R.id.Runs_Given);
            wicketsTAken = itemView.findViewById(R.id.wickets_Taken);
            economy = itemView.findViewById(R.id.EconomyRate);
        }
    }
}