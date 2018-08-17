package com.battista.guido.youdj.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.battista.guido.youdj.Modelos.Evento;
import com.battista.guido.youdj.Volley.ErrorManager;
import com.battista.guido.youdj.Volley.MySingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.guido.youdj.R;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

//Import para myLocation
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;

import java.util.List;

//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    GoogleApiClient mGoogleApiClient;

    private int LOCATION_PERMISSION_CODE = 1;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    float zoomLevel = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if (status == ConnectionResult.SUCCESS)
        {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else
        {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(),10);
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //-34.600323, -58.428202: La salsera
        
        //LatLng sydney = new LatLng(-34.600323, -58.428202);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Oscar Gulli - La Salsera"));
        
        
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,zoomLevel));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //buildGoogleApiClient();

                localizacionHabilitada();

            }
            else
            {
                Toast.makeText(this, "No tiene permiso", Toast.LENGTH_SHORT).show();
                requestLocationPermission();
            }
        }
        else {
            //buildGoogleApiClient();
            localizacionHabilitada();
        }

        //mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMyLocationEnabled(true);
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    /*
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    */

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Se requiere permiso de localización")
                    .setMessage("Este permiso se necesita para encontrar los próximos eventos cerca de su ubicación")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();

        } else
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    localizacionHabilitada();
                }

            }
            else
            {

            }

        }
    }

    private void localizacionHabilitada()
    {
        try {

            mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                        if (location != null) {
                            // Got last known location. In some rare situations this can be null.
                            // Logic to handle location object
                            double latitud = location.getLatitude();
                            double longi = location.getLongitude();

                            //Toast.makeText(MapsActivity.this, String.valueOf(latitud) + String.valueOf(longi), Toast.LENGTH_SHORT);
                            LatLng miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miPosicion, zoomLevel));
                            buscarEventosCercanos(location.getLatitude(), location.getLongitude());
                        }
                    }
                }
            );
        }
        catch (SecurityException e)
        {

        };
    }
    
    private void buscarEventosCercanos(double lat, double longit)
    {
        final Context context = this;

        final double latitud = lat;
        final double longitud = longit;

        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/eventosCercanos?latitud=" + String.valueOf(latitud) + "&longitud=" + String.valueOf(longitud);

        Toast.makeText(this,url,Toast.LENGTH_SHORT);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        //Actualizo canciones a Votar
                        List<Evento> eventos = Evento.fromJsonArray(response);
                        //progressDialog.dismiss();

                        for (Evento evento:eventos)
                        {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(evento.latitud,evento.longitud)).title(evento.nombre + " - " + evento.dj).snippet(evento.lugar));
                            
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //progressDialog.dismiss();
                        ErrorManager.mostrarErrorConexion(context);
                    }
                });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    public void onFabClick(View v) {

        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Got last known location. In some rare situations this can be null.
                                // Logic to handle location object
                                double latitud = location.getLatitude();
                                double longi = location.getLongitude();

                                //Toast.makeText(MapsActivity.this, String.valueOf(latitud) + String.valueOf(longi), Toast.LENGTH_SHORT);
                                LatLng miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(miPosicion, zoomLevel));
                            }
                        }
                    }
            );
        }
        catch (SecurityException e)
        {

        }
    }




}
