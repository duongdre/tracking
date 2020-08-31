package com.example.dss.project.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dss.R;
import com.example.dss.project.Fragment.ViewHistory;
import com.example.dss.project.Models.Car;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.maps.android.ui.IconGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static android.graphics.Color.parseColor;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private int ACCESS_LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Marker userLocationMarker, locationMarker;
    Circle userLocationCircle, locationCircle;

    //For Draw the Line
    int k = 0, speedSet = 0;
    Long l = 730l;
    String speedColor = "#000000";
    List<Polyline> listPolylines = new ArrayList<Polyline>();
    List<Marker> listMarkers = new ArrayList<Marker>();
    Polyline polyline = null;
    String dateFrom, timeFrom, dateTo, timeTo, carFromChosing, passingCarSign, stringSpeed, stringShowDirection;
    double passingCarLat, passingCarLog, passingMyLat, passingMyLog;
    String showSpeedBar;
    Handler handler = new Handler();
    Random r = new Random();
    float random;

    //For Searching and Viewing
    ImageView imageView;
    TextView numberSign, time, status, business, direction, speed, kmPerDay, fuel, maxFuel, dateAndTimeRun, speedRun, locationRun;
    ListView listView;
    List<Car> listItems = new ArrayList<>();
    List<Car> listItemsDemo = new ArrayList<>();
    List<Car> listDrawItems = new ArrayList<>();
    List<Car> listSelectedItems = new ArrayList<>();
    Car previousCar, nextCar;
    CustomAdapter customAdapter;
    SearchView filTer;
    LinearLayout searchAndView, linearSeekBar;

    SeekBar speedSeekBar;
    boolean isViewHistoryOpen = false, isRunning = false;

    //Demo Data
    Date currenttime;
    String pattern = "MM/dd/yyyy HH:mm:ss";
    DateFormat df = new SimpleDateFormat(pattern);
    double listDemoLa[] = new double[50];
    double listDemoLo[] = new double[50];
    float listDemoDirection[] = new float[50];
    Date listDemoDatetime[] = new Date[50];
    double listDemoSpeed[] = new double[50];


    String listSignNumber[] = {"29L30186", "29C34697", "34C13468", "51C93465", "88A03469", "29R63134", "29L30687", "29C34876", "34C13468", "51C97425", "88A03986", "29R63784"};
    Date listDate[] = new Date[24];
    //String listTime[] = {"14:03:21","14:03:22","14:03:23","14:03:24","14:03:25","14:03:26","14:03:27","14:03:28","14:03:29","14:03:30","14:03:31","14:03:32","14:03:33","14:03:34","14:03:35","14:03:36","14:03:37","14:03:38","14:03:39","14:03:40","14:03:41","14:03:42","14:03:43","14:03:44"};
    double listLatitude[] = {20.980399, 20.990496, 20.995625, 20.989134, 20.97486, 20.988092, 20.994273, 20.983765, 20.994423, 20.991859, 20.987611, 20.997187};
    double listLongtitude[] = {105.811408, 105.813897, 105.810293, 105.805915, 105.809778, 105.799993, 105.796966, 105.803684, 105.800594, 105.798190, 105.797933, 105.804101};
    double listDrawLatitude[] = {20.998122, 20.998500, 20.998550, 20.998662, 20.998711, 20.998745, 20.998779, 20.998867, 20.998936, 20.998984, 20.999000, 20.998990, 20.998913, 20.998513, 20.998254, 20.998056, 20.997800, 20.997535, 20.997675, 20.997320, 20.997450, 20.997580, 20.997986};
    double listDrawLongtitude[] = {105.7957127, 105.795772, 105.795854, 105.796009, 105.796090, 105.796146, 105.796203, 105.796324, 105.796436, 105.796503, 105.796633, 105.796757, 105.797088, 105.797385, 105.797584, 105.797700, 105.797903, 105.797566, 105.796943, 105.796761, 105.796176, 105.795677, 105.795768};
    String listStatus[] = {"Dừng", "Chạy", "Quá tốc độ", "Đỗ", "Đỗ", "Quá tốc độ", "Chạy", "Đỗ", "Chạy", "Quá tốc độ", "Chạy", "Dừng"};
    String listBusiness[] = {"Có hàng", "Đang lấy hàng", "Có hàng", "Trả hàng", "Đang lấy hàng", "Đang lấy hàng", "Có hàng", "Đang lấy hàng", "Có hàng", "Trả hàng", "Đang lấy hàng", "Đang lấy hàng"};
    float listDirection[] = {15f, 50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f, 50f, 111f, 140f, 140f, 140f, 140f, 140f, 240f, 320f, 270f, 320f, 320f, 15f};
    double listSpeed[] = {13.2, 19.2, 20.0, 25.6, 30.5, 30.7, 25.0, 24.1, 22.6, 30.9, 30.9, 35.0, 48.2, 49.6, 51.4, 58.8, 60.2, 62.3, 63.7, 90.4, 92.4, 93.7, 94.7, 95.8};
    double listKmPerDay[] = {100, 324.3, 12.2, 351.1, 12.9, 78.6, 100, 324.3, 12.2, 351.1, 12.9, 78.6};
    double listFuel[] = {11.9, 25.0, 12.1, 5.4, 9.3, 5.5, 11.9, 25.0, 12.1, 5.4, 9.3, 5.5};
    double listMaxFuel[] = {15.0, 18.0, 15.0, 18.0, 18.0, 20.0, 15.0, 18.0, 15.0, 18.0, 18.0, 20.0};


    //Oncreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // For Frangments
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Action Bar and Set to center
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);*/
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View cView = getLayoutInflater().inflate(R.layout.align_center_action_bar, null);
        actionBar.setCustomView(cView);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);

        for (int i = 0; i < 24; i++){
            currenttime = Calendar.getInstance().getTime();
            listDate[i] = currenttime;
        }
        //Demo Data
        for (int i = 0; i < 50; i++){
            //Random
            Random r = new Random();
            //Latitude
            double randomLa = 20.0 + (21.9 - 20.0) * r.nextDouble();
            listDemoLa[i] = randomLa;
            //Longtitude
            double randomLo = 105.0 + (106.0 - 105.0) * r.nextDouble();
            listDemoLo[i] = randomLo;
            //Direction
            float randomDirection = 0 + (360 - 0) * r.nextFloat();
            listDemoDirection[i] = randomDirection;
            //Time
            currenttime = Calendar.getInstance().getTime();
            listDemoDatetime[i] = currenttime;
            //Speed
            double randomSped = 0.0 + (180.0 - 0.0) * r.nextDouble();
            listDemoSpeed[i] = randomSped;
        }
        //Demo Car
        for (int i = 0; i < 50; i++){
            Car car = new Car(listDemoLa[i], listDemoLo[i], listDemoDirection[i], listDemoDatetime[i], listDemoSpeed[i]);
            listItemsDemo.add(car);
            //listDrawItems.add(car);
            System.out.println("Created Demo Car for API Test");
        }

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currenttime2 = Calendar.getInstance().getTime();
                System.out.println("currenttime1: " + df.format(currenttime1));
                System.out.println("currenttime2: " + df.format(currenttime2));
                System.out.println(currenttime1);
                System.out.println(currenttime1.after(currenttime2));
            }
        }, 5000);
        currenttime1 = Calendar.getInstance().getTime();*/

        //For the Map
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        //For Searching and Viewing
        searchAndView = findViewById(R.id.search_and_view);
        searchAndView.setVisibility(LinearLayout.GONE);



        //For Seek Bar Speed
        linearSeekBar = findViewById(R.id.linear_seek_bar);
        linearSeekBar.setVisibility(LinearLayout.GONE);
        speedSeekBar = findViewById(R.id.seek_bar_speed);
        dateAndTimeRun = findViewById(R.id.date_and_time);
        speedRun = findViewById(R.id.carSpeed);
        locationRun = findViewById(R.id.carPlace);

        try {
            Intent intent = getIntent();
            carFromChosing = intent.getStringExtra("car");
            timeFrom = intent.getStringExtra("timeFrom");
            timeTo = intent.getStringExtra("timeTo");
            stringSpeed = intent.getStringExtra("speedCar");
            stringShowDirection =  intent.getStringExtra("chiduong");


            System.out.println("car: " + carFromChosing);
            System.out.println("timeFrom: " + timeFrom);
            System.out.println("timeTo: " + timeTo);
            System.out.println("speedCar: " + stringSpeed);
            System.out.println("chiduong: " + stringShowDirection);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (stringSpeed != null){
            linearSeekBar.setVisibility(LinearLayout.VISIBLE);
        }

        if (stringShowDirection != null){
            Toast.makeText(this, "BẮT ĐẦU ĐIỀU HƯỚNG", Toast.LENGTH_SHORT).show();
        }
    }

    //get String address from Location
    public String getAddress(double lat, double lng) {
        String add = new String();
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            /*String  currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            double latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity= obj.getSubAdminArea();
            String currentState= obj.getAdminArea();
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            //Toast.makeText(this, "Address: " + add, Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(MapsActivity.this, "HOME", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    /*startActivity(new Intent(MapsActivity.this, HomeActivity.class));*/
                    break;
                //For Draw the line
                case R.id.navigation_update:
                    Toast.makeText(MapsActivity.this, "CẬP NHẬT THÔNG TIN", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MapsActivity.this, MessageActivity.class));
                    //searchAndView.setVisibility(LinearLayout.GONE);
                    //handler.removeCallbacksAndMessages(null);
                    break;
                case R.id.navigation_map:
                    if (isRunning == false){
                        Toast.makeText(MapsActivity.this, "RUN", Toast.LENGTH_SHORT).show();
                        searchAndView.setVisibility(LinearLayout.GONE);

                        final Boolean hideMarker = new Boolean(true);
                        //For Delay drawing
                        Runnable runnablee = new Runnable() {
                            @Override
                            public void run() {
                                if (k < listDrawItems.size() - 1) {
                                    final Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(listDrawItems.get(k).getLatitude(), listDrawItems.get(k).getLongitude())).
                                            icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_driving)).
                                            rotation(listDrawItems.get(k).getDirection()).
                                            anchor((float) 0.5, (float) 0.5).
                                            title("" + listDrawItems.get(k).getSignNumber()).
                                            snippet("Thời điểm: " + listDrawItems.get(k).getDate().toString() + "\n" +
                                                    "Vĩ độ: " + listDrawItems.get(k).getLatitude() + " - Kinh độ: " + listDrawItems.get(k).getLongitude() + "\n" +
                                                    "Vận tốc: " + listDrawItems.get(k).getSpeed() + "\n" +
                                                    "Trạng thái: " + listDrawItems.get(k).getStatus() + "\n" +
                                                    "Km ngày: " + listDrawItems.get(k).getKmPerDay() + "(km)" + "\n" +
                                                    "Nhiên liệu: " + listDrawItems.get(k).getFuel() + "lít").
                                            draggable(true).
                                            visible(true));
                                    final Handler handler = new Handler();
                                    final long start = SystemClock.uptimeMillis();
                                    Projection proj = mMap.getProjection();
                                    Point startPoint = ((Projection) proj).toScreenLocation(marker.getPosition());
                                    final LatLng startLatLng = proj.fromScreenLocation(startPoint);
                                    final long duration = l;

                                    final Interpolator interpolator = new LinearInterpolator();

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            long elapsed = SystemClock.uptimeMillis() - start;
                                            float t = interpolator.getInterpolation((float) elapsed / duration);
                                            double lat = t * listDrawItems.get(k).getLatitude() + (1 - t) * startLatLng.latitude;
                                            double lng = t * listDrawItems.get(k).getLongitude() + (1 - t) * startLatLng.longitude;

                                            //mMap.addMarker(markerOptions.position(new LatLng(lat, lng)).draggable(true).visible(true));
                                            marker.setPosition(new LatLng(lat, lng));
                                            /*listDrawItems.get(k).setLatitude(lat);
                                            listDrawItems.get(k).setLongitude(lng);
                                            setLocationMarker(listDrawItems.get(k));*/
                                            CameraUpdateFactory.zoomTo(25.0f);

                                            if (t < 1.0) {
                                                // Post again 16ms later.
                                                handler.postDelayed(this, 10);
                                            } else {
                                                if (hideMarker) {
                                                    marker.setVisible(false);
                                                } else {
                                                    marker.setVisible(true);
                                                }
                                            }
                                        }
                                    });





                                    //animateMarker(mMap, listDrawItems.get(k), listDrawItems.get(k+1), hideMarker);
                                    //Draw the line
