package com.example.isss.SI.ui.slideshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;

public class AdapterRecyclerViewTugas extends RecyclerView.Adapter<AdapterRecyclerViewTugas.ViewHolder> {

    private String[] SubjectValues;
    private Context context;

    AdapterRecyclerViewTugas(Context context1, String[] SubjectValues1) {

        SubjectValues = SubjectValues1;
        context = context1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnlanjut;
        TextView textView;


        ViewHolder(View v) {

            super(v);

            textView = v.findViewById(R.id.judul);
            btnlanjut = v.findViewById(R.id.btnlanjut);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_laporatugas, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(SubjectValues[position]);
        holder.btnlanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, InspeksiAwal.class);
//                context.startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {

        return SubjectValues.length;
    }
}
