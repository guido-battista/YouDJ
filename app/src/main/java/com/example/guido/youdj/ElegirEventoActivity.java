package com.example.guido.youdj;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.widget.Toast;

//Import para Volley
import com.android.volley.Request;

import org.json.JSONObject;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.DefaultRetryPolicy;

import com.example.guido.youdj.ListaVotar.ListasCancionesActivity;
import com.example.guido.youdj.Modelos.Evento;
import com.example.guido.youdj.Volley.MySingleton;

public class ElegirEventoActivity extends AppCompatActivity {

    private TextView nroEvento;
    private TextView codigoEvento;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_evento);
        nroEvento = (TextView) findViewById(R.id.nroEvento);
        codigoEvento = (TextView) findViewById(R.id.codigoEvento);

    }

    public void buttonClick(View v) {
        // Code here executes on main thread after user presses button

        //String url = "http://192.168.1.6:3000/evento?idEvento=1";

        //Se valida que se hayan ingresado todos los datos
        if (nroEvento.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Debe ingresar número de evento",Toast.LENGTH_SHORT).show();
            return;
        }

        if (codigoEvento.getText().toString().trim().equals(""))
        {
            Toast.makeText(getApplicationContext(),"Debe ingresar código de evento",Toast.LENGTH_SHORT).show();
            return;
        }

        //------------------------------------------------


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

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Evento evento = Evento.fromJson(response);
                        validarEvento(evento);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressDialog.dismiss();
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

    public void validarEvento(Evento evento)
    {
        if (evento._id.equals("null"))
        {
            Toast.makeText(getApplicationContext(),"El evento ingresado no existe",Toast.LENGTH_SHORT).show();
            return;
        }

        else if (!evento.codigo.equals(codigoEvento.getText().toString().trim()))
        {
            Toast.makeText(getApplicationContext(),"Codigo de Evento Incorrecto",Toast.LENGTH_SHORT).show();
            return;
        }

        //Guardo el Id de evento en las preferencias
        SharedPreferences sp = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("idEvento", evento.id);
        editor.apply();
        //------------------------------------------

        Intent i = new Intent(this, ListasCancionesActivity.class);
        startActivity(i);

    }


}


