package com.example.isss;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.helper.widget.Carousel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity {
    CarouselView news;
            int[] sampleImages = {R.drawable.slide8, R.drawable.slide6, R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};

            CardView ht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = findViewById(R.id.news_carousel);
        news.setPageCount(sampleImages.length);
        news.setImageListener(imageListener);

        ht = findViewById(R.id.btn_ht);
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