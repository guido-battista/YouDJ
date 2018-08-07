package com.battista.guido.youdj.Vistas.ListaVotar;

import android.app.ProgressDialog;
import android.net.Uri;

public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);

    void recargarPagina();

    ProgressDialog getProgressDialog();
}