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
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ListTraining extends AppCompatActivity {
    RecyclerView recyclerView;
    FirestoreRecyclerAdapter<GetDataTraining, ListTrainingHolder> adapterCardInduction;
    String documentClickId;
    FloatingActionButton tambahInduction;
    TextView tgl;
    Button datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_training);

        tambahInduction = findViewById(R.id.tambahInduction);
        tambahInduction.setBackgroundColor(Color.WHITE);
        tambahInduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tambah = new Intent(ListTraining.this, Training.class);
                startActivity(tambah);
            }
        });

        tgl = findViewById(R.id.tanggal);
        String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
        tgl.setText(dateNow);

        datePicker = findViewById(R.id.bTgl);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monthYear();
            }
        });

        recyclerView = findViewById(R.id.listInduction);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseFirestore.getInstance()
                .collection("training")
//                .orderBy("timeDate",Query.Direction.DESCENDING)
                .whereEqualTo("filterBulan",tgl.getText());

        FirestoreRecyclerOptions<GetDataTraining> options = new FirestoreRecyclerOptions.Builder<GetDataTraining>()
                .setQuery(query, GetDataTraining.class)
                .build();

        adapterCardInduction = new FirestoreRecyclerAdapter<GetDataTraining, ListTrainingHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ListTrainingHolder holder, int position, @NonNull GetDataTraining getDataTraining) {
                holder.setLokasi((getDataTraining.getLokasi()));
                holder.setTrainer(getDataTraining.getTrainer());
                holder.setKehadiran("Jumlah Peserta  : "+ getDataTraining.getKehadiran()+" Orang");
                holder.setDeskripsi(getDataTraining.getDeskripsi());
                holder.setTimedate(getDataTraining.getTimeDate());

                holder.setOnClickListener(new ListTrainingHolder.ClickListener() {
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
            public ListTrainingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
                return new ListTrainingHolder(view);
            }
        };
        adapterCardInduction.startListening();
        recyclerView.setAdapter(adapterCardInduction);

    }
    private void monthYear() {
        final Calendar today = Calendar.getInstance();
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ListTraining.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {

                        tgl.setText((selectedMonth +1)+ "/" +selectedYear);

                        Query query = FirebaseFirestore.getInstance()
                                .collection("training")
//                .orderBy("timeDate",Query.Direction.DESCENDING)
                                .whereEqualTo("filterBulan",tgl.getText());

                        FirestoreRecyclerOptions<GetDataTraining> options = new FirestoreRecyclerOptions.Builder<GetDataTraining>()
                                .setQuery(query, GetDataTraining.class)
                                .build();

                        adapterCardInduction = new FirestoreRecyclerAdapter<GetDataTraining, ListTrainingHolder>(options) {
                            @Override
                            protected void onBindViewHolder(@NonNull ListTrainingHolder holder, int position, @NonNull GetDataTraining getDataTraining) {
                                holder.setLokasi((getDataTraining.getLokasi()));
                                holder.setTrainer(getDataTraining.getTrainer());
                                holder.setKehadiran("Jumlah Peserta  : "+ getDataTraining.getKehadiran()+" Orang");
                                holder.setDeskripsi(getDataTraining.getDeskripsi());
                                holder.setTimedate(getDataTraining.getTimeDate());

                                holder.setOnClickListener(new ListTrainingHolder.ClickListener() {
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
                            public ListTrainingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_training, parent, false);
                                return new ListTrainingHolder(view);
                            }
                        };
                        adapterCardInduction.startListening();
                        recyclerView.setAdapter(adapterCardInduction);

                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(today.get(Calendar.MONTH))
                .setMinYear(1990)
                .setActivatedYear(today.get(Calendar.YEAR))
                .setMaxYear(2030)
                .setTitle("Select month year")
                .build().show();
    }

}