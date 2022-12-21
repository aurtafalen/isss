package com.example.isss.SI.ui.slideshow;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;

public class TugasHolder extends RecyclerView.ViewHolder{
    private View view;


    TugasHolder(View itemView) {
        super(itemView);

        view = itemView;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });
    }

    void setDesk(String deskripsi){
        TextView textView = view.findViewById(R.id.desk);
        textView.setText(deskripsi);
    }


    void setTitle(String titleTugas) {
        TextView textView = view.findViewById(R.id.judul);
        textView.setText(titleTugas);
    }

    void setTeam(String teamTugas){
        TextView textView = view.findViewById(R.id.team);
        textView.setText(teamTugas);
    }

    void setStatus(String status){
        TextView textView = view.findViewById(R.id.status);
        textView.setText(status);
//        if (status == null){
//            textView.setText("Belum Selesai");
//            textView.setTextColor(Color.RED);
//        }else if (status.equals("Selesai")){
//            textView.setTextColor(Color.GREEN);
//        }
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
