package com.example.isss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HandlyTalky extends AppCompatActivity {
    CardView mic;
    ImageView icon_mic;
    TextView txt_mic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handly_talky);

        icon_mic = findViewById(R.id.icon_mic);
        mic = findViewById(R.id.btn_mic);
        txt_mic = findViewById(R.id.txt_mic);

        mic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        icon_mic.setColorFilter(Color.RED);
                        txt_mic.setTextColor(Color.RED);
                        break;

                    case MotionEvent.ACTION_UP:
                        icon_mic.setColorFilter(Color.WHITE);
                        txt_mic.setTextColor(Color.WHITE);
                        break;
                }
                return true;
            }
        });

    }
}