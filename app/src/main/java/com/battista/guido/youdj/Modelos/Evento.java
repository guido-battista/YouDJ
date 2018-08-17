package com.battista.guido.youdj.Modelos;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Evento {

    public String _id;
    public String id;
    public String codigo;
    public String estado;
    public String pass;
    public long intervalo;
    public String enPausa;
    public double latitud;
    public double longitud;
    public String nombre;
    public String dj;
    public String lugar;

    public static Evento fromJson (JSONObject json)
    {
        Evento evento = new Evento();
        try
        {
            evento._id = json.getString("_id");
            if (json.has("id"))
                evento.id = json.getString("id");
            if (json.has("codigo"))
                evento.codigo = json.getString("codigo");
            if (json.has("estado"))
                evento.estado = json.getString("estado");
            if (json.has("pass"))
                evento.pass = json.getString("pass");
            if (json.has("intervalo"))
                evento.intervalo = json.getLong("intervalo");
            if (json.has("enPausa"))
                evento.enPausa = json.getString("enPausa");
            if (json.has("latitud"))
                evento.latitud = json.getDouble("latitud");
            if (json.has("longitud"))
                evento.longitud = json.getDouble("longitud");
            if (json.has("nombre"))
                evento.nombre = json.getString("nombre");
            if (json.has("dj"))
                evento.dj = json.getString("dj");
            if (json.has("lugar"))
                evento.lugar = json.getString("lugar");
        }
        catch (Exception e)
        {
            return null;
        }
        return evento;
    }

    public static List<Evento> fromJsonArray (JSONArray jsonArray)
    {
        List<Evento> eventos = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try
            {
                eventos.add(fromJson(jsonArray.getJSONObject(i)));
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
        return eventos;
    }
}
