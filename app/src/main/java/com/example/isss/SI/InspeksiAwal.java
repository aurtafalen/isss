package com.example.isss.SI;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ajts.androidmads.fontutils.FontUtils;

import com.example.isss.R;
import com.example.isss.SendNotificationPack.APIService;
import com.example.isss.SendNotificationPack.Client;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InspeksiAwal extends AppCompatActivity {

    Button kemali, lanjutkan;
    TextView tambah1, tambah2, tambah3, foto1, foto2, foto3, atindakan1, atindakan2, atindakan3, tglview, lokasi ,alamat,titleInspegsi;
    EditText alamatEdit;

    //getlocation plus adress
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    //camera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    //notif
    private APIService apiService;
    final private String admin1 = "4kCznhvJW5aZfz4hkBGme3ZvV1r2";
    final private String title = "!TEMUAN!";
    final private String pesan = "!RESIKO HIGHT!";


    //tindakan
    FloatingActionButton fab;
    Toolbar toolbar;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;


    //Cuaca
    public static String BaseUrl = "https://api.openweathermap.org/";
    public static String AppId = "df844c5fe47723cce56c4601631f9b64";
    public static String lat = "1.0684909356188659";
    public static String lon = "104.02529037437219";
    public static String metric = "metric";
    private TextView weatherData;
    private ResultReceiver resultReceiver;

    //SpinerTeam
//    Spinner spinner;
    TextView spinner;

    //cloudfirebase
    FirebaseFirestore dbs = FirebaseFirestore.getInstance();

    //String
    String alm;
    String idtemplate;
    String documentId;

    private int waktu_loading = 10000;
    ProgressDialog progress;


    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_awal);

        //alamatedit
        alamatEdit = findViewById(R.id.alamatEdit);

        //loading
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Memproses Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

//        //loading
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progress.dismiss();
//            }
//        },waktu_loading);


        //putextra
        documentId = getIntent().getStringExtra("doc");

        //gps
        resultReceiver = new AddressResultReceiver(new Handler());

        //tgl & jam
        String tgl = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        String month = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());

//        //filterTeam
//        spinner = (Spinner) findViewById(R.id.filterteam);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.filterteam, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.filterteam);
        spinner.setText(mAuth.getCurrentUser().getEmail());

//        spinner.getBackground().setColorFilter(Color.parseColor("#C0C2D1"), PorterDuff.Mode.SRC_ATOP);

        //getlocation plus adress
        findViewById(R.id.btnlokasi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(InspeksiAwal.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    getlokasi();
                }
            }
        });

        //notifservice
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        lanjutkan = findViewById(R.id.btnnext);
        tglview = findViewById(R.id.tglView);
        lokasi = findViewById(R.id.hasilLokasi);
        alamat = findViewById(R.id.alamat);
        titleInspegsi = findViewById(R.id.titleInspeksi);

        //settgl & setjam
        tglview.setText(tgl);

        //cuaca
        weatherData = findViewById(R.id.cuaca);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Lato-Bold.ttf");
        FontUtils fontUtils = new FontUtils();
        fontUtils.applyFontToView(weatherData, typeface);
        getCurrentData();



//        String title = titleInspegsi.getText().toString();
//        if (title.isEmpty()){
            setTtitle();
//            return;
//        }

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String alamat_Edit = alamatEdit.getText().toString();
                    String lok = lokasi.getText().toString().trim();
