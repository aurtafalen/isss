package com.example.isss;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Carousel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isss.GTP.Home_Gtp;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    CarouselView news;
    int[] sampleImages = {R.drawable.slide8, R.drawable.slide6, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

    CardView ht,gtp,safety;
    TextView displayName;
    ImageView person;
    DatabaseReference token;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DocumentReference docRef;
    List<String> accessPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //getDataAccount
        docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                accessPage = (List<String>) documentSnapshot.get("access_page");
                displayName = findViewById(R.id.txt_username);
                displayName.setText("Hi, "+documentSnapshot.get("displayName")+"!");

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
                            onBackPressed();
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
               boolean hasAccessGtp = accessPage.contains("gtp");

                Log.d(TAG, "hasAccessGtp : "+hasAccessGtp);
                    if (hasAccessGtp){
                        Intent intent = new Intent(MainActivity.this, Home_Gtp.class);
                        startActivity(intent);
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
                    Toast.makeText(MainActivity.this, "Yes", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Anda tidak memiliki akses !", Toast.LENGTH_SHORT).show();
                }
            }

        });

        ht = findViewById(R.id.card_ht);
        ht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HandlyTalky.class);
                startActivity(intent);
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
}