package com.leeddev.digitalspeedometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leeddev.digitalspeedometer.DBUtils.DbHelper;
import com.leeddev.digitalspeedometer.HistoryUtils.HistoryActivity;
import com.leeddev.digitalspeedometer.Model.HistoryLocationModel;
import com.leeddev.digitalspeedometer.Utils.AppUtils;


public class MainActivity extends AppCompatActivity implements LocationListener {
    public DrawerLayout drawerLayout;
    public DbHelper dbhelper;
    RelativeLayout btnHistory;
    RelativeLayout btn_getStarted;
    public AppUtils appUtils;
    public ForeGroundService foreGroundService;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    RelativeLayout rateUs;
    Location loc;
    double latitude;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 ;
    protected LocationManager locationManager;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        btnHistory = findViewById(R.id.btn_get_history);
        btn_getStarted = findViewById(R.id.btn_get_started);
        rateUs = findViewById(R.id.btn_rate_us);
        dbhelper = new DbHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        btnHistory.setOnClickListener(view -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
        btn_getStarted.setOnClickListener(view -> {

        });
        sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            loc = locationManager
                    .getLastKnownLocation(LocationManager.FUSED_PROVIDER);
            if (loc != null) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                editor.putString("old location", "" + loc.getLongitude() + "" + loc.getLatitude());
            }
        }
        Toast.makeText(this, "location is " + getLocation(), Toast.LENGTH_SHORT).show();
//////////Dialog box OverSpeeding/////////
        OverSpeedDialogClass alert = new OverSpeedDialogClass();
        alert.showDialog(this);
        dbhelper.insertContact(new HistoryLocationModel("", "10",
                "1KM/H", "0KM", "20KM/H"));
        dbhelper.insertContact(new HistoryLocationModel("", "0",
                "9KM/H", "10KM", "2KM/H"));
        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
     appUtils.calculateTime(this);
        editor.putString("speed", "" + location.getSpeed());
        editor.putString("latitude + long", location.getLongitude() + " -- " + location.getLatitude());
        editor.apply();
        HistoryLocationModel model = new HistoryLocationModel("" +AppUtils.dateFormate(this), "" + location.getLatitude(),
                "" + location.getLongitude(), "", "" + location.getSpeed());
        dbhelper.insertContact(model);

    }


//Intent serviceIntent=new Intent(this,ForeGroundService.class);
//
//    @Nullable
//    @Override
//    public ComponentName startForegroundService(Intent service) {
//        return super.startForegroundService(service);
//    }

    private Location getLocation() {
        try {

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!checkGPS && !checkNetwork) {
                Toast.makeText(this, "No Service Provider is available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {
                    if (ActivityCompat.checkSelfPermission(this
                            , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.FUSED_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.FUSED_PROVIDER);
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                            Toast.makeText(this, "latitude is"+loc.getLatitude()+"and longitude is"+loc.getLongitude()+"speed is"+loc.getSpeed(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loc;
    }
//     private String getSpeed()
//{
//     //(onLocationChanged()-getLocation())/total_time;
//
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

