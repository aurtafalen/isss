package com.example.isss;

import static android.content.ContentValues.TAG;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Carousel;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isss.BRS.BRS;
import com.example.isss.GTP.Admin_Home_Gtp;
import com.example.isss.GTP.Home_Gtp;
import com.example.isss.GTP.LocationService;
import com.example.isss.GTP.SendDataPatroli;
import com.example.isss.SI.Side_SI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    CarouselView news;
    int[] sampleImages = {R.drawable.slide8, R.drawable.slide6, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

    //
    CardView ht,gtp,safety,brs,cctv;
    TextView displayName;
    ImageView person;

    //Firebase
    DatabaseReference token,lokasionline;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DocumentReference docRef;
    List<String> accessPage;
    FirebaseUser firebaseUser;

    //loading
    ProgressDialog progress;

    //dateformat
    SimpleDateFormat dateFormat;

    //String
    String dName,dataku;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //loading
        progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        //firebase
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        //getDataAccount
        docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (task.isComplete()){
                    dName = String.valueOf(documentSnapshot.get("displayName"));
                    accessPage = (List<String>) documentSnapshot.get("access_page");
                    displayName = findViewById(R.id.txt_username);
                    displayName.setText("Hi, "+dName+"!");
                    progress.dismiss();
                }else{
                    Log.d("taskStatus", "noComplate");
                }


            }
        });

        //checkDataAccount
        lokasionline = FirebaseDatabase.getInstance().getReference("lokasionline")
                .child(firebaseUser.getUid()).getRef();
        lokasionline.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                GetIdAda getIdAda = snapshot.getValue(GetIdAda.class);

                if (getIdAda != null) {
                    dataku =getIdAda.getId();
                    Intent intent = new Intent(MainActivity.this, Home_Gtp.class);
                    intent.putExtra("idDoc", dataku);
                    startActivity(intent);
                    onBackPressed();
                    finish();
                    progress.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Internet bermasalah!", Toast.LENGTH_SHORT).show();
            }

        });

        news = findViewById(R.id.news_carousel);
        news.setPageCount(sampleImages.length);
        news.setImageListener(imageListener);

        person = findViewById(R.id.img_person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //popup menu
                final PopupMenu popupMenu = new PopupMenu(MainActivity.this, person);
                //add menu items in popup menu
                popupMenu.getMenu().add(Menu.NONE, 0, 0, "Logout");

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        //handle clicks
                        if (id == 0) {
                            token = FirebaseDatabase.getInstance().getReference("Tokens")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            token.getRef().removeValue();

                            FirebaseAuth.getInstance().signOut();
                            Intent i = new Intent(MainActivity.this, Login.class);
                            startActivity(i);
                            finishAffinity();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        gtp = findViewById(R.id.card_gtp);
        gtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validasiArray True
                boolean hasAccessAdminGtp = accessPage.contains("gtp_admin");
                boolean hasAccessGtp = accessPage.contains("gtp");
                Log.d(TAG, "hasAccessGtp : "+hasAccessGtp);
                if (hasAccessAdminGtp){
                    Intent intent = new Intent(MainActivity.this, Admin_Home_Gtp.class);
                    startActivity(intent);
                }else if (hasAccessGtp){
                        //atribut
                        dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
                        String iddoc = dateFormat.format(Calendar.getInstance().getTime());
                        Timestamp fwaktu = Timestamp.now();
                        String judul = "Patroli Rutin";

                        //sendData
                        SendDataPatroli sendDataPatroli = new SendDataPatroli(fwaktu, dName, judul);
                        db.collection("gtp")
                                .document("data_checkpoint")
                                .collection("checkpoint")
                                .document(iddoc)
                                .set(sendDataPatroli)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            Intent intent = new Intent(MainActivity.this, Home_Gtp.class);
                                            intent.putExtra("idDoc",iddoc);
                                            intent.putExtra("displayName",dName);
                                            startActivity(intent);
                                            finish();
                                            progress.dismiss();

                                        }
                                        else
                                        {
                                            progress.dismiss();
                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progress.dismiss();
                                Toast.makeText(MainActivity.this, "Internet Bermasalah!", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }else{
                        Toast.makeText(MainActivity.this, "Anda tidak memiliki akses !", Toast.LENGTH_SHORT).show();
                    }
                }

        });
        safety = findViewById(R.id.card_safety);
        safety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //validasiArray True
                boolean hasAccessSi = accessPage.contains("si");

                Log.d(TAG, "hasAccessGtp : "+hasAccessSi);
                if (hasAccessSi){
                    Intent intent = new Intent(MainActivity.this, Side_SI.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, "Anda tidak memiliki akses untuk menu safety!", Toast.LENGTH_SHORT).show();
                }
            }

        });

        ht = findViewById(R.id.card_ht);
        ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,HandlyTalky.class);
//                startActivity(intent);
                Toast.makeText(MainActivity.this, "Menu belum tersedia!", Toast.LENGTH_SHORT).show();
            }
        });

        brs = findViewById(R.id.card_brs);
        brs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BRS.class);
                startActivity(intent);
            }
        });

        cctv = findViewById(R.id.card_cctv);
        cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Menu belum tersedia!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
            imageView.setColorFilter(Color.LTGRAY,PorterDuff.Mode.MULTIPLY);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }



}