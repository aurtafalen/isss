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
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.isss.GTP.CustomAdapter;
import com.example.isss.GTP.ModalClass;
import com.example.isss.R;
import com.example.isss.SendNotificationPack.APIService;
import com.example.isss.SendNotificationPack.Client;
import com.example.isss.SendNotificationPack.Data;
import com.example.isss.SendNotificationPack.MyResponse;
import com.example.isss.SendNotificationPack.NotificationSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kyanogen.signatureview.SignatureView;
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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InspeksiKetiga extends AppCompatActivity {
    //notif
    private APIService apiService;
    final private String admin1 = "PRk9zfV1HXa3s1p4Ded9LnhO0Mg2";
    final private String title = "!TEMUAN!";
    final private String pesan = "Resiko HIGH !";


    //TTD
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_LOCATION = 2;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 3;
    SignatureView mSignaturePad;
    private static final int REQUEST_PERMISSIONS = 20;

    //camera
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public static final int GALLERY_REQUEST_CODE = 105;
    private static final int PICK_FROM_GALLERY = 107;
    StorageReference storageReference;

    //tindakan
    FloatingActionButton fab;
    Toolbar toolbar;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    //firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //collection templates
    CollectionReference pages = db.collection("si").document("data_inspections").collection("inspections");
    //Collection hasiltemplates
    CollectionReference tugas = db.collection("si").document("data_tugasTemplate").collection("tugasTemplate");


    //penamaan
    TextView qtitle, berikutnya, jPage, nPage;

    //getIntentString
    String documentId;
    String idtemplate;
    String alamat;

    //idPages
    String idPages;

    //idDesClick
    String idDesclick;

    //idContent
    String idContent;

    //idDocSection
    String idDocSection;

    //idPagesBefore
    String idPagesBefore;

    String parentSection;
    //documenttest
    boolean documentTest;
    int hasil;
    //answer
    List<EditText> allAnswer = new ArrayList<EditText>();
    List<String> sizeAnswer = new ArrayList<String>();
    Map<String, Object> mapHasil = new HashMap<>();
//    Map<String, Object> contents = new HashMap<>();
//    Map<String, Object> childContent = new HashMap<>();



    //answerSection
    List<EditText> allAnswerSection = new ArrayList<EditText>();


    //linear
    LinearLayoutCompat myLinearLayout;
//    FrameLayout framelayout;
//    FrameLayout framelayoutSection;

    //String
    String desc;
    String idAn;
    String idAnSection;
    String idAnSectionBox;
    String parentId;
    String parentIdBox;
    Integer sizeawal;
    String ck;
    String idOpsi;
    String ttd;
    String sPhoto;
    String txtfoto;
    String status;


    //Textview
    TextView Description;
    TextView Section;
    TextView DescriptionSection;
    TextView textView6;

    //    //answer
//    EditText Answer;
//    EditText AnswerSection;
    String idaAnswer;

    //Params
    LinearLayoutCompat.LayoutParams params;
    LinearLayoutCompat.LayoutParams params2;
    LinearLayoutCompat.LayoutParams params3;
    LinearLayoutCompat.LayoutParams params4;
    LinearLayoutCompat.LayoutParams params5;
    LinearLayoutCompat.LayoutParams params6;


    DocumentSnapshot lastvisible;


    String inSection;

    String statusTindakan;

    RadioButton boxOpsi;
    RadioButton boxOpsiSec;

    TextView kembali,panah;
    ProgressDialog progress;

    ProgressBar progressBar;

    String qAction;

    ArrayList<Map> isi;

    ConstraintLayout constraintLayout;

    FrameLayout frame;
    ScrollView scrollView;

    //multipleImage
    private static final int IMAGE_CODE = 1;
    private StorageReference mStorageReference;
    List<ModalClass> modalClassList;
    CustomAdapter customAdapter;

    private int waktu_loading = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspeksi_ketiga);

        //multiple
        modalClassList = new ArrayList<>();
        mStorageReference = FirebaseStorage.getInstance().getReference().child("Photo");
        storageReference = FirebaseStorage.getInstance().getReference();

//        frame = findViewById(R.id.textView4);
//        frame.setVisibility(View.INVISIBLE);
//
//        scrollView = findViewById(R.id.scrollView2);
//        scrollView.setVisibility(View.INVISIBLE);
//
//        progressBar = findViewById(R.id.cirlce);
//        progressBar.setVisibility(View.VISIBLE);
//
//        textView6 = findViewById(R.id.textView6);
//        textView6.setVisibility(View.INVISIBLE);

        //notifservice
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        //loading
        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Memproses Data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
//        progress.show();



        //idDocument
        documentId = getIntent().getStringExtra("doc");
        idtemplate = getIntent().getStringExtra("idtem");
        alamat = getIntent().getStringExtra("alamat");

        //judul
        qtitle = findViewById(R.id.qTitile);
//        qtitle.setVisibility(View.INVISIBLE);

        //button berikutnya
        berikutnya = findViewById(R.id.berikutnya);

        //Halaman
        jPage = findViewById(R.id.jPage);
        nPage = findViewById(R.id.nPage);

