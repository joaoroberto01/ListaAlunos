package com.jmp.listaalunos;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class LocalPersistence {

    static void saveStudents(Context context, List<JSONObject> students){
        Gson gson = new Gson();
        Type type = new TypeToken<List<JSONObject>>() {}.getType();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String json = gson.toJson(students);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString("students",json).apply();
    }

    static List<JSONObject> getStudents(Context context){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sharedPreferences.getString("students", "[]");
        Type type = new TypeToken<List<JSONObject>>(){}.getType();

        return gson.fromJson(json, type);
    }
}
