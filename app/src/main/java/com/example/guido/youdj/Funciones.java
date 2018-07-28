package com.example.guido.youdj;

import android.content.SharedPreferences;
import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

public class Funciones {

    public static String getStringPreference (Context context, String clave) {

        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        return sp.getString(clave, "default value");
    }

    public static void setStringPreference (Context context, String clave, String valor)
    {
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(clave, valor);
        editor.apply();

        return;
    }

    public static int getIntPreference (Context context, String clave) {

        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        return sp.getInt(clave, -1);
    }

    public static void setIntPreference (Context context, String clave, int valor)
    {
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(clave, valor);
        editor.apply();

        return;
    }

    public static long getLongPreference (Context context, String clave) {

        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        return sp.getLong(clave, -1);
    }

    public static void setLongPreference (Context context, String clave, long valor)
    {
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(clave, valor);
        editor.apply();

        return;
    }

    public static boolean preferenceContains (Context context, String clave)
    {
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        return sp.contains(clave);
    }

}
