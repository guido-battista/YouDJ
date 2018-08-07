package com.battista.guido.youdj.Volley;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class ErrorManager {

    public static void mostrarErrorConexion (Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Hubo en error en la conexión con el servidor")
                .setMessage("Revise su conexión con internet, o vuelva a intentar en unos minutos.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
