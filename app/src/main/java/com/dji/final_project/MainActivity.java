package com.dji.final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleMap.OnMapClickListener,OnMapReadyCallback{
    private MapView mMapView;
    private GoogleMap map;
    private RelativeLayout mMapContainer;
    private Button locate, adding, clear,hotPoint;
    private Button config, upload, start, stop;
    private boolean isAdd = false;
    private boolean isHot = false;
    private boolean Hot_point_exist = true;

    float[] results = new float[1];
    private final Map<Integer, Marker> mMarkers = new ConcurrentHashMap<Integer, Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.mapView);
        ImageButton imageviewBTN = findViewById(R.id.btn_full_screen_map);
        LinearLayout firstMenu = findViewById(R.id.firstMenu);
        LinearLayout secondMenu = findViewById(R.id.secondMenu);

        mMapView.getMapAsync(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.setVisibility(View.INVISIBLE);
        imageviewBTN.setVisibility(View.INVISIBLE);
        firstMenu.setVisibility(View.INVISIBLE);
        secondMenu.setVisibility(View.INVISIBLE);

        initUI();
    }

    private void initUI() {
        adding = (Button) findViewById(R.id.adding);
        hotPoint= (Button) findViewById(R.id.hotPoint);

        hotPoint.setOnClickListener(this);
        adding.setOnClickListener(this);
    }

    private void enableDisableAdd(){
        if (isAdd == false) {
            isAdd = true;
            Toast.makeText(getApplicationContext(),"You can Add points as you wish...", Toast.LENGTH_LONG).show();
            adding.setText("Exit");
        }else{
            isAdd = false;
            adding.setText("Add");
        }
    }

    private void enableDisableHotPoint(){
        if (isHot == false) {
            isHot = true;
            Toast.makeText(getApplicationContext(),"You can Add only ONE HOT POINT!!!", Toast.LENGTH_LONG).show();
            hotPoint.setText("Exit");
        }else{
            isHot = false;
            hotPoint.setText("Hot");
        }
    }


    private static final int MAP_LAYOUT_STATE_CONTRACTED = 0;
    private static final int MAP_LAYOUT_STATE_EXPANDED = 1;
    private int mMapLayoutState = 0;


    public void fullscreenbtnclick(View v) {
        RelativeLayout mapContainer = findViewById(R.id.map_container);
        Button Login = findViewById(R.id.logbutton);
        LinearLayout firstMenu = findViewById(R.id.firstMenu);
        LinearLayout secondMenu = findViewById(R.id.secondMenu);

        if(mMapLayoutState == MAP_LAYOUT_STATE_CONTRACTED){
            mMapLayoutState = MAP_LAYOUT_STATE_EXPANDED;
            //expandMapAnimation();
            expand(mapContainer);
        }
        else if(mMapLayoutState == MAP_LAYOUT_STATE_EXPANDED){
            mMapLayoutState = MAP_LAYOUT_STATE_CONTRACTED;
            //contractMapAnimation();
            collapse(mapContainer);
            Login.setVisibility(View.VISIBLE);
            firstMenu.setVisibility(View.INVISIBLE);
            secondMenu.setVisibility(View.INVISIBLE);
        }

    }

    public void onClickLog(View v){
        EditText UserName = findViewById(R.id.name);
        EditText password = findViewById(R.id.Password);
        Button Login = findViewById(R.id.logbutton);
        ImageButton imageviewBTN = findViewById(R.id.btn_full_screen_map);
        LinearLayout firstMenu = findViewById(R.id.firstMenu);
        LinearLayout secondMenu = findViewById(R.id.secondMenu);

        if ((UserName.getText().toString()).matches("")  || (password.getText().toString()).matches(""))
        {
            Toast.makeText(getApplicationContext(),"Please Enter Details!!!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"You succeeded!", Toast.LENGTH_LONG).show();
            //MapView map = findViewById(R.id.mapView);
            RelativeLayout mapContainer = findViewById(R.id.map_container);
            mMapView.setVisibility(View.VISIBLE);
            imageviewBTN.setVisibility(View.VISIBLE);
            firstMenu.setVisibility(View.VISIBLE);
            secondMenu.setVisibility(View.VISIBLE);
            expand(mapContainer);
            mMapLayoutState =1;
            Login.setVisibility(View.INVISIBLE);


            String floatToString =String.valueOf(results[0]);
            Toast.makeText(getApplicationContext(),"The Distance is: "+floatToString + " meters", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onMapClick(LatLng point) {
        if (isAdd == true)
        {
            Toast.makeText(getApplicationContext(),"Its fine here", Toast.LENGTH_LONG).show();
            markWaypoint(point);
        }

        if(isAdd == false)
        {
            Toast.makeText(getApplicationContext(),"Cannot Add Waypoint", Toast.LENGTH_LONG).show();
        }

        if(isHot == true && Hot_point_exist == true)
        {
            Toast.makeText(getApplicationContext(),"Its fine here", Toast.LENGTH_LONG).show();
            markHotWaypoint(point);
        }

        if (isHot == false || Hot_point_exist == false)
        {
            Toast.makeText(getApplicationContext(),"Cannot Add Waypoint", Toast.LENGTH_LONG).show();
        }
    }


    private void markWaypoint(LatLng point){
        //Create MarkerOptions object
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker marker = map.addMarker(markerOptions);
        mMarkers.put(mMarkers.size(), marker);
    }


    private void markHotWaypoint(LatLng point){
        if(Hot_point_exist == true)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(point);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            Marker marker = map.addMarker(markerOptions);
            mMarkers.put(mMarkers.size(), marker);
            Hot_point_exist = false;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng israel = new LatLng(31.766140,34.632272);
        map.addMarker(new MarkerOptions().position(israel).title("Ashdod-Home!"));
        LatLng Ivan = new LatLng(32.019567,34.751503);
        map.addMarker(new MarkerOptions().position(Ivan).title("Ivan-Home!"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.766140,34.632272), 10.5f));
        //map.moveCamera(CameraUpdateFactory.newLatLng(israel));
        map.setMapType(map.MAP_TYPE_HYBRID);
        PolylineOptions line = new PolylineOptions().add(new LatLng(31.766140,34.632272),
                new LatLng(32.019567,34.751503))
                .width(5).color(Color.RED);
        map.addPolyline(line);


        Location.distanceBetween(israel.latitude,israel.longitude,Ivan.latitude,Ivan.longitude,results);
        setUpMap();



    }

    private void setUpMap() {
        map.setOnMapClickListener(this);// add the listener for click for amap object

    }

    @Override
    protected void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }



    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adding:{
                enableDisableAdd();
                break;
            }

            case R.id.hotPoint:
                enableDisableHotPoint();
                break;

            case R.id.config:

                break;

            default:
                break;
        }
    }


}