package com.example.isss.SI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class InspeksiHasil extends AppCompatActivity {


    String status,month;
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<GetDataHasil, HasilHolder> adaptercard;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_hasil);

//        //spiner
//        Spinner spinner = (Spinner) findViewById(R.id.sfilter);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(InspeksiHasil.this,
//                R.array.filtergroup, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        status = getIntent().getStringExtra("status");
        month = getIntent().getStringExtra("month");

        recyclerView = findViewById(R.id.recycler_ViewDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (status == null){
            query = FirebaseFirestore.getInstance()
                    .collection("si")
                    .document("data_inspections")
                    .collection("inspections")
                    .whereEqualTo("templateMonth",month)
                    .orderBy("status");
        }else{
            query = FirebaseFirestore.getInstance()
                    .collection("si")
                    .document("data_inspections")
                    .collection("inspections")
                    .whereEqualTo("templateMonth",month)
                    .whereEqualTo("status",status);
        }


        FirestoreRecyclerOptions<GetDataHasil> options = new FirestoreRecyclerOptions.Builder<GetDataHasil>()
                .setQuery(query, GetDataHasil.class)
                .build();

        adaptercard = new FirestoreRecyclerAdapter<GetDataHasil, HasilHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HasilHolder holder, int position, @NonNull GetDataHasil getDataHasil) {
                holder.settemplateTeam(("Pelaksana : "+getDataHasil.getTemplateTeam()));
                holder.setTgroup(getDataHasil.getTemplateGroup());
                holder.settemplateAddress("Lokasi : "+ getDataHasil.getTemplateAddress());
                holder.settemplateTitle(getDataHasil.getTemplateTitle());
                holder.setstatus("Status : "+getDataHasil.getStatus());


            }

            @NonNull
            @Override
            public HasilHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laporandone, parent, false);
                return new HasilHolder(view);
            }
        };
        adaptercard.startListening();
        recyclerView.setAdapter(adaptercard);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent kembali = new Intent(InspeksiHasil.this,Side_SI.class);
        startActivity(kembali);
        finish();
    }
}