//        jPage.setVisibility(View.INVISIBLE);
//        nPage.setVisibility(View.INVISIBLE);

        showtitle();
        halaman();

        //Button
        buttonberiktunya();

        //kembali
        kembali = findViewById(R.id.kembali);
        panah = findViewById(R.id.panahback);
        buttonkembali();

        if (idPagesBefore == null){
            kembali.setVisibility(View.INVISIBLE);
            panah.setVisibility(View.INVISIBLE);
        }else{
            kembali.setVisibility(View.INVISIBLE);
            panah.setVisibility(View.INVISIBLE);
        }

    }

    private void halaman() {
        //getjumlahpage
        pages.document(documentId)
                .collection("pages")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                sizeawal = task.getResult().size();
                Log.d("sizeawal : ", String.valueOf(sizeawal));
                jPage.setText(String.valueOf(sizeawal));


            }
        });
    }

    private void showtitle() {

        pages.document(documentId)
                .collection("pages")
                .limit(1)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                //title
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    String title = (String) documentSnapshot.get("pageTitle");
                    qtitle.setText(title);
                    idPages = documentSnapshot.getId();
                    Log.d("testgettask", idPages);
                }
                lastvisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                showcontent();

            }



        });
    }

    private void showcontent() {
        progress.show();
        pages.document(documentId)
                .collection("pages")
                .document(idPages)
                .collection("contents")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            if (task.getResult().isEmpty()) {
                                Log.d("suksesawal", "No");
                                showcontent();
                            }else{
                                Log.d("suksesawal", "Yes");
                                progress.dismiss();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    myLinearLayout = findViewById(R.id.lPertanyaan);


                                    params3 = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                                    params3.setMargins(10, 20, 10, 20);

                                    params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                                    params.setMargins(30, 20, 30, 20);

                                    params4 = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, 100);
                                    params4.setMargins(30, 0, 30, 20);

                                    params5 = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, 100);
                                    params5.setMargins(50, 20, 50, 20);

                                    params2 = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                                    params2.setMargins(0, 300, 0, 30);

                                    params6 = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
                                    params6.setMargins(30, 200, 30, 20);

                                    if (document != null && document.exists()) {
                                        desc = (String) document.get("description");
                                        String type = (String) document.get("type");
                                        Log.d("cekdes", desc);
                                        Log.d("cektype", type);

                                        if (type.equals("section")) {


                                            HashMap<String, Object> contentArray = new HashMap<>();

                                            contentArray.put("description", document.get("description"));
                                            contentArray.put("type", document.get("type"));
                                            contentArray.put("id", document.getId());

                                            Log.d("cekDes", contentArray.get("description").toString());


                                            ArrayList<Map> tempatTerakhir = new ArrayList<Map>();

                                            ArrayList childContent = new ArrayList();

                                            idDocSection = document.getId();
                                            pages.document(documentId)
                                                    .collection("pages")
                                                    .document(idPages)
                                                    .collection("contents")
                                                    .document(idDocSection)
                                                    .collection("contents")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                if (task.getResult().isEmpty()) {
                                                                    Log.d("suksesSection", "No");
//                                                                    Intent intent = getIntent();
//                                                                    overridePendingTransition(0, 0);
//                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                                                    finish();
//                                                                    overridePendingTransition(0, 0);
//                                                                    startActivity(intent);
                                                                    checkAgain();
                                                                } else {
                                                                    Log.d("suksesSection", "Yes");
//                                                                    progressBar.setVisibility(View.INVISIBLE);
//                                                                    qtitle.setVisibility(View.VISIBLE);
//                                                                    frame.setVisibility(View.VISIBLE);
//                                                                    scrollView.setVisibility(View.VISIBLE);
//                                                                    myLinearLayout.setVisibility(View.VISIBLE);
//                                                                    jPage.setVisibility(View.VISIBLE);
//                                                                    nPage.setVisibility(View.VISIBLE);
//                                                                    textView6.setVisibility(View.VISIBLE);
                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                            Log.d("checkchild", "sukses");

                                                                            Map<String, Object> childContentObj = new HashMap<>();
                                                                            childContentObj.put("id", document.getId());
                                                                            childContentObj.put("parentContentId", document.getString("parentContentId"));
                                                                            childContentObj.put("type", document.getString("type"));
                                                                            childContentObj.put("description", document.getString("description"));
                                                                            childContentObj.put("typeOfResponse", document.get("typeOfResponse"));
                                                                            childContent.add(childContentObj);
                                                                    }

                                                                    contentArray.put("childContents", childContent);
                                                                    Log.d("asu1", contentArray.toString());
                                                                    tempatTerakhir.remove(contentArray);
                                                                    tempatTerakhir.add(contentArray);
                                                                    Log.d("asu2", tempatTerakhir.toString());

                                                                    progress.dismiss();

                                                                    int sizeTempatTerakhir = tempatTerakhir.size();
                                                                    for (int i = 0; i < sizeTempatTerakhir; i++) {

                                                                        String descS = tempatTerakhir.get(i).get("description").toString();

                                                                        Log.d("getDescS", descS);
                                                                        Log.d("tempatTerakhir", tempatTerakhir.toString());

                                                                        //Build Section
                                                                        Section = new TextView(InspeksiKetiga.this);
                                                                        Section.setLayoutParams(params3);
                                                                        Section.setBackgroundResource(R.drawable.cardsection);
                                                                        Section.setTextSize(13);
                                                                        Section.setMaxLines(1);
                                                                        Section.setPaddingRelative(25, 25, 10, 25);
                                                                        Section.setTypeface(null, Typeface.ITALIC);
                                                                        Section.setTextColor(Color.parseColor("#767676"));
                                                                        Drawable img1 = getApplicationContext().getResources().getDrawable(R.drawable.down_icon);
                                                                        Section.setCompoundDrawablesWithIntrinsicBounds(null, null, img1, null);
                                                                        Section.setText(descS);

                                                                        myLinearLayout.addView(Section);

                                                                        isi = (ArrayList<Map>) tempatTerakhir.get(i).get("childContents");
                                                                        Log.d("checkIsi", isi.toString());

                                                                        for (int a = 0; a < isi.size(); a++) {

                                                                            //Initializing frame layout
                                                                            FrameLayout framelayoutSection = new FrameLayout(InspeksiKetiga.this);
                                                                            framelayoutSection.setLayoutParams(params3);
                                                                            framelayoutSection.setBackgroundResource(R.drawable.cardpertanyaan);

                                                                            String descIsi = (String) isi.get(a).get("description").toString();
                                                                            Log.d("iniDescSec", descIsi);

                                                                            Log.d("iniDescSec", "Notnull");

                                                                            //get Map typeOfresonse
                                                                            Map maptype = (Map) isi.get(a).get("typeOfResponse");
                                                                            Log.d("maptypeInSection", maptype.toString());

                                                                            //get type in Map typeOfresponse
                                                                            String typeResponse = String.valueOf(maptype.get("type"));
                                                                            Log.d("getTypeResponseInsection", typeResponse);

                                                                            // Build Description
                                                                            final TextView DescriptionSection = new TextView(InspeksiKetiga.this);
                                                                            DescriptionSection.setBackgroundResource(R.drawable.cardpertanyaan);
                                                                            DescriptionSection.setTextSize(11);
                                                                            DescriptionSection.setPaddingRelative(25, 25, 10, 25);
                                                                            DescriptionSection.setTypeface(null, Typeface.ITALIC);
                                                                            DescriptionSection.setTextColor(Color.parseColor("#767676"));
                                                                            DescriptionSection.setLayoutParams(params3);
//                                                                            DescriptionSection.setMaxLines(4);
//                                                                      Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.action_icon);
//                                                                      DescriptionSection.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                                                                            DescriptionSection.setTag(R.id.idClick, isi.get(a).get("id"));
                                                                            DescriptionSection.setTag(R.id.parentSection, isi.get(a).get("parentContentId"));
                                                                            DescriptionSection.setText("Pertanyaan :" + "\n" + descIsi);

                                                                            Log.d("idclickk", DescriptionSection.getTag(R.id.idClick).toString() + "parentSection : " + DescriptionSection.getTag(R.id.parentSection).toString());
                                                                            DescriptionSection.setOnTouchListener(new View.OnTouchListener() {
                                                                                @Override
                                                                                public boolean onTouch(View v, MotionEvent event) {
                                                                                    idDesclick = DescriptionSection.getTag(R.id.idClick).toString();
                                                                                    parentSection = DescriptionSection.getTag(R.id.parentSection).toString();
                                                                                    Log.d("idDesc", idDesclick + " parent : " + parentSection);

                                                                                    //popup menu
                                                                                    final PopupMenu popupMenu2 = new PopupMenu(InspeksiKetiga.this, DescriptionSection);
                                                                                    //add menu items in popup menu
                                                                                    popupMenu2.getMenu().add(Menu.NONE, 0, 0, "Tambah Catatan"); //parm 2 is menu id, param 3 is position of this menu item in menu items list, param 4 is title of the menu
                                                                                    popupMenu2.getMenu().add(Menu.NONE, 1, 1, "Tambah Foto");
                                                                                    popupMenu2.getMenu().add(Menu.NONE, 2, 2, "Tambah Tindakan");

                                                                                    //handle menu item clicks
                                                                                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                                                        @Override
                                                                                        public boolean onMenuItemClick(MenuItem menuItem) {
                                                                                            //get id of the clicked item
                                                                                            int id = menuItem.getItemId();
                                                                                            //handle clicks
                                                                                            if (id == 0) {

                                                                                                tambahcatatanSection();
                                                                                                //Copy clicked
                                                                                                //set text
                                                                                                //selectedTv.setText("Copy clicked");
                                                                                            } else if (id == 1) {
                                                                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspeksiKetiga.this);
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
//                                                                                                              dialog.cancel();
                                                                                                                ambilfoto();
                                                                                                            }
                                                                                                        });

                                                                                                alertDialog.show();

//                                                                                                ambilfotoSection();
                                                                                                //Share clicked
                                                                                                //set text
                                                                                                // selectedTv.setText("Share clicked");
                                                                                            } else if (id == 2) {
                                                                                                tindakanSection();
                                                                                                //Save clicked
                                                                                                //set text
                                                                                                //selectedTv.setText("Save clicked");
                                                                                            }

                                                                                            return false;
                                                                                        }
                                                                                    });
                                                                                    popupMenu2.show();

                                                                                    return false;
                                                                                }
                                                                            });


                                                                            framelayoutSection.addView(DescriptionSection);
