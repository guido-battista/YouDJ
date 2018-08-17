package com.battista.guido.youdj.Vistas;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

import android.app.ProgressDialog;
import android.widget.Toast;

//Import para Volley
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.DefaultRetryPolicy;

import com.battista.guido.youdj.Funciones;
import com.battista.guido.youdj.Modelos.Cancion;
import com.battista.guido.youdj.Modelos.Evento;
import com.battista.guido.youdj.Vistas.ListaVotar.ListasCancionesActivity;
import com.battista.guido.youdj.Volley.ErrorManager;

import com.example.guido.youdj.R;
import com.battista.guido.youdj.Volley.MySingleton;

//Firebase
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class ElegirEventoActivity extends AppCompatActivity {

    private EditText nroEvento;
    private EditText codigoEvento;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_elegir_evento);
        nroEvento = (EditText) findViewById(R.id.nroEvento);
        codigoEvento = (EditText) findViewById(R.id.codigoEvento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_elegir_evento, menu);
        return true;
    }

    public void onClickSearch(MenuItem menuItem) {

        Intent intent = new Intent(ElegirEventoActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    public void buttonClick(View v) {
        // Code here executes on main thread after user presses button

        //String url = "http://192.168.1.6:3000/evento?idEvento=1";

        //Se valida que se hayan ingresado todos los datos
        if (nroEvento.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe ingresar número de evento", Toast.LENGTH_SHORT).show();
            return;
        }

        if (codigoEvento.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe ingresar código de evento", Toast.LENGTH_SHORT).show();
            return;
        }

        //------------------------------------------------

        final Context context = this;

        //Se activa el progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando..."); // Setting Message
        progressDialog.setTitle("Buscando Evento"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        //------------------------------------------------

        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/evento?idEvento=" + nroEvento.getText();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Evento evento = Evento.fromJson(response);
                        if (validarEvento(evento)) {
                            ingresarEvento(evento);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressDialog.dismiss();
                        ErrorManager.mostrarErrorConexion(context);
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        //--------------------------------------------------------

        /*
        String url = "http://192.168.1.6:3000/canciones?idEvento=1";

        JsonArrayRequest  jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        mTextView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        mTextView.setText(error.getMessage());

                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        */

        //startActivity(i);
    }

    public void buttonReconocerClick(View v) {
        // Code here executes on main thread after user presses button

        //Se activa el progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Reconociendo Evento en el que se encuentra"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

        FusedLocationProviderClient mFusedLocationClient;

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Falta!!!!! Tengo que reemplazar el Try por el chequeo de permisos!!!!!
        try {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // Got last known location. In some rare situations this can be null.
                                // Logic to handle location object
                                double latitud = location.getLatitude();
                                double longitud = location.getLongitude();
                                buscarEventosCercanos(latitud, longitud);
                            }
                        }
                    }
            );
        } catch (SecurityException e) {

        }


    }

    public void buscarEventosCercanos(double latitud, double longitud)
    {
        final Context context = this;

        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/reconocerEvento?latitud=" + String.valueOf(latitud) + "&longitud=" + String.valueOf(longitud);

        Toast.makeText(this,url,Toast.LENGTH_SHORT);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        //Actualizo canciones a Votar
                        List<Evento> eventos = Evento.fromJsonArray(response);
                        progressDialog.dismiss();

                        switch (eventos.size())
                        {
                            case 0:
                            {
                                Toast.makeText(getApplicationContext(), "No se encuentra ningún evento en su ubicación",Toast.LENGTH_SHORT).show();
                            }
                            break;
                            case 1:
                            {
                                ingresarEvento(eventos.get(0));
                            }
                            break;
                            default:
                            {
                                //Falta!!!! Veo que hago!
                            }
                            break;
                        }

                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressDialog.dismiss();
                        ErrorManager.mostrarErrorConexion(context);
                    }
                });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    public boolean validarEvento(Evento evento)
    {
        if (evento._id.equals("null"))
        {
            Toast.makeText(this,"El evento ingresado no existe",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (!evento.codigo.equals(codigoEvento.getText().toString().trim()))
        {
            Toast.makeText(this,"Codigo de Evento Incorrecto",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

        //Guardo el Id de evento en las preferencias
        /*
        SharedPreferences sp = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("idEvento", evento.id);
        editor.apply();
        */
    }

    public void ingresarEvento(Evento evento)
    {
        Funciones.setStringPreference(this, "idEvento", evento.id);
        Funciones.setLongPreference(this, "intervalo", evento.intervalo);
        //------------------------------------------

        revisarNotificaciones(evento.id);
        //Toast.makeText(getApplicationContext(), "Topic Subscribed <" + topic + ">", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, ListasCancionesActivity.class);
        startActivity(i);
        nroEvento.setText("");
        codigoEvento.setText("");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.moveTaskToBack(true);
    }

    public void revisarNotificaciones(String eventoId)
    {
        //Notificaciones de aviso para votar
        if (!Funciones.preferenceContains(this, "notificacion_aviso_voto") ||
                Funciones.getBooleanPrefences(this, "notificacion_aviso_voto"))
        {
            String topic = eventoId + getResources().getString(R.string.notificacion_votar);
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        }

        //Notificaciones de aviso para votar
        if (!Funciones.preferenceContains(this, "notificacion_cancion_sonando") ||
                Funciones.getBooleanPrefences(this, "notificacion_cancion_sonando"))
        {
            String topic = eventoId + getResources().getString(R.string.notificacion_cancion_sonando);
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
        }
    }
}


