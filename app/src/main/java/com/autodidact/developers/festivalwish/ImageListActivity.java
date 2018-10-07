package com.autodidact.developers.festivalwish;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ImageListActivity extends AppCompatActivity {
    public static StorageReference mStorageRef;
    public List<ImageUpload> imgList;
    public RecyclerView vertical_recycler_view;
    String answer1;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private DatabaseReference mDatabaseRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Log.e(TAG, user.toString());
        } else {
            mAuth.signInAnonymously();
        }


        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setContentView(R.layout.image_list);
            vertical_recycler_view = findViewById(R.id.RecyclerViewImageList);
            imgList = new ArrayList<>();

            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait For A While..");
            progressDialog.show();


            Bundle bundle = getIntent().getExtras();

            String FB_STORAGE_PATH = bundle.getString("DBPATH");

            String FB_DATABASE_PATH = bundle.getString("DBPATH");
            mStorageRef = FirebaseStorage.getInstance().getReference(FB_STORAGE_PATH);
            mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
            //StorageRef = FirebaseStorage.getInstance().getReference();
            mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Fetch image data from firebase database
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //ImageUpload class require default constructor
                        ImageUpload img = snapshot.getValue(ImageUpload.class);
                        imgList.add(img);

                    }
                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(ImageListActivity.this, imgList);
                    LinearLayoutManager LayoutManager = new LinearLayoutManager(ImageListActivity.this, LinearLayoutManager.VERTICAL, false);
                    LayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    vertical_recycler_view.setLayoutManager(LayoutManager);
                    vertical_recycler_view.setAdapter(recyclerAdapter);
                    progressDialog.dismiss();

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Cancelled");
                }
            });


        } else {
            answer1 = "No internet Connectivity";
            Toast.makeText(getApplicationContext(), answer1, Toast.LENGTH_LONG).show();

            Intent j = new Intent(this, NoConnection.class);
            startActivity(j);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }
}

