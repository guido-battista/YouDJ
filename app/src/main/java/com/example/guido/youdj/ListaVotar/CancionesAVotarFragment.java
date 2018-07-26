package com.example.guido.youdj.ListaVotar;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.AppCompatActivity;

import com.example.guido.youdj.Modelos.Cancion;
import com.example.guido.youdj.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CancionesAVotarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CancionesAVotarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancionesAVotarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    public static CancionesAVotarFragment newInstance() {
        CancionesAVotarFragment fragment = new CancionesAVotarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canciones_avotar, container, false);

        //ESTO DEBERIA IR EN EL FRAGMENT!!!!!!!!!
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        //* Esto se reemplaza por el webservice
        List<Cancion> canciones = new ArrayList<>();
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


        //*************************************
        CancionVotarAdapter adapter = new CancionVotarAdapter(canciones);
        rv.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
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
    */

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
