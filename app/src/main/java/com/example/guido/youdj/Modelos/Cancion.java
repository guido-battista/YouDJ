package com.example.guido.youdj.Modelos;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public static JSONArray filtrarCanciones(JSONArray input, String filtro) {
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

    public static List<Cancion> filtarCancionesPorTitulo (List<Cancion> lista, final String filtro)
    {
        List<Cancion> listaFiltrada = new ArrayList<>();

        for (Cancion cancion:lista)
        {
            if (cancion.titulo.toLowerCase().contains(filtro))
            {
                listaFiltrada.add(cancion);
            }
        }
        return  listaFiltrada;
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