//                    String spinertim = spinner.getSelectedItem().toString().trim();
                    String spinertim = spinner.getText().toString();
                    String pilih = "Pilih Team";
                    String suhu = weatherData.getText().toString();
                    FieldValue itgl = FieldValue.serverTimestamp();
                    String alamt = alamat.getText().toString();

                    if (lok.isEmpty()&& spinertim.equals(pilih)){
                        Snackbar.make(findViewById(R.id.inspeksiawal),"Lokasi & Team Tidak Boleh Kosong",Snackbar.LENGTH_LONG).show();
                    }
                    else if (lok.isEmpty()) {
                              Snackbar.make(findViewById(R.id.inspeksiawal),"Lokasi Tidak Boleh Kosong",Snackbar.LENGTH_LONG).show();
                    }
                    else if (alamat_Edit.isEmpty()){
                             alamatEdit.setError("Tidak boleh kosong !");
                             Snackbar.make(findViewById(R.id.inspeksiawal),"Alamat Tidak Boleh Kosong",Snackbar.LENGTH_LONG).show();
                    }
                    else{


                        dbs = FirebaseFirestore.getInstance();
                        dbs.collection("si").document("data_inspections").collection("inspections").document(documentId)
                                .update("templateLocation",lok,
                                        "templateTeam",spinertim,
                                        "templateTemperature",suhu,
                                        "templateDate",itgl,
                                        "templateMonth",month,
                                        "templateAddress",alamt,
                                        "templateSecondAddress",alamatEdit.getText().toString())

                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            //pushtotemplates
                                            dbs.collection("si").document("data_inspections").collection("inspections").document(documentId).get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()){
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Log.d("cekdata", document.toString());

                                                        }
                                                    }

                                                }
                                            });


                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                        dbs.collection("si").document("data_inspections").collection("inspections").document(documentId)
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                Intent lanjut = new Intent(InspeksiAwal.this, InspeksiKetiga.class);
                                lanjut.putExtra("doc", documentId);
                                lanjut.putExtra("idtem", idtemplate);
                                lanjut.putExtra("alamat",alamt);
                                startActivity(lanjut);
                                finish();
                            }
                        });

                    }

            }
        });

    }


    private void setTtitle() {

        //gettile
        dbs.collection("si").document("data_inspections").collection("inspections").document(documentId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()) {

                    String title = (String) task.getResult().get("templateTitle");
                    if (title == null){
                        Log.d("inigagal","yes");
                        setTtitle();
                    }else{
                        titleInspegsi.setText(title);
                        progress.dismiss();
                    }

                }
            }

        });
    }

    private void getlokasi() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(InspeksiAwal.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(InspeksiAwal.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0){
                            int latesLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latesLocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latesLocationIndex).getLongitude();

                            lokasi.setText(String.format(
                                    "(%s,%s)",
                                    latitude,
                                    longitude
                            ));
                        Location location = new Location("providerNA");
                        location.setLatitude(latitude);
                        location.setLongitude(longitude);
                        fetcAddressFromLatLong(location);
                        }
                    }
                }, Looper.getMainLooper());


    }
    private void fetcAddressFromLatLong(Location location){
        Intent intent = new Intent(this,AddressService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        startService(intent);
    }
    private class AddressResultReceiver extends ResultReceiver{
         AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCCESS_RESULT){
                alamat.setText(resultData.getString(Constants.RESULT_DATA_KEY));

            }else{
                Toast.makeText(InspeksiAwal.this,resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

    void tambahcatatan() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspeksiAwal.this);
        alertDialog.setTitle("Tambah Catatan");

        final EditText input = new EditText(InspeksiAwal.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("Tambah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void ambilfoto() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults [0] == PackageManager.PERMISSION_GRANTED){
                getlokasi();
            }else{
                Toast.makeText(this, "GPS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void getCurrentData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        WeatherService service = retrofit.create(WeatherService.class);
        Call<WeatherResponse> call = service.getCurrentWeatherData(lat, lon, metric, AppId);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<WeatherResponse> call, @NonNull Response<WeatherResponse> response) {
                if (response.code() == 200) {
                    WeatherResponse weatherResponse = response.body();
                    assert weatherResponse != null;

                    String stringBuilder =
//                            "Country: " +
//                            weatherResponse.sys.country +
//                            "\n" +
                            weatherResponse.main.temp + " °C" ;
//                                    +
//                            "\n" +
//                            "Temperature(Min): " +
//                            weatherResponse.main.temp_min +
//                            "\n" +
//                            "Temperature(Max): " +
//                            weatherResponse.main.temp_max +
//                            "\n" +
//                            "Humidity: " +
//                            weatherResponse.main.humidity +
//                            "\n" +
//                            "Pressure: " +
//                            weatherResponse.main.pressure;

                    weatherData.setText(stringBuilder);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherResponse> call, @NonNull Throwable t) {
                weatherData.setText(t.getMessage());
            }
        });
    }

}