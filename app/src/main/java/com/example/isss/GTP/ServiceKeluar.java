package com.example.isss.GTP;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class ServiceKeluar extends Service {
    DatabaseReference lokasionline, token;
    public ServiceKeluar() {
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopLocationService();

//        token = FirebaseDatabase.getInstance().getReference("Tokens")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        token.getRef().removeValue();

        lokasionline = FirebaseDatabase.getInstance().getReference("lokasionline")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        lokasionline.getRef().removeValue();

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void stopLocationService(){
            Intent intent = new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
        }

}