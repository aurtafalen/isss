package com.example.isss.SI;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.isss.R;
import com.google.android.material.snackbar.Snackbar;

public class InspeksiSelesai extends AppCompatActivity {

    Button selesai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_selesai);

        selesai = findViewById(R.id.btnselesai);
        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selesai = new Intent(InspeksiSelesai.this,Side_SI.class);
                startActivity(selesai);
                finish();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            Snackbar.make(findViewById(R.id.inspeksiselesai),"Inspeksi telah selesai anda tidak bisa kembali sesuka hati !",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}