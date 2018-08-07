package com.battista.guido.youdj.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class  SumarVotoResp {

    public static int ObtenerCodigo (String input)
    {
        int resp = 0;
        JSONObject json;

        try
        {
            json = new JSONObject(input);
            resp = Integer.valueOf(json.getString("codigoRetorno"));
        }
        catch (JSONException e) {
        }
        return resp;
    }

    public static String ObtenerDescripcion (String input)
    {
        String resp = "";
        JSONObject json;
        try {
            json = new JSONObject(input);
            resp = json.getString("descripcionRetorno");
        }
        catch (JSONException e)
        {

        }

        return resp;
    }

}
