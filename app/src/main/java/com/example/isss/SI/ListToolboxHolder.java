package com.example.isss.SI;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;

public class ListToolboxHolder extends RecyclerView.ViewHolder{
    private View view;


    ListToolboxHolder(View itemView) {
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
        TextView textView = view.findViewById(R.id.tanggal);
        textView.setText(timedate);
    }

    void setInput(String input){
        TextView textView = view.findViewById(R.id.input);
        textView.setText(input);
    }


    void setLokasi(String lokasi) {
        TextView textView = view.findViewById(R.id.lokasiListToolbox);
        textView.setText(lokasi);
    }

    void setKehadiran(String kehadiran){
        TextView textView = view.findViewById(R.id.kehadiranListToolbox);
        textView.setText(kehadiran);
    }

    void setTanggapan(String tanggapan){
        TextView textView = view.findViewById(R.id.tanggapanListToolbox);
        textView.setText(tanggapan);

    }

    void setTargetWaktu(String targetWaktu){
        TextView textView = view.findViewById(R.id.targetwaktu);
        textView.setText(targetWaktu);

    }
    void setStatus(String status){
        TextView textView = view.findViewById(R.id.statusListToolbox);
        textView.setText(status);

    }
    void setTopik(String topik){
        TextView textView = view.findViewById(R.id.topik);
        textView.setText(topik);

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
