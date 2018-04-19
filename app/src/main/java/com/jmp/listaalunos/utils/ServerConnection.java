package com.jmp.listaalunos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnection extends AsyncTask<String,Void,String>{
    public static final String HOST_STUDENTS = "sisacweb.com.br/ws/ws_lista_alunos.php";
    public static final String HOST_IMG = "http://sisacweb.com.br/dist/img/fotos-alunos/";

    private static final int CONNECTION_TIME_OUT = 3000;
    private static final int READ_TIME_OUT = 3000;

    private ProgressDialog progressDialog;
    private OnConnectionCompletedListener<Object> onConnectionCompletedListener;
    private String address;
    private Context context;
    private boolean showWaitDialog = true;
    private String dialogMessage = "";

    public ServerConnection(Context context,OnConnectionCompletedListener<Object> onConnectionCompletedListener,String address){
        this.context = context;
        this.onConnectionCompletedListener = onConnectionCompletedListener;
        this.address = address;
    }

    public void shouldShowWaitDialog(boolean showWaitDialog){
        this.showWaitDialog = showWaitDialog;
    }

    public void setDialogMessage(String dialogMessage){
        this.dialogMessage = dialogMessage;
    }

    private static String getData(String address){
        HttpURLConnection connection = null;
        String response = null;
        try{
            URL url = new URL("http://"+address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setDoInput(true);
            connection.connect();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();
            String input;
            while ((input = reader.readLine()) != null){
                stringBuilder.append(input);
            }
            reader.close();

            response = stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(connection != null) {
                try {
                    connection.disconnect();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return response;
    } //Se conecta a um endereço usando o método GET e retorna o resultado

    private String getDataWithPost(String params){
        HttpURLConnection connection = null;
        String response;
        try{
            URL url = new URL("http://"+address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(CONNECTION_TIME_OUT);
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(params);
            writer.close();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String input;
            StringBuilder stringBuilder = new StringBuilder();
            while ((input = reader.readLine()) != null){
                stringBuilder.append(input);
            }
            reader.close();

            response = stringBuilder.toString();
            return response.replaceAll(String.valueOf((char) 65279),""); //Removes BOM Character from String
        }catch (Exception e){
            e.printStackTrace();
            return ConnectivityUtils.CONNECTION_ERROR;
        }
        finally {
            if(connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } //Se conecta a um endereço usando o método POST e retorna o resultado


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(showWaitDialog) {
            if(dialogMessage.isEmpty())
                progressDialog = DialogUtils.getLoadingDialog(context);
            else
                progressDialog = DialogUtils.getLoadingDialog(context, dialogMessage);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        if(ConnectivityUtils.isNetworkConnected(context)){
            if(params[0] != null)
                return getDataWithPost(params[0]);
            else
                return getDataWithPost("");
        }else {
            return ConnectivityUtils.NETWORK_ERROR;
        }
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if(showWaitDialog)
            progressDialog.dismiss();
        onConnectionCompletedListener.onConnectionCompleted(response);
    }
}
