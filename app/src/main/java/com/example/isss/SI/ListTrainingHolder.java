package com.example.isss.SI;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;

public class ListTrainingHolder extends RecyclerView.ViewHolder{
    private View view;


    ListTrainingHolder(View itemView) {
        super(itemView);

        view = itemView;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });
    }

    void setTimedate(String timedate){
        TextView textView = view.findViewById(R.id.tanggalListTraining);
        textView.setText(timedate);
    }

    void setTrainer(String trainer){
        TextView textView = view.findViewById(R.id.trainerListTraining);
        textView.setText(trainer);
    }


    void setLokasi(String lokasi) {
        TextView textView = view.findViewById(R.id.lokasiListTraining);
        textView.setText(lokasi);
    }

    void setKehadiran(String kehadiran){
        TextView textView = view.findViewById(R.id.kehadiranListTraining);
        textView.setText(kehadiran);
    }

    void setDeskripsi(String deskripsi){
        TextView textView = view.findViewById(R.id.deskripsiListTraining);
        textView.setText(deskripsi);

    }

    private ClickListener mClickListener;

    //Interface to send callbacks...
    public interface ClickListener{
        void onItemClick(View view, int position);

    }
    public void setOnClickListener(ClickListener clickListener){
        mClickListener = clickListener;
    }
}
