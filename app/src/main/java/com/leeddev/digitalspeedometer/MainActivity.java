package com.leeddev.digitalspeedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.leeddev.digitalspeedometer.DBUtils.DbHelper;
import com.leeddev.digitalspeedometer.HistoryUtils.HistoryActivity;
import com.leeddev.digitalspeedometer.Model.HistoryLocationModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public DrawerLayout drawerLayout;
    public DbHelper dbhelper;
    RelativeLayout btn_getStarted;
    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;
    Location loc;
    double latitude;
    SharedPreferences sharedPref ;
    SharedPreferences.Editor editor;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60;
    protected LocationManager locationManager;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        btn_getStarted = findViewById(R.id.btn_get_started);
        dbhelper = new DbHelper(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        btn_getStarted.setOnClickListener(view -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });
        sharedPref=getPreferences(Context.MODE_PRIVATE);
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
                editor.putString("old location", ""+loc.getLongitude()+ ""+loc.getLatitude());
            }
        }
        Toast.makeText(this, "location is "+getLocation(), Toast.LENGTH_SHORT).show();

     //   OverSpeedDialogClass alert = new OverSpeedDialogClass();
       // alert.showDialog(getActivity(),"You Are over Speeding");
//        AlertDialog alertDialog = new AlertDialog.Builder(this)
////set icon
//                .setIcon(R.drawable.error_icon)
////set title
//                .setTitle("You are Overspeeding Slow Down!")
////set message
//                .setMessage("Exiting will call finish() method")
////set positive button
//                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //set what would happen when positive button is clicked
//                        finish();
//                    }
//                })
////set negative button
//                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        //set what should happen when negative button is clicked
//                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
//                    }
//                })
//                .show();
        dbhelper.insertContact(new HistoryLocationModel("14/10/22","10",
                "1KM/H", "0KM","20KM/H"));
        dbhelper.insertContact(new HistoryLocationModel("12/10/22","0",
                "9KM/H", "10KM","2KM/H"));
     //   dbhelper.insertContact(new HistoryLocationModel("13","20km/h", "1KM/H", "0KM","40km/H"));
   //     dbhelper.insertContact(new HistoryLocationModel("14","30km/h", "1KM/H", "0KM","50km/H"));

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
        Toast.makeText(this, ""+location.getSpeed(), Toast.LENGTH_LONG).show();

        editor.putString("speed", ""+location.getSpeed());
        editor.putString("latitude + long", location.getLongitude()+" -- "+ location.getLatitude());
        editor.apply();
        HistoryLocationModel model=new HistoryLocationModel("",""+location.getLatitude(),
                ""+location.getLongitude(),"",""+location.getSpeed());
        dbhelper.insertContact(model);
    }
    private void dateFormate() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = df.format(c);
    }
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
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loc;
    }

            @Override
            public boolean onOptionsItemSelected(@NonNull MenuItem item) {

                if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }
                return super.onOptionsItemSelected(item);
            }
        }

