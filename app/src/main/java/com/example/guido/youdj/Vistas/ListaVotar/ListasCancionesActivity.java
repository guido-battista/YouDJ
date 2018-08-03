package com.example.guido.youdj.Vistas.ListaVotar;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

import com.example.guido.youdj.Funciones;
import com.example.guido.youdj.Modelos.Cancion;
import com.example.guido.youdj.R;

//Import para Volley
import com.android.volley.Request;
import com.android.volley.DefaultRetryPolicy;

import org.json.JSONObject;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.example.guido.youdj.Vistas.ElegirEventoActivity;
import com.example.guido.youdj.Volley.CancionSonandoResp;
import com.example.guido.youdj.Volley.ErrorManager;
import com.example.guido.youdj.Volley.MySingleton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ListasCancionesActivity extends AppCompatActivity
    implements OnFragmentInteractionListener{

    @Override
    public void recargarPagina()
    {

        //Se limpia el cuadro de busqueda
        limpiarBusqueda();

        //EditText filterCanciones = findViewById(R.id.filterCanciones);
        //filterCanciones.clearFocus();

        //Recupero el Id de evento de las preferencias
        SharedPreferences sp = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        idEvento = sp.getString("idEvento", "default value");
        final Context context = this;

        //Se buscan las canciones en el WS
        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/canciones?idEvento=" + idEvento;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        //Actualizo canciones a Votar
                        cancionesVotar = Cancion.filtrarCanciones(response, "Votar");
                        cancionesAVotarFragment.recargar(cancionesVotar.toString());

                        //Actualizo canciones ya escuchadas
                        cancionesYaEscuchadas = Cancion.filtrarCanciones(response, "Escuchada");
                        cancionesYaEscuchadasFragment.recargar(cancionesYaEscuchadas.toString());

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

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }

    @Override
    public ProgressDialog getProgressDialog()
    {
        return this.progressDialog;
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    public JSONArray cancionesVotar;
    public JSONArray cancionesYaEscuchadas;
    public String idEvento;
    ProgressDialog progressDialog;
    private CancionesAVotarFragment cancionesAVotarFragment;
    private CancionesYaEscuchadasFragment cancionesYaEscuchadasFragment;
    private ArrayList notificacionesActivas;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        this.moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        revisarNotificaciones();

        setContentView(R.layout.activity_listas_canciones);

        progressDialog = new ProgressDialog(this);

        //Se activa el progress bar para ir al WS
        progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Buscando..."); // Setting Message
        progressDialog.setMessage("Cargando canciones"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        //------------------------------------------------


        //Genero lista de itemsSeleccionados
        notificacionesActivas = new ArrayList();
        if (!Funciones.preferenceContains(this, "notificacion_aviso_voto") ||
                Funciones.getBooleanPrefences(this, "notificacion_aviso_voto"))
        {
            notificacionesActivas.add(0);
        }

        //Notificaciones de aviso para votar
        if (!Funciones.preferenceContains(this, "notificacion_cancion_sonando") ||
                Funciones.getBooleanPrefences(this, "notificacion_cancion_sonando"))
        {
            notificacionesActivas.add(1);
        }
        //Recupero el Id de evento de las preferencias
        /*
        SharedPreferences sp = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        idEvento = sp.getString("idEvento", "default value");
        */

        idEvento = Funciones.getStringPreference(this,"idEvento");

        //Se buscan las canciones en el WS
        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/canciones?idEvento=" + idEvento;

        final Context context = this;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        cancionesVotar = Cancion.filtrarCanciones(response, "Votar");
                        cancionesYaEscuchadas = Cancion.filtrarCanciones(response, "Escuchada");
                        progressDialog.dismiss();
                        crearComponentes();
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


    protected void crearComponentes()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_canciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salir) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            Fragment fragment = null;

            switch (position)
            {
                case (0):
                    {
                        cancionesAVotarFragment =  CancionesAVotarFragment.newInstance(cancionesVotar.toString());
                        fragment = cancionesAVotarFragment;
                    }
                break;

                case (1):
                {
                    cancionesYaEscuchadasFragment = CancionesYaEscuchadasFragment.newInstance(cancionesYaEscuchadas.toString());
                    fragment =  cancionesYaEscuchadasFragment;
                }
                break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    public void onClickRefresh(MenuItem menuItem)
    {

        //Toast.makeText(this, "Se apreto REFRESH", Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Actualizando listas..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        recargarPagina();
        return;
    }

    public void onClickSonando(MenuItem menuItem)
    {
        final Context context = this;

        final MenuItem menuItemFinal = menuItem;
        menuItemFinal.setEnabled(false);

        progressDialog = new ProgressDialog(this);

        //Se activa el progress bar para ir al WS
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando..."); // Setting Message
        //progressDialog.setTitle("Buscando Evento"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);


        //Toast.makeText(this, "Se apreto SONANDO", Toast.LENGTH_SHORT).show();
        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/sonando?idEvento=" + Funciones.getStringPreference(this, "idEvento");

        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        progressDialog.dismiss();
                        if (CancionSonandoResp.ObtenerCodigo(response) == 0) {
                            //Toast.makeText(context, CancionSonandoResp.ObtenerCancionSonando(response), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Sonando ahora")
                                    .setMessage(CancionSonandoResp.ObtenerCancionSonando(response))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            menuItemFinal.setEnabled(true);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();

                        } else {

                            //Toast.makeText(context, CancionSonandoResp.ObtenerDescripcion(response), Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle(CancionSonandoResp.ObtenerDescripcion(response))
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                            menuItemFinal.setEnabled(true);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressDialog.dismiss();
                        menuItemFinal.setEnabled(true);
                        ErrorManager.mostrarErrorConexion(context);

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        return;
    }

    public void reiniciarBusquedaClick(View v)
    {
        limpiarBusqueda();
        cancionesAVotarFragment.reiniciarBusqueda();
        return;
    }

    public void limpiarBusqueda()
    {
        EditText filter = findViewById(R.id.filterCanciones);
        filter.setText("");
        filter.onEditorAction(EditorInfo.IME_ACTION_DONE);
        filter.clearFocus();
        RelativeLayout layoutBuscar = findViewById(R.id.layoutBusqueda);
        layoutBuscar.setVisibility(View.GONE);
    }

    public void onClickSalirEvento(MenuItem menuItem)
    {
        //Se limpian todas las notificaciones
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.
                        NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        //------------------------------------

        Funciones.unsuscribeAll(this);
        Funciones.preferencesRemoveKey(this, getResources().getString(R.string.preferences_id_evento));
        Intent i = new Intent(this, ElegirEventoActivity.class);
        startActivity(i);
        finish();
    }

    public void onClickBuscar (MenuItem menuItem)
    {
        RelativeLayout layoutBuscar = findViewById(R.id.layoutBusqueda);
        layoutBuscar.setVisibility(View.VISIBLE);
        EditText filter = findViewById(R.id.filterCanciones);
        filter.requestFocus();
    }

    public void onClickNotificaciones(MenuItem menuItem)
    {

        Dialog dialog;
        final String[] items = {" Avisos de Voto","Canción Sonando"};
        final boolean[] checkedItems = {Funciones.getBooleanPrefences(this,"notificacion_aviso_voto"),
                                        Funciones.getBooleanPrefences(this,"notificacion_cancion_sonando")};
        //final ArrayList itemsSelected = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Notificaciones a recibir");
        builder.setMultiChoiceItems(items,  checkedItems ,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedItemId,
                                        boolean isSelected) {
                        if (isSelected) {
                            notificacionesActivas.add(selectedItemId);
                        } else if (notificacionesActivas.contains(selectedItemId)) {
                            notificacionesActivas.remove(Integer.valueOf(selectedItemId));
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Your logic when OK button is clicked
                        habilitarDeshabilitarNotificaciones(notificacionesActivas);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    public void habilitarDeshabilitarNotificaciones(ArrayList lista)
    {
        for (int i=0; i<2;i++)
        {
            switch (i)
            {
                case (0):
            {
                String topic = idEvento + getResources().getString(R.string.notificacion_votar);
                if (lista.contains(i)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                    Funciones.setBooleanPreference(this, "notificacion_aviso_voto",true);
                }
                else {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    Funciones.setBooleanPreference(this, "notificacion_aviso_voto",false);
                }
            }
            break;
                case (1):
                {
                    String topic = idEvento + getResources().getString(R.string.notificacion_cancion_sonando);
                    if (lista.contains(i)) {
                        FirebaseMessaging.getInstance().subscribeToTopic(topic);
                        Funciones.setBooleanPreference(this, "notificacion_cancion_sonando",true);
                    }
                    else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                        Funciones.setBooleanPreference(this, "notificacion_cancion_sonando",false);
                    }
                }
                break;
            }
        }
    }

    public void revisarNotificaciones()
    {
        if (!Funciones.preferenceContains(this,"notificacion_aviso_voto"))
            Funciones.setBooleanPreference(this,"notificacion_aviso_voto", true);
        if (!Funciones.preferenceContains(this,"notificacion_cancion_sonando"))
            Funciones.setBooleanPreference(this,"notificacion_cancion_sonando", true);
    }
}
