package com.battista.guido.youdj.Modelos;
import org.json.JSONObject;

public class Evento {

    public String _id;
    public String id;
    public String codigo;
    public String estado;
    public String pass;
    public long intervalo;
    public String enPausa;

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
        }
        catch (Exception e)
        {
            return null;
        }
        return evento;
    }
}
