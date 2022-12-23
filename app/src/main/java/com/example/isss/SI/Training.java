package com.example.isss.SI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.isss.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Training extends AppCompatActivity {

    TextInputEditText lokasi,trainer,deskripsi,kehadiran;
    Button selesai;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        //loading
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Memproses Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        lokasi = (TextInputEditText) findViewById(R.id.lokasiTraining);
        trainer = (TextInputEditText) findViewById(R.id.trainer);
        deskripsi = (TextInputEditText) findViewById(R.id.deskripsi);
        kehadiran = (TextInputEditText) findViewById(R.id.kehadiranTraining);
        selesai = (Button) findViewById(R.id.selesaiTraining);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLokasi = lokasi.getText().toString();
                String getTrainer = trainer.getText().toString();
                String getDeskripsi = deskripsi.getText().toString();
                String getkehadiran = kehadiran.getText().toString();

                if (getLokasi.isEmpty() && getTrainer.isEmpty() && getDeskripsi.isEmpty() && getkehadiran.isEmpty()){
                    lokasi.setError("Tidak boleh kosong !");
                    trainer.setError("Tidak boleh kosong !");
                    deskripsi.setError("Tidak boleh kosong !");
                    kehadiran.setError("Tidak boleh kosong !");

                    Toast.makeText(Training.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();
                }else if (getLokasi.isEmpty()){
                    lokasi.setError("Tidak boleh kosong !");
                    Toast.makeText(Training.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();
                }else if (getTrainer.isEmpty()){
                    trainer.setError("Tidak boleh kosong !");
                    Toast.makeText(Training.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getDeskripsi.isEmpty()){
                    deskripsi.setError("Tidak boleh kosong !");
                    Toast.makeText(Training.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                } else if (getkehadiran.isEmpty()) {
                    kehadiran.setError("Tidak boleh kosong !");
                    Toast.makeText(Training.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else {
                    progress.show();
                    String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
                    Map<String, Object> isiTraining = new HashMap<>();
                    isiTraining.put("lokasi",getLokasi);
                    isiTraining.put("trainer",getTrainer);
                    isiTraining.put("deskripsi",getDeskripsi);
                    isiTraining.put("kehadiran",getkehadiran);
                    isiTraining.put("timeDate", FieldValue.serverTimestamp());
                    isiTraining.put("filterBulan",dateNow);

                    //firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //collection templates
                    CollectionReference push = db.collection("si").document("data_training").collection("training");

                    push.document()
                            .set(isiTraining).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progress.dismiss();
                            Toast.makeText(Training.this, "Data Berhasil Ditambah !", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Training.this,Side_SI.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                }
            }
        });
    }
}