package com.example.isss.BRS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.isss.R;

import java.util.ArrayList;

public class BRS extends AppCompatActivity {

    //kategori
    Spinner kategori,subKategori;
    String[] aKategori,aSubKategoriKriminal,aSubKategoriSafety;
    ArrayAdapter kategoriAdapter,subKategoriAdapter;
    LinearLayout lSubKategori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brs);

        //lsubKategori
        lSubKategori = findViewById(R.id.lSubKategori);
        lSubKategori.setVisibility(View.GONE);

        //kategori
        kategori = findViewById(R.id.sKategori);
        aKategori = new String[]{"Kriminal", "Safety", "Pilih Kategori.."};
        kategoriAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, aKategori){
            @Override
            public int getCount() {
                return aKategori.length-1;
            }
        };
        kategoriAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        kategori.setAdapter(kategoriAdapter);
        kategori.setSelection(aKategori.length-1);

        //subKategori
        subKategori = findViewById(R.id.sSubKategori);
        aSubKategoriKriminal = new String[]{"Pencurian","Pelanggaran Asusila","Penjambretan","Penganiayaan","Pemerkosaan","Lainnya","Pilih SubKategori.."};
        aSubKategoriSafety = new String[]{"Kecelakaan Lalulintas","Property Damage","Work Place","Near Miss","Traffic","Lainnya","Pilih SubKategori.."};

        kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    lSubKategori.setVisibility(View.VISIBLE);
                    subKategoriAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, aSubKategoriKriminal){
                        @Override
                        public int getCount() {
                            return aSubKategoriKriminal.length-1;
                        }
                    };
                    subKategoriAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    subKategori.setAdapter(subKategoriAdapter);
                    subKategori.setSelection(aSubKategoriKriminal.length-1);
                }
                if (position == 1){
                    lSubKategori.setVisibility(View.VISIBLE);
                    subKategoriAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, aSubKategoriSafety){
                        @Override
                        public int getCount() {
                            return aSubKategoriSafety.length-1;
                        }
                    };
                    subKategoriAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    subKategori.setAdapter(subKategoriAdapter);
                    subKategori.setSelection(aSubKategoriSafety.length-1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}