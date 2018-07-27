package com.example.guido.youdj.ListaVotar;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import com.example.guido.youdj.Modelos.Cancion;
import com.example.guido.youdj.R;

import java.util.ArrayList;
import java.util.List;

import android.widget.Toast;

//Import para Volley
import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.example.guido.youdj.ListaVotar.ListasCancionesActivity;
import com.example.guido.youdj.Modelos.Evento;
import com.example.guido.youdj.Volley.MySingleton;

public class ListasCancionesActivity extends AppCompatActivity
    implements OnFragmentInteractionListener{

    @Override
    public void recargarPagina()
    {
        //Recupero el Id de evento de las preferencias
        SharedPreferences sp = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        idEvento = sp.getString("idEvento", "default value");

        //Se buscan las canciones en el WS
        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/canciones?idEvento=" + idEvento;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        cancionesVotar = filtrarCanciones(response, "Votar");
                        cancionesAVotarFragment.recargar(cancionesVotar.toString());
                        
                        progressDialog.dismiss();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressDialog.dismiss();
                    }
                });

        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);

    }

    public JSONArray filtrarCanciones(JSONArray input, String filtro) {
        JSONArray resp = new JSONArray();

        try {
            for (int i = 0; i < input.length(); i++) {
                if (input.getJSONObject(i).get("estado").equals(filtro))
                {
                    resp.put(input.get(i));
                }
            }
        }
        catch(JSONException e)
        {

        }

        return resp;
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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listas_canciones);

        progressDialog = new ProgressDialog(this);

        //Se activa el progress bar para ir al WS
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Buscando..."); // Setting Message
        progressDialog.setTitle("Buscando Evento"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        //------------------------------------------------


        //Recupero el Id de evento de las preferencias
        SharedPreferences sp = getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        idEvento = sp.getString("idEvento", "default value");

        //Se buscan las canciones en el WS
        //Se llama al WebService
        String url = getResources().getString(R.string.WsUrl) + "/canciones?idEvento=" + idEvento;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url , null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        cancionesVotar = filtrarCanciones(response, "Votar");
                        progressDialog.dismiss();
                        crearComponentes();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        progressDialog.dismiss();
                    }
                });

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
        if (id == R.id.action_settings) {
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
                    cancionesYaEscuchadasFragment = CancionesYaEscuchadasFragment.newInstance();
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
}
