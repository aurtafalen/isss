package com.example.isss.GTP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.isss.BRS.BRS;
import com.example.isss.MainActivity;
import com.example.isss.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.maps.plugin.Plugin;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class Home_Gtp extends AppCompatActivity implements
        OnMapReadyCallback, OnLocationClickListener, PermissionsListener, OnCameraTrackingChangedListener {

    //Code
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1111;

    //mapBox
    private MapboxMap mapboxMap;
    private MapView mapView;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;

    //scan
    private IntentIntegrator qrScan;

    //loading
    ProgressDialog progress;

    //menu
    LinearLayout berhenti, scan ,kegiatan,BRS;

    //firebase
    FirebaseFirestore dbs;
    DatabaseReference lokasionline;

    //String
    String idDocument,displayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getTokenMapbox
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_home_gtp);

        //string
        idDocument = getIntent().getStringExtra("idDoc");
        displayName = getIntent().getStringExtra("displayName");

        //firebase
        dbs = FirebaseFirestore.getInstance();

        //scan
        qrScan = new IntentIntegrator(this);

        //loading
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog


        if (Mapbox.getAccessToken().isEmpty()) {
            Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
            Log.d("tokenAcc", "isEmpty");
        } else {
            Log.d("tokenAcc", "noEmpty");
            mapView = (MapView) findViewById(R.id.mapBox);
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);

            berhenti = findViewById(R.id.mBerhenti);
            berhenti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog("Yakin berhenti patroli?");

                }
            });

            scan = findViewById(R.id.mScan);
            scan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    qrScan.initiateScan();
                }
            });

            kegiatan = findViewById(R.id.kegiatan);
            kegiatan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home_Gtp.this,Kegiatan.class);
                    intent.putExtra("idDoc", idDocument);
                    intent.putExtra("displayName",displayName);
                    startActivity(intent);

                }
            });

            BRS = findViewById(R.id.brs);
            BRS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home_Gtp.this, com.example.isss.BRS.BRS.class);
                    intent.putExtra("idDoc", idDocument);
                    intent.putExtra("displayName",displayName);
                    startActivity(intent);
                    finish();
                }
            });
        }

    }

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);

            }
        });

    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationComponent = mapboxMap.getLocationComponent();

            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .pulseEnabled(true)
                    .pulseColor(Color.RED)
                    .pulseAlpha(.3f)
                    .pulseInterpolator(new BounceInterpolator())
                    .build();


            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();


            locationComponent.activateLocationComponent(locationComponentActivationOptions);


            if (ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.zoomWhileTracking(16f);

            locationComponent.setRenderMode(RenderMode.COMPASS);

            locationComponent.addOnLocationClickListener(this);

            locationComponent.addOnCameraTrackingChangedListener(this);


        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    protected void onStart() {
        startLocationService();
        super.onStart();
        mapView.onStart();
        cekgps();
        isOnline();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);

                }
            });
        } else {
            finish();
        }
    }

    @Override
    public void onCameraTrackingDismissed() {

    }

    @Override
    public void onCameraTrackingChanged(int currentMode) {

    }

    @Override
    public void onLocationComponentClick() {

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Toast.makeText(Home_Gtp.this, "Tidak dapat kembali saat melakukan patroli!", Toast.LENGTH_SHORT).show();
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Log.d("dicari", "startLocationService()");
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            intent.putExtra("idDoc", idDocument);
            startService(intent);

        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
        }
    }

    private void cekgps() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PERINGATAN")
                .setMessage("GPS anda mati !")
                .setCancelable(false)
                .setPositiveButton("Hidupkan", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Snackbar.make(findViewById(R.id.homeGTP), "Internet Bermasalah!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            }

        } else {
            Toast.makeText(this, "PERMISSION DENIED !", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialog(String message) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progress.show();
                //date firebase
                Timestamp fwaktu = Timestamp.now();
                dbs.collection("gtp")
                        .document("data_checkpoint")
                        .collection("checkpoint")
                        .document(idDocument)
                        .update("waktuKeluar", fwaktu)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progress.dismiss();
                                    lokasionline = FirebaseDatabase.getInstance().getReference("lokasionline")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    lokasionline.getRef().removeValue();
                                    Intent i = new Intent(Home_Gtp.this, MainActivity.class);
                                    stopLocationService();
                                    startActivity(i);
                                    finishAffinity();

                                } else {
                                    progress.dismiss();
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                dialogInterface.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.show();
    }

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
//                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
//                    //setting values to textviews


                    String id = (obj.getString("id"));
                    String cekPointLat = (obj.getString("checkpointLat"));
                    String cekPointLng = (obj.getString("checkpointLng"));
                    String lokasi = (obj.getString("location"));
                    String area = (obj.getString("area"));

                    Log.d("hasildistance", " :tidak Jauh");
                    Intent qr = new Intent(Home_Gtp.this,Scan.class);
                    qr.putExtra("id",id);
                    qr.putExtra("checkpointLat",cekPointLat);
                    qr.putExtra("checkpointLng",cekPointLng);
                    qr.putExtra("location",lokasi);
                    qr.putExtra("area",area);
                    qr.putExtra("idDoc",idDocument);
                    qr.putExtra("displayName",displayName);
                    startActivity(qr);

                } catch (JSONException e) {
//                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(Home_Gtp.this, "Gagal,Lakukan scan kembali!", Toast.LENGTH_SHORT).show();
//                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}