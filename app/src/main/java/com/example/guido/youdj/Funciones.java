package com.example.guido.youdj;

import android.content.SharedPreferences;
import android.content.Context;

import com.google.firebase.messaging.FirebaseMessaging;

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

    public static boolean getBooleanPrefences (Context context, String clave) {

        SharedPreferences sp = context.getSharedPreferences ("mis_preferencias", MODE_PRIVATE);
        return sp.getBoolean(clave, false);
    }

    public static void setBooleanPreference (Context context, String clave, Boolean valor)
    {
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(clave, valor);
        editor.apply();

        return;
    }

    public static boolean preferenceContains (Context context, String clave)
    {
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        return sp.contains(clave);
    }

    public static void preferencesRemoveKey(Context context, String clave)
    {
        //SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences sp = context.getSharedPreferences("mis_preferencias", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(clave);
        editor.apply();
    }

    public static void unsuscribeAll(Context context)
    {
        String idEvento = Funciones.getStringPreference(context, context.getResources().getString(R.string.preferences_id_evento));
        String claveTopic = context.getResources().getString(R.string.notificacion_votar);
        String topic = idEvento + claveTopic;
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);

        claveTopic = context.getResources().getString(R.string.notificacion_cancion_sonando);
        topic = idEvento + claveTopic;
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
    }
}
