package com.example.isss.SI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.isss.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ListInduction extends AppCompatActivity {
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<GetDataInduction, ListInductionHolder> adapterCardInduction;
    String documentClickId;
    FloatingActionButton tambahInduction;
    TextView tgl;
    Button datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_induction);

        tgl = findViewById(R.id.tanggal);
        String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
        tgl.setText(dateNow);
        datePicker = findViewById(R.id.bTgl);

        tambahInduction = findViewById(R.id.tambahInduction);
        tambahInduction.setBackgroundColor(Color.WHITE);
        tambahInduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(ListInduction.this, Induction.class);
                startActivity(tambah);
            }
        });

        recyclerView = findViewById(R.id.listInduction);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseFirestore.getInstance().collection("si").document("data_induction").collection("induction")
//                .orderBy("timeDate",Query.Direction.DESCENDING)
                .whereEqualTo("filterBulan",tgl.getText());

        FirestoreRecyclerOptions<GetDataInduction> options = new FirestoreRecyclerOptions.Builder<GetDataInduction>()
                .setQuery(query, GetDataInduction.class)
                .build();

        adapterCardInduction = new FirestoreRecyclerAdapter<GetDataInduction, ListInductionHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListInductionHolder holder, int position, @NonNull GetDataInduction getDataInduction) {
                holder.setNama((getDataInduction.getNama()));
                holder.setPrusahaan(getDataInduction.getPrusahaan());
                holder.setJumlahPeserta("Jumlah Peserta  : "+ getDataInduction.getJumlahPeserta()+" Orang");
                holder.setStatus(getDataInduction.getStatus());
                holder.setTimedate(getDataInduction.getTimeDate());

                holder.setOnClickListener(new ListInductionHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        documentClickId = getSnapshots().getSnapshot(position).getId();
                        Log.d("getclickdoc", documentClickId);

//                        Intent keIsiTugas = new Intent(ListInduction.this, Induction.class);
//                        keIsiTugas.putExtra("clickedId",documentClickId);
//                        startActivity(keIsiTugas);


                    }
                });

            }

            @NonNull
            @Override
            public ListInductionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_induction, parent, false);
                return new ListInductionHolder(view);
            }
        };
        adapterCardInduction.startListening();
        recyclerView.setAdapter(adapterCardInduction);
    }
}