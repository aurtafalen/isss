package com.example.isss.SI;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.GTP.CustomAdapter;
import com.example.isss.GTP.ModalClass;
import com.example.isss.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Induction extends AppCompatActivity {
    TextInputEditText nama,perusahaan,status,jumlah;
    Button selesai;
    ProgressDialog progress;
    private ImageView imageView;
    String sPhoto;
    String txtfoto;

    String getNama;
    String getPerusahaan;
    String getStatus;
    String getjumlah;
    StorageReference storageReference;
    public static final int GALLERY_REQUEST_CODE = 105;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int PICK_FROM_GALLERY = 107;

    //multipleImage
    private static final int IMAGE_CODE = 1;
    private StorageReference mStorageReference;
    List<ModalClass> modalClassList;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_induction);
        //multiple
        modalClassList = new ArrayList<>();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Photo");

        storageReference = FirebaseStorage.getInstance().getReference();

        //loading
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Memproses Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

        nama = (TextInputEditText) findViewById (R.id.nama);
        perusahaan = (TextInputEditText) findViewById (R.id.company);
        status = (TextInputEditText) findViewById (R.id.statusInduction);
        jumlah = (TextInputEditText) findViewById(R.id.jumlahPesertaInduction);
        selesai = (Button) findViewById (R.id.selesaiInduction);

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNama = nama.getText().toString();
                getPerusahaan = perusahaan.getText().toString();
                getStatus = status.getText().toString();
                getjumlah = jumlah.getText().toString();

                if (getNama.isEmpty() && getPerusahaan.isEmpty() && getStatus.isEmpty()){
                    nama.setError("Tidak boleh kosong !");
                    perusahaan.setError("Tidak boleh kosong !");
                    status.setError("Tidak Boleh Kosong !");
                    jumlah.setError("Tidak Boleh Kosong !");

                    Toast.makeText(Induction.this, "Data Tidak lengkap !", Toast.LENGTH_SHORT).show();

                } else if (getNama.isEmpty()){
                    nama.setError("Tidak boleh kosong !");
                    Toast.makeText(Induction.this, "Data Tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getPerusahaan.isEmpty()){
                    perusahaan.setError("Tidak boleh kosong !");
                    Toast.makeText(Induction.this, "Data Tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getStatus.isEmpty()){
                    status.setError("Tidak Boleh Kosong !");
                    Toast.makeText(Induction.this, "Data Tidak lengkap !", Toast.LENGTH_SHORT).show();

                }else if (getjumlah.isEmpty()){
                    jumlah.setError("Tidak Boleh Kosong !");
                    Toast.makeText(Induction.this,"Data Tidak lengkap !", Toast.LENGTH_SHORT).show();

                }
                else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Induction.this);
                    alertDialog.setTitle("Tambah Foto ");

                    alertDialog.setPositiveButton("Gallery",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ambilgalery();
                                }
                            });

                    alertDialog.setNegativeButton("Kamera",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ambilfoto();
                                }
                            });

                    alertDialog.show();


                }
            }
        });
    }

    private void ambilgalery(){

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

    private void ambilfoto() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


    }
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private void UploadPhotoToCloudStore(Bitmap photoBitmap) {
        final String namefile = String.format("Photo_%d.jpg", System.currentTimeMillis());
        sPhoto = namefile;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("Photo/" + namefile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("DrawSignatureActivity", "Fail! upload images. error : " + exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.d("Scan", "Success upload images");

            }
        });
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("Photo/" + name);
        txtfoto = name;
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
                        Picasso.get().load(uri).into(imageView);
                    }
                });

                //        Toast.makeText(Multimedia.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Induction.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();

    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        Induction.this.sendBroadcast(mediaScanIntent);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public boolean addJpgPhotoToGallery(Bitmap photoBitmap) {
        boolean result = false;
        String namefile = String.format("Photo_%d.jpg", System.currentTimeMillis());

        try {
            File photo = new File(getAlbumStorageDir("Photo"), namefile);
            saveBitmapToJPG(photoBitmap, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            // Show pop up window
            LayoutInflater layoutInflater = LayoutInflater.from(Induction.this);
            View promptView = layoutInflater.inflate(R.layout.hasilfoto, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Induction.this);
            alertDialogBuilder.setTitle("Foto");
            alertDialogBuilder.setView(promptView);
            imageView = (ImageView) promptView.findViewById(R.id.gambar1);
            Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photoBitmap);
            Log.d("testPhoto", photoBitmap.toString());

            alertDialogBuilder.setPositiveButton("Selesai",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (addJpgPhotoToGallery(photoBitmap)) {
                                Log.d("photoAdd", "masuknih");
                                UploadPhotoToCloudStore(photoBitmap);

                                progress.show();
                                String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
                                Map<String, Object> isiInduction = new HashMap<>();
                                isiInduction.put("nama", getNama);
                                isiInduction.put("prusahaan", getPerusahaan);
                                isiInduction.put("status", getStatus);
                                isiInduction.put("jumlahPeserta", getjumlah);
                                isiInduction.put("timeDate", FieldValue.serverTimestamp());
                                isiInduction.put("foto", sPhoto);
                                isiInduction.put("filterBulan", dateNow);

                                //firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                //collection templates
                                CollectionReference push = db.collection("induction");

                                push.document()
                                        .set(isiInduction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(Induction.this, "Data Berhasil Ditambah !", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Induction.this, Side_SI.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("Batal",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

            alertDialogBuilder.show();
        }

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                // Show pop up window
                LayoutInflater layoutInflater = LayoutInflater.from(Induction.this);
                View promptView = layoutInflater.inflate(R.layout.recyclerviewimage, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Induction.this);
                alertDialogBuilder.setTitle("Foto");
                alertDialogBuilder.setView(promptView);
                RecyclerView recyclerView = (RecyclerView) promptView.findViewById(R.id.recyclerImage);
                recyclerView.removeAllViews();
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                modalClassList.clear();

                ArrayList listImage = new ArrayList<>();

                int totalitem = data.getClipData().getItemCount();
                for (int i = 0; i < totalitem; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String imagename = getFileName(imageUri);
                    ModalClass modalClass = new ModalClass(imagename, imageUri);

                    modalClassList.add(modalClass);

                    customAdapter = new CustomAdapter(Induction.this, modalClassList);
                    recyclerView.setAdapter(customAdapter);

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
                            Toast.makeText(Induction.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                alertDialogBuilder.setPositiveButton("Selesai",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                progress.show();
                                String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
                                Map<String, Object> isiInduction = new HashMap<>();
                                isiInduction.put("nama", getNama);
                                isiInduction.put("prusahaan", getPerusahaan);
                                isiInduction.put("status", getStatus);
                                isiInduction.put("jumlahPeserta", getjumlah);
                                isiInduction.put("timeDate", FieldValue.serverTimestamp());
                                isiInduction.put("foto", listImage);
                                isiInduction.put("filterBulan", dateNow);

                                //firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                //collection templates
                                CollectionReference push = db.collection("induction");

                                push.document()
                                        .set(isiInduction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(Induction.this, "Data Berhasil Ditambah !", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Induction.this, Side_SI.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }

                        });

                alertDialogBuilder.setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                dialog.cancel();
                            }
                        });

                alertDialogBuilder.show();

//                Toast.makeText(this, "Multiple", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null) {

                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
//                selectedImage.setImageURI(contentUri);
//                uploadImageToFirebase(imageFileName, contentUri);

                // Show pop up window
                LayoutInflater layoutInflater = LayoutInflater.from(Induction.this);
                View promptView = layoutInflater.inflate(R.layout.hasilfoto, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Induction.this);
                alertDialogBuilder.setTitle("Foto");
                alertDialogBuilder.setView(promptView);
                imageView = (ImageView) promptView.findViewById(R.id.gambar1);
//                Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageURI(contentUri);
                Log.d("testPhoto", imageFileName.toString());

                alertDialogBuilder.setPositiveButton("Selesai",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                uploadImageToFirebase(imageFileName, contentUri);
                                progress.show();
                                String dateNow = new SimpleDateFormat("M/yyyy", Locale.getDefault()).format(new Date());
                                Map<String, Object> isiInduction = new HashMap<>();
                                isiInduction.put("nama", getNama);
                                isiInduction.put("prusahaan", getPerusahaan);
                                isiInduction.put("status", getStatus);
                                isiInduction.put("jumlahPeserta", getjumlah);
                                isiInduction.put("timeDate", FieldValue.serverTimestamp());
                                isiInduction.put("foto", txtfoto);
                                isiInduction.put("filterBulan", dateNow);

                                //firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                //collection templates
                                CollectionReference push = db.collection("induction");

                                push.document()
                                        .set(isiInduction).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progress.dismiss();
                                        Toast.makeText(Induction.this, "Data Berhasil Ditambah !", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(Induction.this, Side_SI.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }

                        });

                alertDialogBuilder.setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.cancel();
                            }
                        });

                alertDialogBuilder.show();
                Toast.makeText(this, "Single", Toast.LENGTH_SHORT).show();
            }
        }
    }

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

}