//                                                                      myLinearLayout.addView(DescriptionSection);

                                                                            if (typeResponse.equals("multiple-choices")) {

                                                                                ArrayList opsi = (ArrayList) maptype.get("option");
                                                                                Log.d("iniOpsi", opsi.toString());

                                                                                //get Map optionObj
                                                                                Map mapOptionObj = (Map) maptype.get("optionObj");
                                                                                Log.d("optionObj", mapOptionObj.toString());

                                                                                List<Map<String, Object>> choicesSection = (List<Map<String, Object>>) mapOptionObj.get("choices");
                                                                                Log.d("choicesSec", choicesSection.toString());

                                                                                //MapOpsiSection
                                                                                ArrayList<String> mapOpsiSection = new ArrayList<String>();
                                                                                RadioGroup rgs = new RadioGroup(InspeksiKetiga.this); //create the RadioGroup
                                                                                rgs.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                                                                                rgs.setLayoutParams(params2);
                                                                                for (int b = 0; b < choicesSection.size(); b++) {
                                                                                    String nameSection = (String) choicesSection.get(b).get("name");

                                                                                    // Type = checkboxes
                                                                                    boxOpsiSec = new RadioButton(InspeksiKetiga.this);
                                                                                    boxOpsiSec.setLayoutParams(params5);
                                                                                    boxOpsiSec.setId(View.generateViewId());
                                                                                    boxOpsiSec.setText(nameSection);
//                                                                              boxOpsiSec.setTag(R.id.idColorSection,choicesSection.get(b).get("bgColor"));
//                                                                              boxOpsiSec.setTag(R.id.idColorTextSection,choicesSection.get(b).get("textColor"));
                                                                                    boxOpsiSec.setTag(R.id.idClick, isi.get(a).get("id"));
                                                                                    boxOpsiSec.setTag(R.id.parentSection, isi.get(a).get("parentContentId"));
                                                                                    boxOpsiSec.setBackgroundColor(Color.parseColor(choicesSection.get(b).get("bgColor").toString()));
                                                                                    boxOpsiSec.setTextColor(Color.parseColor(choicesSection.get(b).get("textColor").toString()));
                                                                                    boxOpsiSec.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                                                                              boxOpsiSec.setButtonDrawable(new StateListDrawable()); //remove circle
                                                                                    rgs.addView(boxOpsiSec);
                                                                                }
                                                                                framelayoutSection.addView(rgs);
                                                                                rgs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                                    @Override
                                                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                                                        boxOpsiSec = (RadioButton) findViewById(checkedId);
                                                                                        idAnSectionBox = boxOpsiSec.getTag(R.id.idClick).toString();
                                                                                        parentIdBox = boxOpsiSec.getTag(R.id.parentSection).toString();

                                                                                        Log.d("getcolorcheck",String.valueOf(boxOpsiSec.getTextColors().getDefaultColor()));

                                                                                        if (String.valueOf(boxOpsiSec.getTextColors().getDefaultColor()).equals("-769226")){
                                                                                            tindakanSection();

                                                                                            idAnSectionBox = boxOpsiSec.getTag(R.id.idClick).toString();
                                                                                            parentIdBox = boxOpsiSec.getTag(R.id.parentSection).toString();
                                                                                            mapOpsiSection.add(boxOpsiSec.getText().toString());

                                                                                            pages.document(documentId)
                                                                                                    .collection("pages")
                                                                                                    .document(idPages)
                                                                                                    .collection("contents")
                                                                                                    .document(parentIdBox)
                                                                                                    .collection("contents")
                                                                                                    .document(idAnSectionBox)
                                                                                                    .update("answer", boxOpsiSec.getText());

                                                                                        }else{

                                                                                            idAnSectionBox = boxOpsiSec.getTag(R.id.idClick).toString();
                                                                                            parentIdBox = boxOpsiSec.getTag(R.id.parentSection).toString();
                                                                                            mapOpsiSection.add(boxOpsiSec.getText().toString());

                                                                                            pages.document(documentId)
                                                                                                    .collection("pages")
                                                                                                    .document(idPages)
                                                                                                    .collection("contents")
                                                                                                    .document(parentIdBox)
                                                                                                    .collection("contents")
                                                                                                    .document(idAnSectionBox)
                                                                                                    .update("answer", boxOpsiSec.getText());
                                                                                        }
                                                                                    }
                                                                                });
//                                                                    myLinearLayout.addView(rgs);
                                                                            } else {

                                                                                final EditText AnswerSection = new EditText(InspeksiKetiga.this);
                                                                                AnswerSection.setLayoutParams(params6);
                                                                                AnswerSection.setTextSize(11);
//                                                                          AnswerSection.setBackgroundColor(Color.parseColor("#767676"));
                                                                                AnswerSection.setHint("Jawab disini");
                                                                                AnswerSection.setTag(R.id.id, isi.get(a).get("id"));
                                                                                AnswerSection.setTag(R.id.parentContentId, isi.get(a).get("parentContentId"));


                                                                                AnswerSection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                                                    @Override
                                                                                    public void onFocusChange(View v, boolean hasFocus) {

                                                                                        if (hasFocus) {
                                                                                            idAnSection = AnswerSection.getTag(R.id.id).toString();
                                                                                            parentId = AnswerSection.getTag(R.id.parentContentId).toString();

                                                                                            Log.d("getIdSectionAsu", idAnSection);
                                                                                            Log.d("fokus Ya " + "parentId", parentId + "  idaSection : " + idAnSection);

                                                                                        } else {
                                                                                            //Text
                                                                                            String idaAnswer = AnswerSection.getText().toString();
                                                                                            pages.document(documentId)
                                                                                                    .collection("pages")
                                                                                                    .document(idPages)
                                                                                                    .collection("contents")
                                                                                                    .document(parentId)
                                                                                                    .collection("contents")
                                                                                                    .document(idAnSection)
                                                                                                    .update("answer", idaAnswer);

//                                                                            Log.d("fokus Tidak " + "parentId", parentId + "  idaSection : " + idAnSection);
                                                                                        }
                                                                                    }
                                                                                });

                                                                                framelayoutSection.addView(AnswerSection);
