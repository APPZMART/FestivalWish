package com.autodidact.developers.festivalwish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    public static int permission = 0;
    public long iterator = 0;
    public List<ImageUpload> mainimgList;
    public List UpdateCheckerList = new ArrayList<>();
    public String status = "false";
    public String currentVersion = "1.6";
    public String updateUrl = "https://play.google.com/store/apps/details?id=com.autodidact.developers.festivalwish";
    MyRecyclerViewAdapter adapter;
    String answer;
    ImageView image;
    int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    RecyclerView recyclerView;
    File storageDir = null;
    private AdView mAdView;
    private DatabaseReference DatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {

            setContentView(R.layout.activity_main);

            MobileAds.initialize(this, "ca-app-pub-6574435909968892~2458260150");
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            mainimgList = new ArrayList<>();
            DatabaseRef = FirebaseDatabase.getInstance().getReference("Update Checker/");

            DatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    iterator = dataSnapshot.getChildrenCount();
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //ImageUpload class require default constructor
                        String val = snapshot.getValue().toString();
                        UpdateCheckerList.add(val);

                    }
                    status = UpdateCheckerList.get(0).toString();
                    if (status == "true") {
                        currentVersion = UpdateCheckerList.get(1).toString();
                        String appVersion = getAppVersion(MainActivity.this);
                        updateUrl = UpdateCheckerList.get(2).toString();

                        if (!TextUtils.equals(currentVersion, appVersion)) {
                            onUpdateNeeded(updateUrl);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("Cancelled");
                }
            });

            // Read from the database
            DatabaseRef = FirebaseDatabase.getInstance().getReference("MainList/");

            DatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    iterator = dataSnapshot.getChildrenCount();
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //ImageUpload class require default constructor
                        ImageUpload img = snapshot.getValue(ImageUpload.class);
                        mainimgList.add(img);
                    }

                    // data to populate the RecyclerView with

                    Calendar c;
                    c = Calendar.getInstance();
                    int month = c.get(Calendar.MONTH);

                    image = findViewById(R.id.month_image);
                    if (month == 0) {
                        image.setImageResource(R.drawable.january);
                    }
                    if (month == 1) {
                        image.setImageResource(R.drawable.feburary);
                    }
                    if (month == 2) {
                        image.setImageResource(R.drawable.march);
                    }
                    if (month == 3) {
                        image.setImageResource(R.drawable.april);
                    }
                    if (month == 4) {
                        image.setImageResource(R.drawable.may);
                    }
                    if (month == 5) {
                        image.setImageResource(R.drawable.june);
                    }
                    if (month == 6) {
                        image.setImageResource(R.drawable.july);
                    }
                    if (month == 7) {
                        image.setImageResource(R.drawable.august);
                    }
                    if (month == 8) {
                        image.setImageResource(R.drawable.september);
                    }
                    if (month == 9) {
                        image.setImageResource(R.drawable.october);
                    }
                    if (month == 10) {
                        image.setImageResource(R.drawable.november);
                    }
                    if (month == 11) {
                        image.setImageResource(R.drawable.december);
                    }
                    recyclerView = findViewById(R.id.rvNumbers);
                    int numberOfColumns = 2;
                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), numberOfColumns));
                    adapter = new MyRecyclerViewAdapter(getApplicationContext(), mainimgList);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.out.println("Cancelled");
                }

            });
            recyclerView = findViewById(R.id.rvNumbers);
            int numberOfColumns = 2;
            recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), numberOfColumns));
            adapter = new MyRecyclerViewAdapter(getApplicationContext(), mainimgList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            answer = "No internet Connectivity";
            Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
            Intent j = new Intent(MainActivity.this, NoConnection.class);
            startActivity(j);
            finish();
        }
        storagefile();
        // set up the RecyclerView
    }

    private String getAppVersion(Context context) {
        String result = "";
        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public void storagefile() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permission = 1;
            } else {
                requestPermission(this);
            }
        }
    }

    private void requestPermission(final Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                final String appPackageName = BuildConfig.APPLICATION_ID;
                final String appName = this.getString(R.string.app_name);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "https://play.google.com/store/apps/details?id=" +
                        appPackageName;
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "appName");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
                return true;

            case R.id.rateus:
                final String appPackageName1 = BuildConfig.APPLICATION_ID;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + appPackageName1));
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(View view, int position) {

        Intent i = new Intent(MainActivity.this, ImageListActivity.class);
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
        int x = position;
        String text = mainimgList.get(x).getName();
        Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position + text);
        i.putExtra("DBPATH", text);
        startActivity(i);
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();

    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}




