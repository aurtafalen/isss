package com.example.isss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;


public class Login extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS = 20;
    EditText user,pas;
    Button masuk;
    FirebaseFirestore db,dbs;
    FirebaseAuth mAuth;
    ProgressDialog progress;
    DatabaseReference liattoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //login
        user = findViewById(R.id.eUser);
        pas = findViewById(R.id.ePassword);
        masuk = findViewById(R.id.masuk);

        //DB
        dbs = FirebaseFirestore.getInstance();
        db= FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        //loading
        progress = new ProgressDialog(this);
//        progress.setTitle("Loading");
        progress.setMessage("Loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        if (FirebaseAuth.getInstance().getCurrentUser()!= null) {
            goToDashboard();
            finish();
        } else {
            masuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String username = user.getText().toString().trim();
                    String password = pas.getText().toString().trim();
                    if (username.isEmpty()&& password.isEmpty()) {
//                   Snackbar.make(findViewById(R.id.login),"Username & Password tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                        user.setError("Tidak boleh kosong !");
                        pas.setError("Tidak boleh kosong !");
                    } else if (username.isEmpty()) {
//                    Snackbar.make(findViewById(R.id.login),"Username tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                        user.setError("Tidak boleh kosong !");
                    } else if (password.isEmpty()) {
//                    Snackbar.make(findViewById(R.id.login),"Password tidak boleh kosong", Snackbar.LENGTH_LONG).show();
                        pas.setError("Tidak boleh kosong !");
                    } else if (!(username.isEmpty() && password.isEmpty())) {
                        showProgress();
                        progress.show();


                        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                hideProgress();

                                if (task.isSuccessful()) {

                                    cekrulelogin();

                                }
                                else{
                                    progress.dismiss();
                                    Snackbar.make(findViewById(R.id.login),"Username atau Password salah !",Snackbar.LENGTH_INDEFINITE)
                                            .setAction("OK", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                }
                                            }).show();

                                }

                            }
                        });


                    }
                }
            });
        }
    }

    private void ceklogin(){

        liattoken = FirebaseDatabase.getInstance().getReference("Tokens")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        liattoken.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GetTokenId gettokenid = snapshot.getValue(GetTokenId.class);
//
                if (gettokenid == null){
//                    Toast.makeText(MainActivity.this, "Selamat Datang", Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                    UpdateToken();
                    goToDashboard();
                }

                else{
                    progress.dismiss();
                    Snackbar.make(findViewById(R.id.login),"Akun anda sedang aktif di perangkat lain !",Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.setText("");
                                    pas.setText("");
                                }
                            }).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void cekrulelogin(){

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                document.getData();
//                document.getString("email");
                if (document.exists()) {
                    ceklogin();
                }else{
                    //logout
                    FirebaseAuth.getInstance().signOut();

                    progress.dismiss();
                    Snackbar.make(findViewById(R.id.login),"Rule login belum terdaftar !",Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    user.setText("");
                                    pas.setText("");
                                }
                            }).show();
                }
            }
        });

    }


    private void goToDashboard() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();


    }
    private void UpdateToken(){
        FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);

    }
    private void showMessageBox2(String message2) {
        android.app.AlertDialog.Builder alertDialogBuilder2 = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder2.setTitle("Gagal Login");
        alertDialogBuilder2.setMessage(message2);
        alertDialogBuilder2.setCancelable(false);
        alertDialogBuilder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface2, int i) {
                user.setText("");
                pas.setText("");
//                FirebaseAuth.getInstance().signOut();


                dialogInterface2.dismiss();

            }
        });

        if(!isFinishing())
        {
            alertDialogBuilder2.show();
        }
    }

    private void hideProgress() {
//        RelativeLayout.setVisibility(View.GONE);
        user.setEnabled(true);
        pas.setEnabled(true);
    }

    private void showProgress() {
//        RelativeLayout.setVisibility(View.VISIBLE);
        user.setEnabled(false);
        pas.setEnabled(false);
    }
}