//                                                                    myLinearLayout.addView(AnswerSection);
                                                                            }

                                                                            myLinearLayout.addView(framelayoutSection);

                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        private void checkAgain() {

                                                            pages.document(documentId)
                                                                    .collection("pages")
                                                                    .document(idPages)
                                                                    .collection("contents")
                                                                    .document(idDocSection)
                                                                    .collection("contents")
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                if (task.getResult().isEmpty()) {
                                                                                    Log.d("suksesSection", "No");
                                                                                    progress.show();
                                                                                    checkAgain();
                                                                                } else {
                                                                                    Log.d("suksesSection", "yes");
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        Log.d("checkchild", "sukses");

                                                                                        Map<String, Object> childContentObj = new HashMap<>();
                                                                                        childContentObj.put("id", document.getId());
                                                                                        childContentObj.put("parentContentId", document.getString("parentContentId"));
                                                                                        childContentObj.put("type", document.getString("type"));
                                                                                        childContentObj.put("description", document.getString("description"));
                                                                                        childContentObj.put("typeOfResponse", document.get("typeOfResponse"));
                                                                                        childContent.add(childContentObj);
                                                                                    }
                                                                                    contentArray.put("childContents", childContent);
                                                                                    Log.d("asu1", contentArray.toString());
                                                                                    tempatTerakhir.remove(contentArray);
                                                                                    tempatTerakhir.add(contentArray);
                                                                                    Log.d("asu2", tempatTerakhir.toString());
                                                                                    progress.dismiss();
                                                                                }


                                                                                    int sizeTempatTerakhir = tempatTerakhir.size();
                                                                                    for (int i = 0; i < sizeTempatTerakhir; i++) {

                                                                                        String descS = tempatTerakhir.get(i).get("description").toString();

                                                                                        Log.d("getDescS", descS);
                                                                                        Log.d("tempatTerakhir", tempatTerakhir.toString());

                                                                                        //Build Section
                                                                                        Section = new TextView(InspeksiKetiga.this);
                                                                                        Section.setLayoutParams(params3);
                                                                                        Section.setBackgroundResource(R.drawable.cardsection);
                                                                                        Section.setTextSize(13);
                                                                                        Section.setPaddingRelative(25, 25, 10, 25);
                                                                                        Section.setTypeface(null, Typeface.ITALIC);
                                                                                        Section.setTextColor(Color.parseColor("#767676"));
                                                                                        Drawable img1 = getApplicationContext().getResources().getDrawable(R.drawable.down_icon);
                                                                                        Section.setCompoundDrawablesWithIntrinsicBounds(null, null, img1, null);
                                                                                        Section.setText(descS);

                                                                                        myLinearLayout.addView(Section);

                                                                                        isi = (ArrayList<Map>) tempatTerakhir.get(i).get("childContents");
                                                                                        Log.d("checkIsi", isi.toString());

                                                                                        for (int a = 0; a < isi.size(); a++) {

                                                                                            //Initializing frame layout
                                                                                            FrameLayout framelayoutSection = new FrameLayout(InspeksiKetiga.this);
                                                                                            framelayoutSection.setLayoutParams(params3);
                                                                                            framelayoutSection.setBackgroundResource(R.drawable.cardpertanyaan);

                                                                                            String descIsi = (String) isi.get(a).get("description").toString();
                                                                                            Log.d("iniDescSec", descIsi);

                                                                                            Log.d("iniDescSec", "Notnull");

                                                                                            //get Map typeOfresonse
                                                                                            Map maptype = (Map) isi.get(a).get("typeOfResponse");
                                                                                            Log.d("maptypeInSection", maptype.toString());

                                                                                            //get type in Map typeOfresponse
                                                                                            String typeResponse = String.valueOf(maptype.get("type"));
                                                                                            Log.d("getTypeResponseInsection", typeResponse);

                                                                                            // Build Description
                                                                                            final TextView DescriptionSection = new TextView(InspeksiKetiga.this);
                                                                                            DescriptionSection.setBackgroundResource(R.drawable.cardpertanyaan);
                                                                                            DescriptionSection.setTextSize(11);
                                                                                            DescriptionSection.setPaddingRelative(25, 25, 10, 25);
                                                                                            DescriptionSection.setTypeface(null, Typeface.ITALIC);
                                                                                            DescriptionSection.setTextColor(Color.parseColor("#767676"));
                                                                                            DescriptionSection.setLayoutParams(params3);
                                                                                            DescriptionSection.setTag(R.id.idClick, isi.get(a).get("id"));
                                                                                            DescriptionSection.setTag(R.id.parentSection, isi.get(a).get("parentContentId"));
                                                                                            DescriptionSection.setText("Pertanyaan :" + "\n" + descIsi);
//                                                                                            DescriptionSection.setMaxLines(4);

                                                                                            Log.d("idclickk", DescriptionSection.getTag(R.id.idClick).toString() + "parentSection : " + DescriptionSection.getTag(R.id.parentSection).toString());
                                                                                            DescriptionSection.setOnTouchListener(new View.OnTouchListener() {
                                                                                                @Override
                                                                                                public boolean onTouch(View v, MotionEvent event) {
                                                                                                    idDesclick = DescriptionSection.getTag(R.id.idClick).toString();
                                                                                                    parentSection = DescriptionSection.getTag(R.id.parentSection).toString();
                                                                                                    Log.d("idDesc", idDesclick + " parent : " + parentSection);

                                                                                                    //popup menu
                                                                                                    final PopupMenu popupMenu2 = new PopupMenu(InspeksiKetiga.this, DescriptionSection);
                                                                                                    //add menu items in popup menu
                                                                                                    popupMenu2.getMenu().add(Menu.NONE, 0, 0, "Tambah Catatan"); //parm 2 is menu id, param 3 is position of this menu item in menu items list, param 4 is title of the menu
                                                                                                    popupMenu2.getMenu().add(Menu.NONE, 1, 1, "Tambah Foto");
                                                                                                    popupMenu2.getMenu().add(Menu.NONE, 2, 2, "Tambah Tindakan");

                                                                                                    //handle menu item clicks
                                                                                                    popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                                                                        @Override
                                                                                                        public boolean onMenuItemClick(MenuItem menuItem) {
                                                                                                            //get id of the clicked item
                                                                                                            int id = menuItem.getItemId();
                                                                                                            //handle clicks
                                                                                                            if (id == 0) {

                                                                                                                tambahcatatanSection();
                                                                                                                //Copy clicked
                                                                                                                //set text
                                                                                                                //selectedTv.setText("Copy clicked");
                                                                                                            } else if (id == 1) {
                                                                                                                ambilfotoSection();
                                                                                                                //Share clicked
                                                                                                                //set text
                                                                                                                // selectedTv.setText("Share clicked");
                                                                                                            } else if (id == 2) {
                                                                                                                tindakanSection();
                                                                                                                //Save clicked
                                                                                                                //set text
                                                                                                                //selectedTv.setText("Save clicked");
                                                                                                            }

                                                                                                            return false;
                                                                                                        }
                                                                                                    });
                                                                                                    popupMenu2.show();

                                                                                                    return false;
                                                                                                }
                                                                                            });


                                                                                            framelayoutSection.addView(DescriptionSection);

                                                                                            if (typeResponse.equals("multiple-choices")) {

                                                                                                ArrayList opsi = (ArrayList) maptype.get("option");
                                                                                                Log.d("iniOpsi", opsi.toString());

                                                                                                //get Map optionObj
                                                                                                Map mapOptionObj = (Map) maptype.get("optionObj");
                                                                                                Log.d("optionObj", mapOptionObj.toString());

                                                                                                List<Map<String, Object>> choicesSection = (List<Map<String, Object>>) mapOptionObj.get("choices");
                                                                                                Log.d("choicesSec", choicesSection.toString());

                                                                                                //MapOpsiSection
                                                                                                ArrayList<String> mapOpsiSection = new ArrayList<String>();
                                                                                                RadioGroup rgs = new RadioGroup(InspeksiKetiga.this); //create the RadioGroup
                                                                                                rgs.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                                                                                                rgs.setLayoutParams(params2);
                                                                                                for (int b = 0; b < choicesSection.size(); b++) {
                                                                                                    String nameSection = (String) choicesSection.get(b).get("name");

                                                                                                    // Type = checkboxes
                                                                                                    boxOpsiSec = new RadioButton(InspeksiKetiga.this);
                                                                                                    boxOpsiSec.setLayoutParams(params5);
                                                                                                    boxOpsiSec.setId(View.generateViewId());
                                                                                                    boxOpsiSec.setText(nameSection);
                                                                                                    boxOpsiSec.setTag(R.id.idClick, isi.get(a).get("id"));
                                                                                                    boxOpsiSec.setTag(R.id.parentSection, isi.get(a).get("parentContentId"));
                                                                                                    boxOpsiSec.setBackgroundColor(Color.parseColor(choicesSection.get(b).get("bgColor").toString()));
                                                                                                    boxOpsiSec.setTextColor(Color.parseColor(choicesSection.get(b).get("textColor").toString()));
                                                                                                    boxOpsiSec.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                                                                    rgs.addView(boxOpsiSec);
                                                                                                }
                                                                                                framelayoutSection.addView(rgs);
                                                                                                rgs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                                                    @Override
                                                                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                                                                        boxOpsiSec = (RadioButton) findViewById(checkedId);
                                                                                                        idAnSectionBox = boxOpsiSec.getTag(R.id.idClick).toString();
                                                                                                        parentIdBox = boxOpsiSec.getTag(R.id.parentSection).toString();
                                                                                                        mapOpsiSection.add(boxOpsiSec.getText().toString());

                                                                                                        pages.document(documentId)
                                                                                                                .collection("pages")
                                                                                                                .document(idPages)
                                                                                                                .collection("contents")
                                                                                                                .document(parentIdBox)
                                                                                                                .collection("contents")
                                                                                                                .document(idAnSectionBox)
                                                                                                                .update("answer", boxOpsiSec.getText());

                                                                                                    }
                                                                                                });
                                                                                            } else {

                                                                                                final EditText AnswerSection = new EditText(InspeksiKetiga.this);
                                                                                                AnswerSection.setLayoutParams(params6);
                                                                                                AnswerSection.setTextSize(11);
                                                                                                AnswerSection.setHint("Jawab disini");
                                                                                                AnswerSection.setTag(R.id.id, isi.get(a).get("id"));
                                                                                                AnswerSection.setTag(R.id.parentContentId, isi.get(a).get("parentContentId"));


                                                                                                AnswerSection.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                                                                    @Override
                                                                                                    public void onFocusChange(View v, boolean hasFocus) {

                                                                                                        if (hasFocus) {
                                                                                                            idAnSection = AnswerSection.getTag(R.id.id).toString();
                                                                                                            parentId = AnswerSection.getTag(R.id.parentContentId).toString();

                                                                                                            Log.d("getIdSectionAsu", idAnSection);
                                                                                                            Log.d("fokus Ya " + "parentId", parentId + "  idaSection : " + idAnSection);

                                                                                                        } else {
                                                                                                            //Text
                                                                                                            String idaAnswer = AnswerSection.getText().toString();
                                                                                                            pages.document(documentId)
                                                                                                                    .collection("pages")
                                                                                                                    .document(idPages)
                                                                                                                    .collection("contents")
                                                                                                                    .document(parentId)
                                                                                                                    .collection("contents")
                                                                                                                    .document(idAnSection)
                                                                                                                    .update("answer", idaAnswer);

                                                                                                        }
                                                                                                    }
                                                                                                });

                                                                                                framelayoutSection.addView(AnswerSection);

                                                                                            }

                                                                                            myLinearLayout.addView(framelayoutSection);

                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                    });
                                                        }
                                                    });

                                        } else {

                                        //Initializing frame layout
                                        FrameLayout framelayout = new FrameLayout(InspeksiKetiga.this);
                                        framelayout.setLayoutParams(params3);
                                        framelayout.setBackgroundResource(R.drawable.cardpertanyaan);


                                        //get Map typeOfresonse
                                        Map maptype = (Map) document.get("typeOfResponse");
                                        Log.d("maptype", maptype.toString());

                                        //get type in Map typeOfresponse
                                        String typeResponse = String.valueOf(maptype.get("type"));
                                        Log.d("getTypeResponse", typeResponse);

                                        TextView Description = new TextView(InspeksiKetiga.this);
                                        Description.setBackgroundResource(R.drawable.cardpertanyaan);
                                        Description.setTextSize(11);
                                        Description.setPaddingRelative(25, 25, 10, 25);
                                        Description.setTypeface(null, Typeface.ITALIC);
                                        Description.setTextColor(Color.parseColor("#767676"));
                                        Description.setLayoutParams(params3);
//                                        Description.setMaxLines(4);
//                                        Drawable img = getApplicationContext().getResources().getDrawable(R.drawable.action_icon);
//                                        Description.setCompoundDrawablesWithIntrinsicBounds(null, null, null, img);
                                        Description.setText("Pertanyaan :" + "\n" + desc);
                                        Description.setOnTouchListener(new View.OnTouchListener() {
                                            @Override
                                            public boolean onTouch(View v, MotionEvent event) {
                                                idDesclick = document.getId();
                                                qAction = Description.getText().toString();
                                                Log.d("idDesc", idDesclick);

                                                //popup menu
                                                final PopupMenu popupMenu = new PopupMenu(InspeksiKetiga.this, Description);
                                                //add menu items in popup menu
                                                popupMenu.getMenu().add(Menu.NONE, 0, 0, "Tambah Catatan"); //parm 2 is menu id, param 3 is position of this menu item in menu items list, param 4 is title of the menu
                                                popupMenu.getMenu().add(Menu.NONE, 1, 1, "Tambah Foto");
                                                popupMenu.getMenu().add(Menu.NONE, 2, 2, "Tambah Tindakan");

                                                //handle menu item clicks
                                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                    @Override
                                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                                        //get id of the clicked item
                                                        int id = menuItem.getItemId();
                                                        //handle clicks
                                                        if (id == 0) {
                                                            tambahcatatan();
                                                            //Copy clicked
                                                            //set text
                                                            //selectedTv.setText("Copy clicked");
                                                        } else if (id == 1) {

                                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspeksiKetiga.this);
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
//                                                                            dialog.cancel();
                                                                            ambilfoto();
                                                                        }
                                                                    });

                                                            alertDialog.show();

//                                                            ambilfoto();
                                                            //Share clicked
                                                            //set text
                                                            // selectedTv.setText("Share clicked");
                                                        } else if (id == 2) {
                                                            tindakan();
                                                            //Save clicked
                                                            //set text
                                                            //selectedTv.setText("Save clicked");
                                                        }

                                                        return false;
                                                    }
                                                });
                                                //handle button click, show popup menu
                                                Description.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        popupMenu.show();

                                                    }
                                                });

                                                return false;
                                            }
                                        });

                                        framelayout.addView(Description);

                                        if (typeResponse.equals("multiple-choices")) {

                                            ArrayList opsi = (ArrayList) maptype.get("option");
                                            Log.d("iniOpsi", opsi.toString());

                                            //MapOpsi
                                            ArrayList<String> mapOpsi = new ArrayList<String>();

                                            //get Map optionObj
                                            Map mapOptionObj= (Map) maptype.get("optionObj");
                                            Log.d("optionObj", mapOptionObj.toString());

                                            List<Map<String, Object>> choices = (List<Map<String, Object>>) mapOptionObj.get("choices");
                                            Log.d("choices", choices.toString());



                                            RadioGroup rg = new RadioGroup(InspeksiKetiga.this); //create the RadioGroup
                                            rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
                                            rg.setLayoutParams(params2);
                                            for (int i = 0; i < choices.size(); i++) {

                                                String name = (String) choices.get(i).get("name");

                                                // Type = checkboxes
                                                boxOpsi = new RadioButton(InspeksiKetiga.this);
                                                boxOpsi.setText(name);
                                                boxOpsi.setId(View.generateViewId());
                                                boxOpsi.setLayoutParams(params4);
//                                                boxOpsi.setTag(R.id.idColor,choices.get(i).get("bgColor"));
//                                                boxOpsi.setTag(R.id.idColorText,choices.get(i).get("textColor"));
                                                boxOpsi.setBackgroundColor(Color.parseColor(choices.get(i).get("bgColor").toString()));
                                                boxOpsi.setTextColor(Color.parseColor(choices.get(i).get("textColor").toString()));
//                                                boxOpsi.setButtonDrawable(new StateListDrawable()); //remove circle
                                                boxOpsi.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                rg.addView(boxOpsi);


                                            }
                                            framelayout.addView(rg);

                                            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                @Override
                                                public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                    boxOpsi = (RadioButton) findViewById(checkedId);
                                                    Log.d("getcolorcheck",String.valueOf(boxOpsi.getTextColors().getDefaultColor()));

                                                    if (String.valueOf(boxOpsi.getTextColors().getDefaultColor()).equals("-769226")){
                                                        tindakan();

                                                        idDesclick = document.getId();
                                                        qAction = Description.getText().toString();

                                                        idOpsi = document.getId();
                                                        Log.d("opsiAns", mapOpsi.toString());
                                                        //checkboxes update
                                                        pages.document(documentId)
                                                                .collection("pages")
                                                                .document(idPages)
                                                                .collection("contents")
                                                                .document(idOpsi)
                                                                .update("answer", boxOpsi.getText());
                                                    }else {

                                                        idOpsi = document.getId();
                                                        Log.d("opsiAns", mapOpsi.toString());
                                                        //checkboxes update
                                                        pages.document(documentId)
                                                                .collection("pages")
                                                                .document(idPages)
                                                                .collection("contents")
                                                                .document(idOpsi)
                                                                .update("answer", boxOpsi.getText());
                                                    }

                                                }
                                            });

                                        } else {
                                            // Type = Text
                                            final EditText Answer = new EditText(InspeksiKetiga.this);
                                            Answer.setLayoutParams(params6);
                                            Answer.setTextSize(11);
//                                            Answer.setBackgroundColor(Color.parseColor("#767676"));
                                            Answer.setHint("Jawab disini");

                                            Answer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View v, boolean hasFocus) {

                                                    if (hasFocus) {

                                                        idAn = document.getId();
                                                        String parentId = (String) document.get("parentId");
                                                        Log.d("fokus", "ya");
                                                        Log.d("ida", idAn);

                                                    } else {
                                                        //Text
                                                        idaAnswer = Answer.getText().toString();
                                                        sizeAnswer.add(idaAnswer);
                                                        pages.document(documentId)
                                                                .collection("pages")
                                                                .document(idPages)
                                                                .collection("contents")
                                                                .document(idAn)
                                                                .update("answer", idaAnswer);

                                                        Log.d("fokus", "tidak");
                                                    }
                                                }
                                            });


                                            framelayout.addView(Answer);
                                        }
                                            myLinearLayout.addView(framelayout);

                                        }

                                    }
                            }

                        }

                    }

            }

        });


    }

    private void buttonberiktunya() {

        berikutnya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();

                        int angkaawal = Integer.parseInt(nPage.getText().toString());
                        int tambah = 1;
                        hasil = angkaawal + tambah;


                        int jsize = Integer.parseInt(jPage.getText().toString());
                        int jpage = Integer.parseInt(nPage.getText().toString());


                        int jsizeAnswer = allAnswer.size();
                        int jsizeinAnswer = sizeAnswer.size();

                        Log.d("jsizeAnswer", String.valueOf(jsizeAnswer) + " sizeAnswer : " + jsizeinAnswer);


                        if (jpage >= jsize) {
                            ttd();
                            nPage.setText(String.valueOf(sizeawal));

                        } else {
                            idPagesBefore = idPages;
                            if (idPagesBefore == null){
                                kembali.setVisibility(View.INVISIBLE);
                                panah.setVisibility(View.INVISIBLE);
                            }else{
                                kembali.setVisibility(View.INVISIBLE);
                                panah.setVisibility(View.INVISIBLE);
                            }

                            nPage.setText(String.valueOf(hasil));
                            //nextPage
                            myLinearLayout.removeAllViews();
                            pages.document(documentId)
                                    .collection("pages")
                                    .startAfter(lastvisible)
                                    .limit(1)
                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    //title
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        String title = (String) documentSnapshot.get("pageTitle");
                                        qtitle.setText(title);
                                        idPages = documentSnapshot.getId();
                                        Log.d("idclick", idPages + " title " + title);

                                    }
                                    lastvisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                    showcontent();
                                    progress.dismiss();
                                }
                            });


                        }
                    progress.dismiss();
                    }
        });
    }

    private void buttonkembali() {
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                myLinearLayout.removeAllViews();
//                nPage.setText(String.valueOf(Integer.valueOf(hasil - 1)));
//                pages.document(documentId)
//                        .collection("pages")
//                        .document(idPagesBefore)
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                String title = (String) task.getResult().get("pageTitle");
//                                qtitle.setText(title);
//                                idPages = task.getResult().getId();
//                                Log.d("testgettask", idPages);
//
//                                showcontent();
//                            }
//                        });

            }
        });

    }

    private void actionPopup() {
        Log.d("inPopup","yes");
        //popup menu
        final PopupMenu popupMenu = new PopupMenu(InspeksiKetiga.this, Description);
        //add menu items in popup menu
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Tambah Catatan"); //parm 2 is menu id, param 3 is position of this menu item in menu items list, param 4 is title of the menu
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Tambah Foto");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Tambah Tindakan");

        //handle menu item clicks
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //get id of the clicked item
                int id = menuItem.getItemId();
                //handle clicks
                if (id == 0) {
                    tambahcatatan();
                    //Copy clicked
                    //set text
                    //selectedTv.setText("Copy clicked");
                } else if (id == 1) {
                    ambilfoto();
                    //Share clicked
                    //set text
                    // selectedTv.setText("Share clicked");
                } else if (id == 2) {
                    tindakan();
                    //Save clicked
                    //set text
                    //selectedTv.setText("Save clicked");
                }

                return false;
            }
        });
        //handle button click, show popup menu
        Description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();

            }
        });
    }

    private void actionPopupSection() {

        //popup menu
        final PopupMenu popupMenu2 = new PopupMenu(InspeksiKetiga.this, DescriptionSection);
        //add menu items in popup menu
        popupMenu2.getMenu().add(Menu.NONE, 0, 0, "Tambah Catatan"); //parm 2 is menu id, param 3 is position of this menu item in menu items list, param 4 is title of the menu
        popupMenu2.getMenu().add(Menu.NONE, 1, 1, "Tambah Foto");
        popupMenu2.getMenu().add(Menu.NONE, 2, 2, "Tambah Tindakan");

        //handle menu item clicks
        popupMenu2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //get id of the clicked item
                int id = menuItem.getItemId();
                //handle clicks
                if (id == 0) {

                    tambahcatatanSection();
                    //Copy clicked
                    //set text
                    //selectedTv.setText("Copy clicked");
                } else if (id == 1) {
                    ambilfotoSection();
                    //Share clicked
                    //set text
                    // selectedTv.setText("Share clicked");
                } else if (id == 2) {
                    tindakanSection();
                    //Save clicked
                    //set text
                    //selectedTv.setText("Save clicked");
                }

                return false;
            }
        });
        //handle button click, show popup menu
        DescriptionSection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idDesclick = DescriptionSection.getTag(R.id.idClick).toString();
                parentSection = DescriptionSection.getTag(R.id.parentSection).toString();
                Log.d("idDesc",idDesclick+" parent : "+parentSection);
                popupMenu2.show();
            }
        });
    }

    private void tindakan() {
        dialog = new AlertDialog.Builder(InspeksiKetiga.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.tindakan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah Tindakan");

        EditText eTitle = (EditText) dialogView.findViewById(R.id.teditText1);
        EditText eDeskripsi = (EditText) dialogView.findViewById(R.id.teditText2);
        EditText eTeam = (EditText) dialogView.findViewById(R.id.teditText3);

        RadioButton low = dialogView.findViewById(R.id.low);
        low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusTindakan = "low";
                }
            }
        });
        RadioButton medium = dialogView.findViewById(R.id.medium);
        medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusTindakan = "medium";
                }
            }
        });
        RadioButton hight = dialogView.findViewById(R.id.hight);
        hight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusTindakan = "high";
                    Log.d("teststatus","high");
                }

            }
        });

        dialog.setPositiveButton("Tambah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        FieldValue itgl = FieldValue.serverTimestamp();
                        String title = eTitle.getText().toString();
                        String deskripsi = eDeskripsi.getText().toString();
                        String team = eTeam.getText().toString();

                        Map<String, Object> tugasTemplate = new HashMap<>();
                        tugasTemplate.put("titleTugas",title);
                        tugasTemplate.put("deskripsi",deskripsi);
                        tugasTemplate.put("teamTugas",team);
                        tugasTemplate.put("inspectionsId",documentId);
                        tugasTemplate.put("pagesId",idPages);
                        tugasTemplate.put("questionId",idDesclick);
                        tugasTemplate.put("status",statusTindakan);
                        tugasTemplate.put("questionAnswer",qAction);
                        tugasTemplate.put("timeInspection",itgl);
                        tugasTemplate.put("alamat",alamat);
                        tugasTemplate.put("statusTugas","Belum Selesai");

                        if (title.isEmpty() && deskripsi.isEmpty() && team.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        } else if (title.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        }else if (deskripsi.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        }else if (team.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        } else {
                            //addTugas
                            tugas.document()
                                    .set(tugasTemplate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (statusTindakan == "high"){

                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(admin1).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String usertoken = dataSnapshot.getValue(String.class);
                                                Log.d("usertoken",usertoken);
                                                kirimnotif(usertoken, title.toString().trim(), pesan.toString().trim());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        kirim();
                                        status = "Fail Open";
                                        statusTindakan = "";
                                        Toast.makeText(InspeksiKetiga.this, "Berhasil Menambah Tindakan", Toast.LENGTH_LONG).show();
                                    }else if (statusTindakan == "medium"){
                                        Toast.makeText(InspeksiKetiga.this, "Berhasil Menambah Tindakan", Toast.LENGTH_LONG).show();
                                        status = "Fail close";
                                        statusTindakan = "";
                                    }else{
                                        Toast.makeText(InspeksiKetiga.this, "Berhasil Menambah Tindakan", Toast.LENGTH_LONG).show();
                                        status = "Fail Open";
                                        statusTindakan = "";
                                    }

                                }
                            });
                        }
                    }
                });

        dialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        statusTindakan = "";
                    }
                });

        dialog.show();
    }

    private void tindakanSection() {
        dialog = new AlertDialog.Builder(InspeksiKetiga.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.tindakan, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setTitle("Tambah Tindakan");

        EditText eTitle = (EditText) dialogView.findViewById(R.id.teditText1);
        EditText eDeskripsi = (EditText) dialogView.findViewById(R.id.teditText2);
        EditText eTeam = (EditText) dialogView.findViewById(R.id.teditText3);

        RadioButton low = dialogView.findViewById(R.id.low);
        low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusTindakan = "low";
                }
            }
        });
        RadioButton medium = dialogView.findViewById(R.id.medium);
        medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusTindakan = "medium";
                }
            }
        });
        RadioButton hight = dialogView.findViewById(R.id.hight);
        hight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusTindakan = "high";
                    Log.d("teststatus","high");
                }

            }
        });

        dialog.setPositiveButton("Tambah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String title = eTitle.getText().toString();
                        String deskripsi = eDeskripsi.getText().toString();
                        String team = eTeam.getText().toString();

                        Map<String, Object> tugasTemplate = new HashMap<>();
                        tugasTemplate.put("titleTugas",title);
                        tugasTemplate.put("deskripsi",deskripsi);
                        tugasTemplate.put("teamTugas",team);
                        tugasTemplate.put("inspectionsId",documentId);
                        tugasTemplate.put("pagesId",idPages);
                        tugasTemplate.put("contentsId",parentSection);
                        tugasTemplate.put("questionId",idDesclick);
                        tugasTemplate.put("status",statusTindakan);

                        if (title.isEmpty() && deskripsi.isEmpty() && team.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        } else if (title.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        }else if (deskripsi.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        }else if (team.isEmpty()){
                            Toast.makeText(InspeksiKetiga.this, "Gagal Menambah (Data tidak lengkap)", Toast.LENGTH_LONG).show();
                        } else {
                            //addTugas
                            tugas.document()
                                    .set(tugasTemplate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (statusTindakan == "high"){
                                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(admin1).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                String usertoken = dataSnapshot.getValue(String.class);
                                                Log.d("usertoken",usertoken);
                                                kirimnotif(usertoken, title.toString().trim(), pesan.toString().trim());

                                                kirim();
                                                status = "Fail Open";
                                                statusTindakan = "";
                                                Toast.makeText(InspeksiKetiga.this, "Berhasil Menambah Tindakan", Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }else if (statusTindakan == "medium"){
                                    Toast.makeText(InspeksiKetiga.this, "Berhasil Menambah Tindakan", Toast.LENGTH_LONG).show();
                                    status = "Fail close";
                                    statusTindakan = "";
                                    }else{
                                    Toast.makeText(InspeksiKetiga.this, "Berhasil Menambah Tindakan", Toast.LENGTH_LONG).show();
                                    status = "Fail Open";
                                    statusTindakan = "";
                                }

                                }
                            });
                        }
                    }
                });

        dialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        statusTindakan = "";
                    }
                });

        dialog.show();
    }

    public void kirimnotif(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Snackbar.make(findViewById(R.id.inspeksiawal),"Berhasil Mengirim Tindakan",Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    public  void kirim(){
        String email = "septian@batamindo.co.id";
        String subject = "[Safety Inspector] high risk finding";
        String message = "Hi " + email + "\n"+
                         "You have 1 finding high risk please click this link below " + "\n" +
                         "\n" +
                         "https://batamindo-iauditor-dev.web.app/dashboard/home";

        String mEmail = email.toString();
        String mSubject = subject.toString();
        String mMessage = message.toString();

        JavaMailAPI javaMailAPI = new JavaMailAPI(InspeksiKetiga.this, mEmail, mSubject, mMessage);
        javaMailAPI.execute();

//        Snackbar.make(findViewById(R.id.inspeksiawal),"Berhasil Mengirim Tindakan",Snackbar.LENGTH_LONG).show();
    }

    void tambahcatatan() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspeksiKetiga.this);
        alertDialog.setTitle("Tambah Catatan");

        final EditText catatan = new EditText(InspeksiKetiga.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        catatan.setLayoutParams(lp);
        alertDialog.setView(catatan);

        alertDialog.setPositiveButton("Tambah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String getcatatan = catatan.getText().toString();
                        pages.document(documentId)
                                .collection("pages")
                                .document(idPages)
                                .collection("contents")
                                .document(idDesclick)
                                .update("catatan", getcatatan).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Catatan", Toast.LENGTH_LONG).show();
                            }
                        });

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

    void tambahcatatanSection() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspeksiKetiga.this);
        alertDialog.setTitle("Tambah Catatan");

        final EditText catatan = new EditText(InspeksiKetiga.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        catatan.setLayoutParams(lp);
        alertDialog.setView(catatan);

        alertDialog.setPositiveButton("Tambah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String getcatatan = catatan.getText().toString();
                        pages.document(documentId)
                                .collection("pages")
                                .document(idPages)
                                .collection("contents")
                                .document(parentSection)
                                .collection("contents")
                                .document(idDesclick)
                                .update("catatan", getcatatan).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Catatan", Toast.LENGTH_LONG).show();
                            }
                        });

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

    void ttd() {
        progress.dismiss();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(InspeksiKetiga.this);
        alertDialog.setTitle("Tanda Tangan");

        mSignaturePad = new SignatureView(InspeksiKetiga.this, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mSignaturePad.setLayoutParams(lp);


        alertDialog.setView(mSignaturePad);

        alertDialog.setPositiveButton("Selesai",
                new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {


                        //uploadTtd
                        if (ContextCompat.checkSelfPermission(
                                getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(InspeksiKetiga.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    REQUEST_CODE_STORAGE_PERMISSION);
                        } else {
                            Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();

                            if (addJpgSignatureToGallery(signatureBitmap)) {
                                Log.d("ttd", "masuknih");
                                UploadSignatureToCloudStore(signatureBitmap);

                                //updatestatus
                                if (status == null) {
                                    pages.document(documentId)
                                            .update("status", "Pass",
                                                    "signature", ttd);
                                }else {
                                    pages.document(documentId)
                                            .update("status", status,
                                                    "signature", ttd);
                                }
                                Intent selesai = new Intent(InspeksiKetiga.this, InspeksiSelesai.class);
                                startActivity(selesai);
                                finish();
                            }
                        }



                    }
                });

        alertDialog.setNegativeButton("Batal",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        progress.dismiss();
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    private void ambilfoto() {

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


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
    private void ambilfotoSection() {
        inSection = "ambilfotoSection";
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


    }

    private void UploadSignatureToCloudStore(Bitmap signatureBitmap) {
        final String namefile = String.format("Signature_%d.jpg", System.currentTimeMillis());
        ttd = namefile;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainImagesRef = storageRef.child("Signature/" + namefile);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        String namefile = String.format("Signature_%d.jpg", System.currentTimeMillis());

        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), namefile);
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
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
        InspeksiKetiga.this.sendBroadcast(mediaScanIntent);
    }

//    /**
//     * Checks if the app has permission to write to device storage
//     * <p/>
//     * If the app does not has permission then the user will be prompted to grant permissions
//     *
//     * @param activity the activity from which permissions are checked
//     */
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            // Show pop up window
            LayoutInflater layoutInflater = LayoutInflater.from(InspeksiKetiga.this);
            View promptView = layoutInflater.inflate(R.layout.hasilfoto, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InspeksiKetiga.this);
            alertDialogBuilder.setTitle("Tambah Foto");
            alertDialogBuilder.setView(promptView);
            imageView = (ImageView) promptView.findViewById(R.id.gambar1);
            Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photoBitmap);
            Log.d("testPhoto",photoBitmap.toString());

            alertDialogBuilder.setPositiveButton("Tambah",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            if (addJpgPhotoToGallery(photoBitmap)) {
                                Log.d("photoAdd", "masuknih");
                                UploadPhotoToCloudStore(photoBitmap);

                                if (inSection == "ambilfotoSection"){
                                    pages.document(documentId)
                                            .collection("pages")
                                            .document(idPages)
                                            .collection("contents")
                                            .document(parentSection)
                                            .collection("contents")
                                            .document(idDesclick)
                                            .update("photo", sPhoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Foto", Toast.LENGTH_LONG).show();
                                            inSection = "";
                                        }
                                    });

                                }else{
                                    pages.document(documentId)
                                            .collection("pages")
                                            .document(idPages)
                                            .collection("contents")
                                            .document(idDesclick)
                                            .update("photo", sPhoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Foto", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("Batal",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            inSection = "";
                            dialog.cancel();
                        }
                    });

            alertDialogBuilder.show();
        }

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                // Show pop up window
                LayoutInflater layoutInflater = LayoutInflater.from(InspeksiKetiga.this);
                View promptView = layoutInflater.inflate(R.layout.recyclerviewimage, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InspeksiKetiga.this);
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

                    customAdapter = new CustomAdapter(InspeksiKetiga.this, modalClassList);
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
                            Toast.makeText(InspeksiKetiga.this, "Fail" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                alertDialogBuilder.setPositiveButton("Selesai",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (inSection == "ambilfotoSection") {
                                    pages.document(documentId)
                                            .collection("pages")
                                            .document(idPages)
                                            .collection("contents")
                                            .document(parentSection)
                                            .collection("contents")
                                            .document(idDesclick)
                                            .update("photo", listImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Foto", Toast.LENGTH_LONG).show();
                                            inSection = "";
                                        }
                                    });

                                } else {
                                    pages.document(documentId)
                                            .collection("pages")
                                            .document(idPages)
                                            .collection("contents")
                                            .document(idDesclick)
                                            .update("photo", listImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Foto", Toast.LENGTH_LONG).show();
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

//                Toast.makeText(this, "Multiple", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null) {

                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
//                selectedImage.setImageURI(contentUri);
//                uploadImageToFirebase(imageFileName, contentUri);

                // Show pop up window
                LayoutInflater layoutInflater = LayoutInflater.from(InspeksiKetiga.this);
                View promptView = layoutInflater.inflate(R.layout.hasilfoto, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(InspeksiKetiga.this);
                alertDialogBuilder.setTitle("Tambah Foto");
                alertDialogBuilder.setView(promptView);
                imageView = (ImageView) promptView.findViewById(R.id.gambar1);
//                Bitmap photoBitmap = (Bitmap) data.getExtras().get("data");
                imageView.setImageURI(contentUri);
                Log.d("testPhoto", imageFileName.toString());

                alertDialogBuilder.setPositiveButton("Tambah",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                uploadImageToFirebase(imageFileName, contentUri);
                                if (inSection == "ambilfotoSection") {
                                    pages.document(documentId)
                                            .collection("pages")
                                            .document(idPages)
                                            .collection("contents")
                                            .document(parentSection)
                                            .collection("contents")
                                            .document(idDesclick)
                                            .update("photo", txtfoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Foto", Toast.LENGTH_LONG).show();
                                            inSection = "";
                                        }
                                    });

                                } else {
                                    pages.document(documentId)
                                            .collection("pages")
                                            .document(idPages)
                                            .collection("contents")
                                            .document(idDesclick)
                                            .update("photo", txtfoto).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(InspeksiKetiga.this, "Berhasil Menambahkan Foto", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }

                        });

                alertDialogBuilder.setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                inSection = "";
                                dialog.cancel();
                            }
                        });

                alertDialogBuilder.show();
            }
        }
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
//        if(result){
//            includesForUploadFiles(namefile);
//        }
        return result;
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
                Toast.makeText(InspeksiKetiga.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            Snackbar.make(findViewById(R.id.inspeksiketiga),"Inspeksi sedang berjalan anda tidak bisa kembali sesuka hati !",Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        inSection = "";
        statusTindakan = "";
        super.onBackPressed();
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
