package com.raamaangroup.stepMeter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

public class Main extends AppCompatActivity {


    SensorManager sensorManager;
    TextView steps_text;
    TextView coin_text;
    TextView distance_text;
    TextView calories_text;
    TextView pastry_text;
    TextView time_text;

    private PieModel sliceGoal, sliceCurrent;
    private PieChart pg;

    private int coin_counter = 0;
    private int min_counter = 0;

    // Menu
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        steps_text = findViewById(R.id.steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        coin_text = findViewById(R.id.coinsText);
        distance_text = findViewById(R.id.distanceTextView);
        calories_text = findViewById(R.id.caloriesTextView);
        pastry_text = findViewById(R.id.pastryCounterTextView);
        time_text = findViewById(R.id.timeTextView);

        pg = (PieChart) findViewById(R.id.graph);

        // slice for the steps taken today
        sliceCurrent = new PieModel("", 100, Color.parseColor("#99CC00"));
        pg.addPieSlice(sliceCurrent);

        // slice for the "missing" steps until reaching the goal
//        sliceGoal = new PieModel("", 100, Color.parseColor("#CC0000"));
//        pg.addPieSlice(sliceGoal);

        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
//                showSteps = !showSteps;
//                stepsDistanceChanged();
            }
        });

        pg.setDrawValueInPie(false);
        pg.setUsePieRotation(true);
        pg.startAnimation();


        final LocationListener mLocationListener = new LocationListener() {
            int start = 0;
            Location lastLocation;

            @Override
            public void onLocationChanged(final Location location) {
                //your code here
                Toast.makeText(Main.this, String.valueOf(location.getAccuracy()), Toast.LENGTH_SHORT).show();
                if (start >= 1 && location.distanceTo(lastLocation) >= location.getAccuracy()+1.5) { //15

                    Toast.makeText(Main.this, "Change", Toast.LENGTH_SHORT).show();
                    steps_text.setText(String.valueOf((Double.parseDouble(steps_text.getText().toString()) + (int)(location.getAccuracy()*3.35))));
                    lastLocation = location;
                    coin_counter += (int)(location.getAccuracy()*3.35);
                    if(coin_counter >= 100) {
                        coin_text.setText(String.valueOf(Integer.parseInt(coin_text.getText().toString()) + 1));
                        coin_counter -= 100;
                    }

                    distance_text.setText(String.valueOf(Double.parseDouble(distance_text.getText().toString()) + ((location.getAccuracy()*3.35) * 0.805)));
                    calories_text.setText(String.valueOf(Double.parseDouble(calories_text.getText().toString()) + ((location.getAccuracy()*3.35) * 0.056)));
                    pastry_text.setText(String.valueOf(Double.parseDouble(pastry_text.getText().toString()) + (Double.parseDouble(calories_text.getText().toString())/250) ));

                    min_counter += (int)(location.getAccuracy()*3.35);
                    if(min_counter >= 60) {
                        time_text.setText(String.valueOf(Integer.parseInt(time_text.getText().toString()) + 1 ));
                        min_counter -= 60;
                    }
                } else {
                    Toast.makeText(Main.this, "NOT MOVING", Toast.LENGTH_SHORT).show();
                }

                if (start < 1) {
                    lastLocation = location;
                }
                start++;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "permission denied golam!", Toast.LENGTH_SHORT).show();
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200,
                2, mLocationListener);



        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // ...From section above...
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        Button button = (Button) findViewById(R.id.menu_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
    }

    public void openDrawer(){
        mDrawer.openDrawer(GravityCompat.START);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
//        Fragment fragment = null;
//        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.account:
//                Intent i = new Intent(Main.this, MyAccount.class);
//                startActivity(i);
//                finish();
                break;
            case R.id.support:
//                Intent i2 = new Intent(Main.this, Support.class);
//                startActivity(i2);
//                finish();
                break;
            case R.id.info:
//                Intent i3 = new Intent(Main.this, Info.class);
//                startActivity(i3);
//                finish();
                break;
            case R.id.settings:
//                Intent i4 = new Intent(Main.this, Setting.class);
//                startActivity(i4);
//                finish();
                break;
        }

//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.flContent,fragment ).commit();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Insert the fragment by replacing any existing fragment
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


        // Highlight the selected item has been done by NavigationView
//        menuItem.setChecked(true);

        // Set action bar title
//        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }

}