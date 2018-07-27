package com.example.guido.youdj.Modelos;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class Cancion {

    public String _id;
    public String idEvento;
    public String titulo;
    public int votos;
    public String estado;

    public Cancion(String titulo, int votos)
    {
        this.titulo = titulo;
        this.votos = votos;
    }

    public Cancion()
    {
    }

    public static Cancion fromJson(JSONObject json)
    {
        Cancion cancion = new Cancion();
        try
        {
            cancion._id = json.getString("_id");
            if (json.has("idEvento"))
                cancion.idEvento = json.getString("idEvento");
            if (json.has("titulo"))
                cancion.titulo = json.getString("titulo");
            if (json.has("votos"))
                cancion.votos = json.getInt("votos");
            if (json.has("estado"))
                cancion.estado = json.getString("estado");

        }
        catch (Exception e)
        {
            return null;
        }
        return cancion;
    }

    public static List<Cancion> fromJsonArray (JSONArray jsonArray)
    {
        List<Cancion> canciones = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try
            {
                canciones.add(fromJson(jsonArray.getJSONObject(i)));
            }
            catch (JSONException e)
            {
                return null;
            }
            catch (Exception e)
            {
                return  null;
            }
        }
        return canciones;
    }



    public String getTitulo()
    {
        return this.titulo;
    }

    public int getVotos()
    {
        return this.votos;
    }
}
