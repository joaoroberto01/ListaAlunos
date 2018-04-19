package com.jmp.listaalunos.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

public class DialogUtils implements DialogInterface.OnClickListener {

    private static final int DIALOG_POSITIVE = -1;
    public static final int DIALOG_NEGATIVE = -2;
    public static final int DIALOG_NEUTRAL = -3;

    private Activity activity;
    private AlertDialog autenticationDialog, exitDialog, problemsSelectionDialog, disconnectDialog,locationDialog;

    public DialogUtils(Activity activity) {
        this.activity = activity;
    }

    public void showExitDialog() {
        exitDialog = new AlertDialog.Builder(activity).create();
        exitDialog.setTitle("Sair do App?");
        exitDialog.setMessage("Deseja sair do app?");
        exitDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Não", this);
        exitDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sim", this);
        exitDialog.show();
    }

    public static void showAlert(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snack = Snackbar.make(rootView,message,Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        snack.show();
    }

    static ProgressDialog getLoadingDialog(Context context){
        return ProgressDialog.show(context,"Aguarde...",
                "Isso depende da velocidade da sua conexão.",true,true);
    }

    static ProgressDialog getLoadingDialog(Context context, String message){
        return ProgressDialog.show(context,"Aguarde...", message,true,true);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(dialog == exitDialog){
            if(which == DIALOG_POSITIVE) {
                activity.finishAffinity();
            }
        }
    }
}
