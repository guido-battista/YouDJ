package com.example.guido.youdj.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CancionSonandoResp {

    public static int ObtenerCodigo (JSONObject json)
    {
        int resp = 0;

        try
        {
            resp = Integer.valueOf(json.getString("codigoRetorno"));
        }
        catch (JSONException e) {
        }
        return resp;
    }

    public static String ObtenerDescripcion (JSONObject json)
    {
        String resp = "";
        try {
            resp = json.getString("descripcionRetorno");
        }
        catch (JSONException e)
        {

        }

        return resp;
    }

    public static String ObtenerCancionSonando (JSONObject json)
    {
        String resp = "";
        try {
            resp = json.getString("cancionSonando");
        }
        catch (JSONException e)
        {

        }

        return resp;
    }
}
