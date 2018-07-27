package com.example.guido.youdj.ListaVotar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.guido.youdj.Modelos.Cancion;
import com.example.guido.youdj.R;
import com.example.guido.youdj.Volley.MySingleton;
import com.example.guido.youdj.Volley.SumarVotoResp;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CancionesAVotarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancionesAVotarFragment extends Fragment
    implements  ItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    //Defino mètodos de click

    @Override
    public void onClick(View view, int position, boolean isLongClick)
    {
        final int pos = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Desea votar este tema")
                .setMessage(canciones.get(position).titulo)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(),"Se apreto SI", Toast.LENGTH_SHORT).show();
                        votarTema(canciones.get(pos));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
        //Toast.makeText(getActivity(), canciones.get(position).titulo, Toast.LENGTH_SHORT).show();
    }



    public void recargar (String canciones)
    {
        try
        {
            this.canciones = Cancion.fromJsonArray(new JSONArray(canciones));
        }
        catch (JSONException e)
        {
        }

        adapter.canciones = this.canciones;
        adapter.notifyDataSetChanged();
    }

    public void votarTema(final Cancion cancion)
    {
        //Toast.makeText(getActivity(),"Se va a votar el tema "+cancion.titulo, Toast.LENGTH_SHORT).show();

        //Se buscan las canciones en el WS
        //Se llama al WebService
        //Se activa el progress bar para ir al WS
        ProgressDialog progressDialog = mListener.getProgressDialog();
        progressDialog.setTitle("Sumando Voto");
        progressDialog.setMessage("Actualizando..."); // Setting Message
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
        String url = getResources().getString(R.string.WsUrl) + "/sumarVoto";

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.

                int codigoRetorno = SumarVotoResp.ObtenerCodigo(response);
                switch (codigoRetorno) {

                    case (0):
                    {
                        mListener.recargarPagina();
                        Toast.makeText(getActivity(),SumarVotoResp.ObtenerDescripcion(response), Toast.LENGTH_SHORT).show();

                    }
                    break;
                    case (1):
                    {
                        mListener.recargarPagina();
                        Toast.makeText(getActivity(),"Voto no contabilizado: "+ SumarVotoResp.ObtenerDescripcion(response), Toast.LENGTH_SHORT).show();
                    }
                    break;
                    case (2):
                    {
                        mListener.getProgressDialog().dismiss();
                        Toast.makeText(getActivity(),"Voto no contabilizado: "+ SumarVotoResp.ObtenerDescripcion(response), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }

                //SumarVotoResp.ObtenerCodigo(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(getActivity(),"Hubo un error", Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("_id", cancion._id); //Add the data you'd like to send to the server.
                MyData.put("idEvento", cancion.idEvento); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        MySingleton.getInstance(getActivity()).addToRequestQueue(MyStringRequest);

    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String cancionesString;
    private String mParam1;
    private String mParam2;
    private List<Cancion> canciones;
    CancionVotarAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public CancionesAVotarFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancionesAVotarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CancionesAVotarFragment newInstance(String param1, String param2) {
        CancionesAVotarFragment fragment = new CancionesAVotarFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        //RecyclerView rv = findViewById(R.id.rv);
        return fragment;
    }

    public static CancionesAVotarFragment newInstance(String canciones) {
        CancionesAVotarFragment fragment = new CancionesAVotarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, canciones);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (canciones == null) {
            if (getArguments() != null) {
                cancionesString = getArguments().getString(ARG_PARAM1);
                try {
                    canciones = Cancion.fromJsonArray(new JSONArray(cancionesString));
                } catch (JSONException e) {
                }
                //mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canciones_avotar, container, false);

        // Armo la lista de canciones
        if (canciones == null) {
            if (getArguments() != null) {
                cancionesString = getArguments().getString(ARG_PARAM1);
                try {
                    canciones = Cancion.fromJsonArray(new JSONArray(cancionesString));
                } catch (JSONException e) {
                }
                //mParam2 = getArguments().getString(ARG_PARAM2);
            }
        }

        //ESTO DEBERIA IR EN EL FRAGMENT!!!!!!!!!

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemeDcoration = new DividerItemDecoration(getActivity(),llm.getOrientation());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(dividerItemeDcoration);

        //* Esto se reemplaza por el webservice
        /*
        canciones = new ArrayList<>();
        Cancion cancion = new Cancion("Cancion1", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion2", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion3", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion4", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion24", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion34", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion45", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion23", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion35", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion46", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion22", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion36", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion47", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion22", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion36", 3);
        canciones.add(cancion);
        cancion = new Cancion("Cancion42", 3);
        canciones.add(cancion);
        */

        //*************************************
        adapter = new CancionVotarAdapter(canciones, this);
        rv.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
