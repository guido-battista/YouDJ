package com.example.guido.youdj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.example.guido.youdj.ListaVotar.ListasCancionesActivity;

public class ElegirEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elegir_evento);
    }

    public void buttonClick(View v) {
        // Code here executes on main thread after user presses button

        Intent i = new Intent(this, ListasCancionesActivity.class);
        startActivity(i);

    }
}
