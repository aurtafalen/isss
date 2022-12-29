package com.example.isss.BRS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.isss.GTP.CustomAdapter;
import com.example.isss.GTP.Home_Gtp;
import com.example.isss.GTP.ModalClass;
import com.example.isss.MainActivity;
import com.example.isss.R;
import com.example.isss.SendNotificationPack.APIService;
import com.example.isss.SendNotificationPack.Client;
import com.example.isss.SendNotificationPack.Data;
import com.example.isss.SendNotificationPack.MyResponse;
import com.example.isss.SendNotificationPack.NotificationSender;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BRS extends AppCompatActivity {

    //kategori
    Spinner kategori,subKategori;
    String[] aKategori,aSubKategoriKriminal,aSubKategoriSafety;
    ArrayAdapter kategoriAdapter,subKategoriAdapter;
    LinearLayout lSubKategori,lWaktu;

    //atribut
    EditText eLokasi,eDeskripsi;
    Button bKirim;
    TextView vDate,vTime;
    //firebase
    FirebaseFirestore dbs;

    //date firebase
    Timestamp fwaktu = Timestamp.now();

    //String
    String displayName,idDocument;

    //loading
    ProgressDialog progress;

    //currentLocation
    FusedLocationProviderClient fusedLocationClient;

    //gfoto
    GridLayout gFoto;

    //foto
    RecyclerView rFoto;

    //PushNotif
    private APIService apiService;
    final private String title = "Batamindo Reporting System";
    ArrayList<String> uidGtp = new ArrayList<>();
    ArrayList<String> uidSi = new ArrayList<>();

    //CameraCode
    private static final int PICK_FROM_GALLERY = 107;
    private static final int IMAGE_CODE = 1;

    //array
    ArrayList listImage = new ArrayList<>();
    ArrayList effects = new ArrayList<>();

    private StorageReference mStorageReference;
    List<ModalClass> modalClassList;
    CustomAdapter customAdapter;
    StorageReference storageReference;

    // Get the current date and time
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    //checkbox
    CheckBox Disruption,Property,Environmental,iFirst,iRestricted,iLost,Fatality;

    //firebaseTimeStamp
    Timestamp ts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brs);

        //notif
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        //checkbox
        Disruption = findViewById(R.id.Disruption);
        Property = findViewById(R.id.Property);
        Environmental = findViewById(R.id.Environmental);
        iFirst = findViewById(R.id.iFirst);
        iRestricted = findViewById(R.id.iRestricted);
        iLost = findViewById(R.id.iLost);
        Fatality = findViewById(R.id.Fatality);


        //waktu
        lWaktu = findViewById(R.id.lTimeIncident);
        lWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePicker = new DatePickerDialog(BRS.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        // Set the selected date in the TextView
                        vDate = findViewById(R.id.vDate);
                        vDate.setText(String.format("%d/%d/%d", day, month+1, year));
                    }
                }, year, month, day);

                datePicker.show();

                TimePickerDialog timePicker = new TimePickerDialog(BRS.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        // Set the selected time in the TextView
                        vTime = findViewById(R.id.vTime);
                        vTime.setText(String.format(" %d:%d", hour, minute));
                    }
                }, hour, minute, true);

                timePicker.show();
            }
        });

        //loading
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        //firebase
        dbs = FirebaseFirestore.getInstance();

        //string
        idDocument = getIntent().getStringExtra("idDoc");
        displayName = getIntent().getStringExtra("displayName");

        //foto
        rFoto = findViewById(R.id.recyclerFoto);
        modalClassList = new ArrayList<>();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("BRS");
        storageReference = FirebaseStorage.getInstance().getReference();

        //gfoto
        gFoto = findViewById(R.id.gFoto);
        gFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotoGallery();
            }
        });

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

        eLokasi = findViewById(R.id.eLocation);
        eDeskripsi = findViewById(R.id.eDesc);
        bKirim = findViewById(R.id.btn_kirim);
        bKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getLokasi = eLokasi.getText().toString();
                String getDeskripsi = eDeskripsi.getText().toString();

                if (getLokasi.isEmpty()&& getDeskripsi.isEmpty()&& listImage.isEmpty()) {
                    eLokasi.setError("Tidak boleh kosong !");
                    eDeskripsi.setError("Tidak boleh kosong !");
                    Toast.makeText(getApplicationContext(), "Foto Tidak Boleh kosong !", Toast.LENGTH_LONG).show();

                }
                else if (getLokasi.isEmpty()) {
                    eLokasi.setError("Tidak boleh kosong !");
                } else if (getDeskripsi.isEmpty()) {
                    eDeskripsi.setError("Tidak boleh kosong !");
                } else if (listImage.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Foto Tidak Boleh kosong !", Toast.LENGTH_LONG).show();
                } else {
                    progress.show();

                    //getTime
                    String txtDate = vDate.getText().toString();
                    String txtTime = vTime.getText().toString();
                    String dateString = txtDate+txtTime;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    try {
                        // Parse the string into a date object
                        Date date = dateFormat.parse(dateString);

                        // Convert the date object to a timestamp
                       long timestamp = date.getTime();

                        // Use the timestamp with Firebase
                        ts = new Timestamp(date);
                        Log.d("getDateString",String.valueOf(date)+" dateString : "+txtDate);


                    } catch (ParseException e) {
                        // Handle the error
                        // ...
                    }

                    //getEffects
                    if (Disruption.isChecked()){
                        effects.add(Disruption.getText());
                    }
                    if (Property.isChecked()){
                        effects.add(Property.getText());
                    }
                    if (Environmental.isChecked()){
                        effects.add(Environmental.getText());
                    }
                    if (iFirst.isChecked()){
                        effects.add(iFirst.getText());
                    }
                    if (iRestricted.isChecked()){
                        effects.add(iRestricted.getText());
                    }
                    if (iLost.isChecked()){
                        effects.add(iLost.getText());
                    }
                    if (Fatality.isChecked()){
                        effects.add(Fatality.getText());
                    }

                    //currentLocation
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }

                    fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            double CurrentLat = location.getLatitude();
                            double CurrentLng = location.getLongitude();

                            SendDataBRS sendDataBRS = new SendDataBRS(
                                    eDeskripsi.getText().toString(),
                                    listImage,
                                    fwaktu,
                                    displayName,
                                    eLokasi.getText().toString(),
                                    CurrentLat,
                                    CurrentLng,
                                    kategori.getSelectedItem().toString(),
                                    subKategori.getSelectedItem().toString(),
                                    effects,
                                    ts

                            );

                            dbs.collection("incident_reports").document().set(sendDataBRS)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progress.dismiss();

                                                if (idDocument == null){
//                                                    //notif
//                                                    uidGtp.add("yhksngpmjDezajBOywyKwd453372");
//                                                    uidGtp.add("Y5AKtCuyZMbyU49JCimzSWG9ljF3");
//                                                    uidGtp.add("DB9CarrbKRWZYNcbVomIxZ8wxj13");
//                                                    uidGtp.add("rktGLNBP8bfIq7fgs5vEXxHuePp2");
//                                                    uidGtp.add("UVwCtvOQ4cNcreAJN1cd3XUF1c13");
//                                                    uidGtp.add("TMXl1jCAomecyLBMj7xw5P6eIy12");
//                                                    uidGtp.add("DT3gSlK8psZ4ctKNFSpqpGcgz4O2");
//                                                    uidGtp.add("RSFqB02IxPP2Gb4kXHWVHddn5hG3");

                                                    //testAurtaAli
                                                    uidGtp.add("xinVZDlpN2WzpTEkfRVEK5t9Xgh2");
                                                    uidGtp.add("lOu0IOzf6gWEK4YAV2fLlPSRZtm1");

                                                    for (int i = 0; i < uidGtp.size(); i++) {
                                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(String.valueOf(i)).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Log.d("pushNotifCheck", "yes");
                                                                String usertoken = dataSnapshot.getValue(String.class);
                                                                sendNotifications(usertoken, title.toString().trim(), "Telah terjadi "+subKategori.getSelectedItem().toString()+" di lokasi "+eLokasi.getText().toString().trim());
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                    Toast.makeText(getApplicationContext(), "Laporan berhasil masuk!", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(BRS.this, MainActivity.class);
                                                    i.putExtra("idDoc", idDocument);
                                                    i.putExtra("displayName",displayName);
                                                    startActivity(i);
                                                    finish();

                                                }else{
                                                    Toast.makeText(getApplicationContext(), "Laporan berhasil masuk!", Toast.LENGTH_LONG).show();
                                                    Intent i = new Intent(BRS.this, Home_Gtp.class);
                                                    i.putExtra("idDoc", idDocument);
                                                    i.putExtra("displayName",displayName);
                                                    startActivity(i);
                                                    finish();
                                                }

                                            } else {
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
                }

            }
        });
    }

    private void fotoGallery() {
        if (checkSelfPermission( Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(intent,IMAGE_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                Log.d("dataFoto", "multiple");

                rFoto.removeAllViews();
                rFoto.setHasFixedSize(true);
                rFoto.setLayoutManager(new LinearLayoutManager(this));

                listImage = new ArrayList<>();

                int totalitem = data.getClipData().getItemCount();
                Log.d("totalFoto", String.valueOf(totalitem));

                for (int i = 0; i < totalitem; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imagename = getFileName(imageUri);
                    ModalClass modalClass = new ModalClass(imagename, imageUri);

                    modalClassList.add(modalClass);

                    customAdapter = new CustomAdapter(BRS.this, modalClassList);
                    rFoto.setAdapter(customAdapter);

                    StorageReference mRef = mStorageReference.child(imagename);
                    mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(Induction.this, "Done", Toast.LENGTH_SHORT).show();
                            listImage.add(imagename);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(BRS.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }else if (data.getData() != null) {

                Log.d("dataFoto", "single");
                rFoto.removeAllViews();
                rFoto.setHasFixedSize(true);
                rFoto.setLayoutManager(new LinearLayoutManager(this));

                listImage = new ArrayList<>();

                Uri imageUri = data.getData();
                listImage.add(imageUri);
                String imagename = getFileName(imageUri);
                ModalClass modalClass = new ModalClass(imagename, imageUri);

                modalClassList.add(modalClass);

                customAdapter = new CustomAdapter(BRS.this, modalClassList);
                rFoto.setAdapter(customAdapter);

                StorageReference mRef = mStorageReference.child(imagename);
                mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        listImage.add(imagename);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BRS.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
//                        Toast.makeText(Start.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
}