//                                    drawTheLine(listDrawItems.get(k).getLatitude(), listDrawItems.get(k).getLongitude(),
//                                            listDrawItems.get(k+1).getLatitude(), listDrawItems.get(k+1).getLongitude(),
//                                            listDrawItems.get(k+1).getSpeed());

                                    //Set text for details
                                    dateAndTimeRun.setText(listDrawItems.get(k).getDate().toString());
                                    speedRun.setText("Tốc độ: " + listDrawItems.get(k).getSpeed() + "(km/h)");
                                    locationRun.setText(getAddress(listDrawItems.get(k).getLatitude(), listDrawItems.get(k).getLongitude()));

                                    //Set marker if have a status (situasion)
                                    if (!listDrawItems.get(k).getStatus().equals("status")){
                                        LatLng situation = new LatLng(listDrawItems.get(k).getLatitude(), listDrawItems.get(k).getLongitude());
                                        IconGenerator icg = new IconGenerator(MapsActivity.this);
                                        icg.setColor(parseColor("#fff176")); // Yellow background
                                        Bitmap bmap = icg.makeIcon(listDrawItems.get(k).getStatus()); //Set Text
                                        MarkerOptions options = new MarkerOptions().position(situation).icon(BitmapDescriptorFactory.fromBitmap(bmap));
                                    /*listPolylines.add(this.mMap.addPolyline(new PolylineOptions().color(parseColor(speedColor)).width(5.0f).geodesic(true)
                                            .add(stepLatLng1).add(stepLatLng2).clickable(true)));*/
                                        Marker newSign = mMap.addMarker(options);
                                        newSign.setTitle("Chi tiết");
                                        newSign.setSnippet("Thời điểm: " + listDrawItems.get(k).getDate().toString() + "\n" +
                                                "Vĩ độ: " + listDrawItems.get(k).getLatitude() + " - Kinh độ: " + listDrawItems.get(k+1).getLongitude() + "\n" +
                                                "Vận tốc: " + listDrawItems.get(k).getSpeed() + "\n" +
                                                "Trạng thái: " + listDrawItems.get(k).getStatus() + "\n" +
                                                "Km ngày: " + listDrawItems.get(k).getKmPerDay() + "(km)" + "\n" +
                                                "Nhiên liệu: " + listDrawItems.get(k).getFuel() + "lít");
                                        listMarkers.add(newSign);
                                    }

                                    k++;
                                } else {
                                    //Clear lines
                                    if (listPolylines.size() > 0){
                                        for(Polyline line : listPolylines)
                                        {
                                            line.remove();
                                        }
                                        listPolylines.clear();
                                    }
                                    //Clear places (situations)
                                    if (listMarkers.size() > 0){
                                        for(Marker markerCheck : listMarkers)
                                        {
                                            markerCheck.remove();
                                        }
                                        listMarkers.clear();
                                    }
                                    //move back
                                    //setLocationMarker(listDrawItems.get(0));
                                    CameraUpdateFactory.zoomTo(25.0f);

                                    //Set text for detals
                                    dateAndTimeRun.setText(listDrawItems.get(k).getDate().toString());
                                    locationRun.setText(getAddress(listDrawItems.get(k).getLatitude(), listDrawItems.get(k).getLongitude()));
                                    k = 0;
                                }
                                speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                        speedSet = i;
                                        l = Long.valueOf(2000 / (i+1));
                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {

                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }
                                });
                                handler.postDelayed(this, l);
                                //System.out.println("L: " + l);
                            }
                        };
                        handler.post(runnablee);
                        isRunning = true;
                    }else{
                        Toast.makeText(MapsActivity.this, "STOP", Toast.LENGTH_SHORT).show();
                        handler.removeCallbacksAndMessages(null);
                        isRunning = false;
                    }
                    break;
                case R.id.navigation_search:
                    Toast.makeText(MapsActivity.this, "TÌM XE", Toast.LENGTH_SHORT).show();
                    searchAndView.setVisibility(LinearLayout.VISIBLE);
                    filTer = findViewById(R.id.search_filter);
                    listView = findViewById(R.id.listView);

                    if (listItems.size() == 0) {
                        for (int i = 0; i < listSignNumber.length; i++) {
                            Car car = new Car(listSignNumber[i], listDate[i], listLatitude[i], listLongtitude[i], listStatus[i], listBusiness[i], random = 0 + r.nextFloat() * (360 - 0), listSpeed[i], listKmPerDay[i], listFuel[i], listMaxFuel[i]);
                            listItems.add(car);
                        }

                        for (int i = listSignNumber.length; i < listLongtitude.length; i++) {
                            Car car = new Car(listLatitude[i], listLongtitude[i], random = 0 + r.nextFloat() * (360 - 0));
                            listItems.add(car);
                        }
                    }

                    customAdapter = new CustomAdapter(listItems);
                    listView.setAdapter(customAdapter);

                    filTer.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            customAdapter.getFilter().filter(s);
                            System.out.println("Filled");
                            return true;
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            //Chose then Back to Map
                            filTer.setQuery("", false);
                            filTer.clearFocus();
                            previousCar = customAdapter.itemsModelListFiltered.get(position);
                            listSelectedItems.clear();
                            listSelectedItems.add(previousCar);
                            Toast.makeText(MapsActivity.this, "Đã chọn xe: " + previousCar.getSignNumber(), Toast.LENGTH_SHORT).show();
                            searchAndView.setVisibility(LinearLayout.GONE);

                            //Move camera back to selected Car's position
                            //setLocationMarker(previousCar);
                            //mMap.animateCamera(CameraUpdateFactory, );
                            LatLng latLng = new LatLng(previousCar.getLatitude(), previousCar.getLongitude());
                            MarkerOptions mark = new MarkerOptions();
                            if (previousCar.getStatus().equals("Dừng")){
                                mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_stop));
                            }else{
                                if (previousCar.getStatus().equals("Đỗ")){
                                    mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_parking));
                                }else{
                                    if (previousCar.getStatus().equals("Chạy")){
                                        mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_driving));
                                    }else{
                                        mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
                                    }
                                }
                            }
                            mark.title("" + previousCar.getSignNumber());
                            mark.snippet("Thời điểm: " + previousCar.getDate().toString() + "\n" +
                                    "Vĩ độ: " + previousCar.getLatitude() + " - Kinh độ: " + previousCar.getLongitude() + "\n" +
                                    "Vận tốc: " + previousCar.getSpeed() + "\n" +
                                    "Trạng thái: " + previousCar.getStatus() + "\n" +
                                    "Km ngày: " + previousCar.getKmPerDay() + "(km)" + "\n" +
                                    "Nhiên liệu: " + previousCar.getFuel() + "lít");

                            mark.rotation(previousCar.getDirection());
                            mark.anchor((float) 0.5, (float) 0.5);

                            mark.position(latLng);
                            Marker addMarker = mMap.addMarker(mark);
                            addMarker.showInfoWindow();
                            mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(latLng, 20.0f ));

                            System.out.println("BACK TO MAP");
                        }
                    });
                    break;
            }
            return true;
        }
    };

    public String getChosingCarSign(){
        return passingCarSign;
    }

    public double getChosingCarLat(){
        return passingCarLat;
    }

    public double getChosingCarLog(){
        return passingCarLog;
    }

    public double getMyLat(){
        return passingMyLat;
    }

    public double getMyLog(){
        return passingMyLog;
    }

    //Custom Adapter for View
    public class CustomAdapter extends BaseAdapter implements Filterable {

        private List<Car> itemsModelList;
        private List<Car> itemsModelListFiltered;

        public CustomAdapter(List<Car> itemsModelList) {
            this.itemsModelList = itemsModelList;
            this.itemsModelListFiltered = itemsModelList;
        }

        @Override
        public int getCount() {
            return itemsModelListFiltered.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //Get View for Adapter
        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_car, null);
            imageView = view.findViewById(R.id.image_View);
            numberSign = view.findViewById(R.id.item_sign_number);
            time = view.findViewById(R.id.item_time);
            status = view.findViewById(R.id.item_status);
            business = view.findViewById(R.id.item_business);
            speed = view.findViewById(R.id.item_speed);
            kmPerDay = view.findViewById(R.id.item_kmPerDay);
            fuel = view.findViewById(R.id.item_fuel);

            //set ListView
            if (itemsModelListFiltered.get(i).getStatus().equals("Dừng")){
                imageView.setImageResource(R.mipmap.car_stop);
            }else{
                if (itemsModelListFiltered.get(i).getStatus().equals("Đỗ")){
                    imageView.setImageResource(R.mipmap.car_parking);
                }else{
                    if (itemsModelListFiltered.get(i).getStatus().equals("Chạy")){
                        imageView.setImageResource(R.mipmap.car_driving);
                    }else{
                        imageView.setImageResource(R.mipmap.car);
                    }
                }
            }
            imageView.setRotation(itemsModelListFiltered.get(i).getDirection());
            numberSign.setText(itemsModelListFiltered.get(i).getSignNumber());
            time.setText("Thời điểm: " + itemsModelListFiltered.get(i).getDate().toString());
            status.setText("Trạng thái: " + itemsModelListFiltered.get(i).getStatus());
            business.setText("Tác vụ: " + itemsModelListFiltered.get(i).getBusiness());
            speed.setText("Tốc độ: " + String.valueOf(itemsModelListFiltered.get(i).getSpeed()) + "(km/h)");
            kmPerDay.setText("Km ngày: " + String.valueOf(itemsModelListFiltered.get(i).getKmPerDay()) + "(km)");
            fuel.setText("Nhiên liệu: " + String.valueOf(itemsModelListFiltered.get(i).getFuel()) + "lít");

            return view;
        }


        //Set on Text Change
        @Override
        public Filter getFilter() {
            final Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();

                    if (constraint == null || constraint.length() == 0) {
                        filterResults.count = itemsModelList.size();
                        filterResults.values = itemsModelList;
                    } else {
                        String searchString = constraint.toString().toUpperCase();

                        List<Car> resultData = new ArrayList<>();

                        //Car change
                        for (Car car : itemsModelList) {
                            if (car.getSignNumber().contains(searchString)) {
                                resultData.add(car);
                            }

                            filterResults.count = resultData.size();
                            filterResults.values = resultData;
                        }
                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    itemsModelListFiltered = (List<Car>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
            return filter;
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        //Map setting
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            //zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
        }

        //Snippet //For multiple line below Title
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }


            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.BLACK);
                snippet.setText(marker.getSnippet());

                //info.setBackgroundColor(Color.parseColor("#bdbdbd"));
                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        //Mark
        mark();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (isViewHistoryOpen == false){
                    linearSeekBar.setVisibility(LinearLayout.GONE);
                    Fragment infoFragment = new ViewHistory();
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_container, infoFragment, "ViewHistory").commit();
                    passingCarSign = marker.getTitle();
                    passingCarLat = marker.getPosition().latitude;
                    passingCarLog = marker.getPosition().longitude;
                    /*passingMyLat = locationResult.getLastLocation().getLatitude();
                    passingCarLog = locationResult.getLastLocation().getLongitude();*/
                    isViewHistoryOpen = true;
                }else{
                    linearSeekBar.setVisibility(LinearLayout.GONE);
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag("ViewHistory");
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    passingCarSign = marker.getTitle();
                    isViewHistoryOpen = false;
                }

                //View view = getLayoutInflater().inflate(R.layout.fragment_view_history, null);

            }
        });

        //Disable click on Marker
        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });*/


        Marker ecec = mMap.addMarker(new MarkerOptions().position(new LatLng(20.995394, 105.808421)).draggable(true).visible(true));
        //Boolean hideMarker = new Boolean(false);
        LatLng latLngMark = new LatLng(20.995394, 105.808421);
        LatLng latLngTLU = new LatLng(20.975751, 105.815163);
        //animateMarker(mMap, listDrawItems.get(0), listDrawItems.get(15), hideMarker);
    }

    public static void animateMarker(final GoogleMap map, final Car carFrom, final Car carTo,
                                     final boolean hideMarker) {
        final Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(carFrom.getLatitude(), carFrom.getLongitude())).draggable(true).visible(true));
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = ((Projection) proj).toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 15000;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lat = t * carTo.getLatitude() + (1 - t) * startLatLng.latitude;
                double lng = t * carTo.getLongitude() + (1 - t) * startLatLng.longitude;

                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private void mark() {
        /*//Mark TLU
        LatLng TLU = new LatLng(20.9759048, 105.814603);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(TLU);
        mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
        mMap.addMarker(markerOptions);
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom(TLU, 15.0f ) );
        System.out.println("MARKED");*/

        //Create Car if not exist
        if (listItems.size() == 0) {
            for (int i = 0; i < listSignNumber.length; i++) {
                Car car = new Car(listSignNumber[i], listDate[i], listLatitude[i], listLongtitude[i], listStatus[i], listBusiness[i], random = 0 + r.nextFloat() * (360 - 0), listSpeed[i], listKmPerDay[i], listFuel[i], listMaxFuel[i]);
                listItems.add(car);
            }
            for (int i = listSignNumber.length; i < listLatitude.length; i++) {
                Car car = new Car(listLatitude[i], listLongtitude[i], random = 0 + r.nextFloat() * (360));
                listItems.add(car);
            }
        }else{
            System.out.println("List is already exist");
        }

        if (listDrawItems.size() == 0) {
            for (int i = 0; i < listSignNumber.length; i++) {
                Car car = new Car(listSignNumber[i], listDate[i], listDrawLatitude[i], listDrawLongtitude[i], listStatus[i], listBusiness[i], listDirection[i], listSpeed[i], listKmPerDay[i], listFuel[i], listMaxFuel[i]);
                listDrawItems.add(car);
            }
            for (int i = listSignNumber.length; i < listDrawLatitude.length; i++) {
                Car car = new Car(listDrawLatitude[i], listDrawLongtitude[i], listDirection[i], listDate[i], listSpeed[i]);
                listDrawItems.add(car);
            }
        }else{
            System.out.println("List is already exist");
        }

        //Customize marker and mark the location in listItems
        for (int i = 0; i < listItems.size(); i++){
            LatLng latLng = new LatLng(listItems.get(i).getLatitude(), listItems.get(i).getLongitude());
            MarkerOptions mark = new MarkerOptions();
            if (listItems.get(i).getStatus().equals("Dừng")){
                mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_stop));
            }else{
                if (listItems.get(i).getStatus().equals("Đỗ")){
                    mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_parking));
                }else{
                    if (listItems.get(i).getStatus().equals("Chạy")){
                        mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_driving));
                    }else{
                        mark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
                    }
                }
            }
            mark.title("" + listItems.get(i).getSignNumber());
            mark.snippet("Thời điểm: " + listItems.get(i).getDate().toString() + "\n" +
                    "Vĩ độ: " + listItems.get(i).getLatitude() + " - Kinh độ: " + listItems.get(i).getLongitude() + "\n" +
                    "Vận tốc: " + listItems.get(i).getSpeed() + "\n" +
                    "Trạng thái: " + listItems.get(i).getStatus() + "\n" +
                    "Km ngày: " + listItems.get(i).getKmPerDay() + "(km)" + "\n" +
                    "Nhiên liệu: " + listItems.get(i).getFuel() + "lít");

            mark.rotation(listItems.get(i).getDirection());
            mark.anchor((float) 0.5, (float) 0.5);
            //Create positon of the Car
            mark.position(latLng);
            //Then create and show Sign Number
            LatLng picture = new LatLng(latLng.latitude, latLng.longitude);
            IconGenerator icg = new IconGenerator(this);
            icg.setColor(Color.GRAY); // Gray background
            icg.setTextAppearance(R.style.WhiteText); // WHITE text
            Bitmap bm = icg.makeIcon(listItems.get(i).getSignNumber()); //Set Text
            MarkerOptions options = new MarkerOptions().position(picture).icon(BitmapDescriptorFactory.fromBitmap(bm));
            Marker newSign = mMap.addMarker(options);
            newSign.setTitle("Chọn xe để tra cứu thông tin");
            //Then add Car
            Marker addMarker = mMap.addMarker(mark);
            //addMarker.showInfoWindow();
        }
        LatLng latLng = new LatLng(listItems.get(listItems.size()-1).getLatitude(), listItems.get(listItems.size()-1).getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        //setLocationMarker(listItems.get(listItems.size()-1));

        /*//Draw the line
        //Customize Marker for Drawing the line
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
        markerOptions.title("" + listDrawItems.get(0).getSignNumber());
        markerOptions.snippet("Thời điểm: " + listDrawItems.get(0).getDate() + " " + listDrawItems.get(0).getTime() + "\n" +
                "Vĩ độ: " + listDrawItems.get(0).getLatitude() + " - Kinh độ: " + listDrawItems.get(0).getLongitude() + "\n" +
                "Vận tốc: " + listDrawItems.get(0).getSpeed() + "\n" +
                "Trạng thái: " + listDrawItems.get(0).getStatus() + "\n" +
                "Km ngày: " + listDrawItems.get(0).getKmPerDay() + "(km)" + "\n" +
                "Nhiên liệu: " + listDrawItems.get(0).getFuel() + "lít");
        //Direction
        markerOptions.rotation(listDrawItems.get(0).getDirection());
        //Set to middle
        markerOptions.anchor((float) 0.5, (float) 0.5);
        //Put Car to the Map
        LatLng latLngPrevious = new LatLng(listDrawItems.get(0).getLatitude(), listDrawItems.get(0).getLongitude());
        markerOptions.position(latLngPrevious);
        Marker addMarker = mMap.addMarker(markerOptions);
        addMarker.showInfoWindow();
        for (int i = 1; i < listDrawItems.size(); i++) {
            LatLng latLng = new LatLng(listDrawItems.get(i).getLatitude(), listDrawItems.get(i).getLongitude());
            MarkerOptions newMarker = new MarkerOptions();
            newMarker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
            newMarker.title("" + listDrawItems.get(i).getSignNumber());
            newMarker.snippet("Thời điểm: " + listDrawItems.get(i).getDate() + " " + listDrawItems.get(i).getTime() + "\n" +
                    "Vĩ độ: " + listDrawItems.get(i).getLatitude() + " - Kinh độ: " + listDrawItems.get(0).getLongitude() + "\n" +
                    "Vận tốc: " + listDrawItems.get(i).getSpeed() + "\n" +
                    "Trạng thái: " + listDrawItems.get(i).getStatus() + "\n" +
                    "Km ngày: " + listDrawItems.get(i).getKmPerDay() + "(km)" + "\n" +
                    "Nhiên liệu: " + listDrawItems.get(i).getFuel() + "lít");

            newMarker.rotation(listDrawItems.get(i).getDirection());
            newMarker.anchor((float) 0.5, (float) 0.5);
            addMarker.remove();

            //Draw a Line
            PolylineOptions polylineOptions = new PolylineOptions()
                    .add(latLng).add(latLngPrevious).clickable(true);
            polyline = mMap.addPolyline(polylineOptions);
            polyline.setColor(Color.parseColor("#F50057"));
            System.out.println("Polyline is already shown up");

            latLngPrevious = latLng;
            markerOptions.position(latLng);
            addMarker = mMap.addMarker(markerOptions);
            addMarker.showInfoWindow();

            //Snaking
            setLocationMarker(listDrawItems.get(i));
        }
        System.out.println("FINISHED ADD");*/
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            //System.out.println("onLocationResult: " + locationResult.getLastLocation());
            //Toast.makeText(MapsActivity.this, "" + locationResult.getLastLocation(), Toast.LENGTH_SHORT).show();
            passingMyLat = locationResult.getLastLocation().getLatitude();
            passingMyLog = locationResult.getLastLocation().getLongitude();
            if (mMap != null) {
                //setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    private void drawTheLine(double Lat1, double Log1, double Lat2, double Log2, double speed){
        LatLng stepLatLng1, stepLatLng2;
        stepLatLng1 = new LatLng(Lat1, Log1);
        stepLatLng2 = new LatLng(Lat2, Log2);

        if (speed <= 40){
            speedColor = "#f8bbd0";
        }else if (speed > 0 && speed <= 100){
            speedColor = "#64dd17";
        }else if (speed > 100 && speed <= 150){
            speedColor = "#ff3d00";
        }

        /*double distanceChecking = sqrt((Lat2 - Lat1)*(Lat2 - Lat1) + (Log2 - Log1)*(Log2 - Log1));
        System.out.println("distanceChecking: " + distanceChecking);
        if (distanceChecking > 0.0001){

            listPolylines.add(mMap.addPolyline(new PolylineOptions().color(parseColor(speedColor)).width(5.0f).geodesic(true)
                    .add(stepLatLng1).add(stepMiddleLatLng).clickable(true)));
            System.out.println("Middle point");

            //stepLatLng1 = stepMiddleLatLng;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawTheLine(stepMiddleLatLng.latitude, stepMiddleLatLng.longitude, stepLatLng2.latitude, stepLatLng2.longitude, 250l);
                }
            }, 100);
            drawTheLine(stepMiddleLatLng.latitude, stepMiddleLatLng.longitude, stepLatLng2.latitude, stepLatLng2.longitude, 250l);
        }*/
        listPolylines.add(this.mMap.addPolyline(new PolylineOptions().color(parseColor(speedColor)).width(5.0f).geodesic(true)
                .add(stepLatLng1).add(stepLatLng2).clickable(true)));
    }

    private void setLocationMarker(Car car) {
        LatLng latLng = new LatLng(car.getLatitude(), car.getLongitude());
        //if location Marker is not exist yet => Create and customize Marker and Mark it 1 time only
        if (locationMarker == null) {
            //Create a new Marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            if (car.getStatus().equals("Dừng")){
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_stop));
            }else{
                if (car.getStatus().equals("Đỗ")){
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_parking));
                }else{
                    if (car.getStatus().equals("Chạy")){
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_driving));
                    }else{
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
                    }
                }
            }
            //Set Properties
            markerOptions.title("" + car.getSignNumber());
            markerOptions.snippet("Thời điểm: " + car.getDate().toString() + "\n" +
                    "Vĩ độ: " + car.getLatitude() + " - Kinh độ: " + car.getLongitude() + "\n" +
                    "Vận tốc: " + car.getSpeed() + "\n" +
                    "Trạng thái: " + car.getStatus() + "\n" +
                    "Km ngày: " + car.getKmPerDay() + "(km)" + "\n" +
                    "Nhiên liệu: " + car.getFuel() + "lít");
            //Direction
            markerOptions.rotation(car.getDirection());
            //Set to middle
            markerOptions.anchor((float) 0.5, (float) 0.5);

            locationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }else{
            //if location Marker is exist => Move the Mark and set Rotation
            locationMarker.setPosition(latLng);

            //Direction
            locationMarker.setRotation(car.getDirection());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }

        //Draw a Circle
        /*if (locationCircle == null){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(3);
            circleOptions.strokeColor(Color.parseColor("#E6F2FF"));
            circleOptions.fillColor(Color.parseColor("#b2ebf2"));
            circleOptions.radius(150);
            locationCircle = mMap.addCircle(circleOptions);
            System.out.println("Didnt have a circle YET");
        }else{
            locationCircle.setCenter(latLng);
            locationCircle.setRadius(150);
            System.out.println("Got a circle already");
        }*/
    }

    private void setUserLocationMarker(Location locationMarker) {
        LatLng latLng = new LatLng(locationMarker.getLatitude(), locationMarker.getLongitude());
        if (userLocationMarker == null) {
            //Create a new Marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));

            //Direction
            markerOptions.rotation(locationMarker.getBearing());

            //Set to middle
            markerOptions.anchor((float) 0.5, (float) 0.5);

            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        } else {
            userLocationMarker.setPosition(latLng);

            //Direction
            userLocationMarker.setRotation(locationMarker.getBearing());

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }

        if (userLocationCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(3);
            circleOptions.strokeColor(parseColor("#E6F2FF"));
            circleOptions.fillColor(parseColor("#b2ebf2"));
            circleOptions.radius(150);
            userLocationCircle = mMap.addCircle(circleOptions);
            System.out.println("Did not have a circle YET");
        } else {
            userLocationCircle.setCenter(latLng);
            userLocationCircle.setRadius(150);
            System.out.println("Got a circle already");
        }
    }

    private void startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdate();
        } else {
            //Ask for Permission
            Toast.makeText(this, "NEED PERMISSION TO ACCESS_FINE_LOCATION", Toast.LENGTH_SHORT).show();
            System.out.println("NEED PERMISSION TO ACCESS_FINE_LOCATION");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdate();
    }

    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location == null) {
                    Toast.makeText(MapsActivity.this, "GẶP LỖI KHI TÌM VỊ TRÍ", Toast.LENGTH_SHORT).show();
                } else {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                    MarkerOptions newMark = new MarkerOptions();
                    newMark.position(latLng);
                    newMark.icon(BitmapDescriptorFactory.fromResource(R.mipmap.car));
                    mMap.addMarker(newMark);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                Toast.makeText(this, "NEED_ACCESS_LOCATION_REQUEST_CODE", Toast.LENGTH_SHORT).show();
            }
        }
    }
}