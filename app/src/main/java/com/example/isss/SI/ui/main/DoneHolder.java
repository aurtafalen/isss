package com.example.isss.SI.ui.main;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;

public class DoneHolder extends RecyclerView.ViewHolder{
    private View view;


    DoneHolder(View itemView) {
        super(itemView);

        view = itemView;

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v,getAdapterPosition());
            }
        });
    }

    void settemplateTeam(String templateTeam){
        TextView textView = view.findViewById(R.id.Tteam);
        textView.setText(templateTeam);
    }

    void setTgroup(String group) {
        TextView textView1 = view.findViewById(R.id.Tgroup1);
        textView1.setText(group);

        TextView textView2 = view.findViewById(R.id.Tgroup2);
        textView1.setText(group);

        if (group != null){
            if (group.equals("FSD")) {
                textView1.setVisibility(View.INVISIBLE);
            }else{
                textView2.setVisibility(View.INVISIBLE);
            }
        }

    }

    void settemplateAddress(String templateAddress) {
        TextView textView = view.findViewById(R.id.Tlokasi);
        textView.setText(templateAddress);
    }

    void settemplateTitle(String templateTitle) {
        TextView textView = view.findViewById(R.id.Ttitle);
        textView.setText(templateTitle);
    }

    void setstatus(String status){
        TextView textView = view.findViewById(R.id.Tstatus);
        textView.setText(status);
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
