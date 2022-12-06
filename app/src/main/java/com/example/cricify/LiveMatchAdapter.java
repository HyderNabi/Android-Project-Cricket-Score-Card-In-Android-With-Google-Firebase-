package com.example.cricify;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class LiveMatchAdapter extends RecyclerView.Adapter<LiveMatchAdapter.MyViewHolder> {

    ArrayList<LiveMatchModel> mList;
    Context context;

    public LiveMatchAdapter(Context context , ArrayList<LiveMatchModel> mList){

        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.livematchitem, parent ,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LiveMatchModel model = mList.get(position);
        holder.name.setText(model.getName().toString());
        holder.matchType.setText(model.getMatchType().toString());
        holder.venue.setText(model.getVenue().toString());
        holder.date.setText(model.getDate().toString());
        holder.time.setText(model.getTime().toString());
        holder.status.setText(model.getStatus().toString());

        String teams[] = model.getTeams();
        holder.teams.setText(teams[0].toString()+" & "+teams[1].toString());
        holder.team1.setText(teams[0].toString());
        holder.team2.setText(teams[1].toString());

        String runs[] = model.getRuns();
        String wickets[] = model.getWickets();
        String over[] = model.getOvers();



        if (runs.length == 4 || runs.length == 3) {
            holder.extraInnings.setVisibility(holder.itemView.VISIBLE);
        }else{
            holder.extraInnings.setVisibility(holder.itemView.GONE);
        }



        if(runs.length == 4) {
            String score1 = runs[0].toString() + " / " + wickets[0].toString();
            String score2 = runs[1].toString() + " / " + wickets[1].toString();
            String score11 = runs[2].toString() + " / " + wickets[2].toString();
            String score22 = runs[3].toString() + " / " + wickets[3].toString();

            String overs1 = over[0].toString();
            String overs2 = over[1].toString();
            String overs11 = over[2].toString();
            String overs22 = over[3].toString();

            holder.score1.setText(score1);
            holder.score2.setText(score2);
            holder.score11.setText(score11);
            holder.score22.setText(score22);

            holder.overs1.setText(overs1);
            holder.overs2.setText(overs2);
            holder.overs11.setText(overs11);
            holder.overs22.setText(overs22);

            holder.inning2.setText("Inning 1");

        }else if(runs.length == 3) {
            String score1 = runs[0].toString() + " / " + wickets[0].toString();
            String score2 = runs[1].toString() + " / " + wickets[1].toString();
            String score11 = runs[2].toString() + " / " + wickets[2].toString();
            String score22 = "0/0";

            String overs1 = over[0].toString();
            String overs2 = over[1].toString();
            String overs11 = over[2].toString();
            String overs22 = "0";

            holder.score1.setText(score1);
            holder.score2.setText(score2);
            holder.score11.setText(score11);
            holder.score22.setText(score22);

            holder.overs1.setText(overs1);
            holder.overs2.setText(overs2);
            holder.overs11.setText(overs11);
            holder.overs22.setText(overs22);
            holder.inning2.setText("Inning 1");
        }else if(runs.length == 2){
            String score1 = runs[0].toString()+" / "+wickets[0].toString();
            String score2 = runs[1].toString()+" / "+wickets[1].toString();

            String overs1 = over[0].toString();
            String overs2 = over[1].toString();

            holder.score1.setText(score1);
            holder.score2.setText(score2);

            holder.overs1.setText(overs1);
            holder.overs2.setText(overs2);

        }else if(runs.length == 1){
            String score1 = runs[0].toString()+" / "+wickets[0].toString();
            String score2 = "0/0";

            String overs1 = over[0].toString();
            String overs2 = "0";

            holder.score1.setText(score1);
            holder.score2.setText(score2);

            holder.overs1.setText(overs1);
            holder.overs2.setText(overs2);
        }else{
            String score1 = "0/0";
            String score2 = "0/0";

            String overs1 = "0";
            String overs2 = "0";

            holder.score1.setText(score1);
            holder.score2.setText(score2);

            holder.overs1.setText(overs1);
            holder.overs2.setText(overs2);
        }


        String ended = model.getMatchEnded();
        String started = model.getMatchStarted();
        if(started.equals("true") && ended.equals("false")){
            if(model.getStatus().contains("No result") || model.getStatus().contains("Match tied") || model.getStatus().contains("Match Tied") || model.getStatus().contains("No Result"))
            {
                holder.islive.setVisibility(holder.itemView.GONE);
            }else{
                holder.islive.setVisibility(holder.itemView.VISIBLE);
            }

        }else{
            holder.islive.setVisibility(holder.itemView.GONE);
        }

        if(ended.equals("true")){
            holder.finished.setVisibility(holder.itemView.VISIBLE);
        }else{
            if(model.getStatus().contains("No result") || model.getStatus().contains("Match tied") || model.getStatus().contains("Match Tied") || model.getStatus().contains("No Result"))
            {
                holder.finished.setVisibility(holder.itemView.VISIBLE);
            }else{
                holder.finished.setVisibility(holder.itemView.GONE);
            }
        }



        String URLS[] = model.getURLS();
        try{
            new DownloadImageTask(holder.t1).execute(URLS[0]);
            new DownloadImageTask(holder.t2).execute(URLS[1]);

            new DownloadImageTask(holder.t3).execute(URLS[0]);
            new DownloadImageTask(holder.t4).execute(URLS[1]);
        }catch(Exception e){}


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {}
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }




    public static  class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,islive,matchType,venue,date,time,status,teams;
        TextView team1,team2,score1,score2,overs1,overs2;
        TextView score11,score22,overs11,overs22,finished,inning2;
        ImageView t1,t2,t3,t4;
        RelativeLayout extraInnings;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            islive = itemView.findViewById(R.id.islive);
            matchType = itemView.findViewById(R.id.matchType);
            venue = itemView.findViewById(R.id.venue);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            status = itemView.findViewById(R.id.status);
            teams = itemView.findViewById(R.id.teams);

            team1 = itemView.findViewById(R.id.team1);
            team2 = itemView.findViewById(R.id.team2);

            score1 = itemView.findViewById(R.id.score1);
            score2 = itemView.findViewById(R.id.score2);

            overs1 = itemView.findViewById(R.id.overs1);
            overs2 = itemView.findViewById(R.id.overs2);

            score11 = itemView.findViewById(R.id.score11);
            score22 = itemView.findViewById(R.id.score22);

            overs11 = itemView.findViewById(R.id.overs11);
            overs22 = itemView.findViewById(R.id.overs22);

            finished = itemView.findViewById(R.id.finish);

            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);

            t3 = itemView.findViewById(R.id.t3);
            t4 = itemView.findViewById(R.id.t4);

            extraInnings = itemView.findViewById(R.id.extraInnings);

            inning2 = itemView.findViewById(R.id.inning2);


        }
    }
}