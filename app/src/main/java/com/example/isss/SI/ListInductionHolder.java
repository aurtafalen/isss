package com.example.isss.SI;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;

public class ListInductionHolder extends RecyclerView.ViewHolder{
    private View view;


    ListInductionHolder(View itemView) {
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

    void setNama(String nama){
        TextView textView = view.findViewById(R.id.nama);
        textView.setText(nama);
    }


    void setPrusahaan(String prusahaan) {
        TextView textView = view.findViewById(R.id.prusahaan);
        textView.setText(prusahaan);
    }

    void setJumlahPeserta(String jumlahPeserta){
        TextView textView = view.findViewById(R.id.jumlah);
        textView.setText(jumlahPeserta);
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
