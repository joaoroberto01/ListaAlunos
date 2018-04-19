package com.jmp.listaalunos.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtils {

    public static final String NETWORK_ERROR = "Verifique a conexão com a Internet.";
    public static final String LOCATION_ERROR = "Verifique se a Localização está ativa.";
    public static final String NETWORK_LOCATION_ERROR = "Verifique se a Localização e/ou a conexão com a Internet.";

    public static final String CONNECTION_ERROR = "Um erro ocorreu ao estabelecer a conexão com o servidor.";

    public static final String SUCCESS = "OK";

    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnected();
    } //Retorna True se o usuário estiver conectado à Internet

    public static boolean isLocationOn(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    } //Retorna True se o usuário estiver com a Localização ativa
}
