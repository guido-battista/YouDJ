package com.battista.guido.youdj.Vistas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.battista.guido.youdj.Funciones;

import com.example.guido.youdj.R;
import com.battista.guido.youdj.Vistas.ListaVotar.ListasCancionesActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (Funciones.preferenceContains(this, getResources().getString(R.string.preferences_id_evento)))
        {
            startActivity(new Intent(SplashActivity.this, ListasCancionesActivity.class));
        }
        else
        {
            startActivity(new Intent(SplashActivity.this, ElegirEventoActivity.class));
        }
    }
}
