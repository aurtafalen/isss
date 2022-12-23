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

public class Toolbox extends AppCompatActivity {
    ProgressDialog progress;

    TextInputEditText lokasi,topik,kehadiran,input,tanggapan,targetwaktu,status;
    Button selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbox);

        //loading
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Memproses Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        lokasi = (TextInputEditText) findViewById(R.id.lokasiToolbox);
        topik = (TextInputEditText) findViewById(R.id.topik);
        kehadiran = (TextInputEditText) findViewById(R.id.kehadiranToolbox);
        input = (TextInputEditText) findViewById(R.id.input);
        tanggapan = (TextInputEditText) findViewById(R.id.tanggapan);
        targetwaktu = (TextInputEditText) findViewById(R.id.target);
        status = (TextInputEditText) findViewById(R.id.statusToolbox);
        selesai = (Button) findViewById(R.id.selesaiToolbox);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLokasi = lokasi.getText().toString();
                String getTopik = topik.getText().toString();
                String getKehadiran = kehadiran.getText().toString();
                String getInput = input.getText().toString();
                String getTanggapan = tanggapan.getText().toString();
                String getTarget = targetwaktu.getText().toString();
                String getStatus = status.getText().toString();

                if (getLokasi.isEmpty() && getTopik.isEmpty() && getKehadiran.isEmpty() && getInput.isEmpty() && getTanggapan.isEmpty() && getTarget.isEmpty() && getStatus.isEmpty()){
                    lokasi.setError("Tidak boleh kosong !");
                    topik.setError("Tidak boleh kosong !");
                    kehadiran.setError("Tidak boleh kosong !");
                    input.setError("Tidak boleh kosong !");
                    tanggapan.setError("Tidak boleh kosong !");
                    targetwaktu.setError("Tidak boleh kosong !");
                    status.setError("Tidak boleh kosong !");

                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getLokasi.isEmpty()){
                    lokasi.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getTopik.isEmpty()){
                    topik.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getKehadiran.isEmpty()){
                    kehadiran.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getInput.isEmpty()){
                    input.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getTanggapan.isEmpty()){
                    tanggapan.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getTarget.isEmpty()){
                    targetwaktu.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getStatus.isEmpty()){
                    status.setError("Tidak boleh kosong !");
                    Toast.makeText(Toolbox.this, "Data tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else {
                    progress.show();
                    String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
                    Map<String, Object> isiToolbox = new HashMap<>();
                    isiToolbox.put("lokasi",getLokasi);
                    isiToolbox.put("topik",getTopik);
                    isiToolbox.put("kehadiran",getKehadiran);
                    isiToolbox.put("input",getInput);
                    isiToolbox.put("tanggapan",getTanggapan);
                    isiToolbox.put("targetWaktu",getTarget);
                    isiToolbox.put("status",getStatus);
                    isiToolbox.put("timeDate", FieldValue.serverTimestamp());
                    isiToolbox.put("filterBulan",dateNow);

                    //firestore
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //collection templates
                    CollectionReference push = db.collection("si").document("data_toolBox").collection("toolBox");

                    push.document()
                            .set(isiToolbox).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progress.dismiss();
                            Toast.makeText(Toolbox.this, "Data Berhasil Ditambah !", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Toolbox.this,Side_SI.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            }
        });

    }
}