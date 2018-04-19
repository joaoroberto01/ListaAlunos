package com.jmp.listaalunos;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.jmp.listaalunos.adapters.CustomListAdapter;
import com.jmp.listaalunos.utils.DialogUtils;
import com.jmp.listaalunos.utils.OnConnectionCompletedListener;
import com.jmp.listaalunos.utils.ServerConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnConnectionCompletedListener<Object>, AdapterView.OnItemClickListener {
    private EditText searchEditText;
    private ListView studentsListView;
    private List<JSONObject> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindElements();
    }

    @Override
    public void onBackPressed() {
        new DialogUtils(this).showExitDialog();
    }

    private void bindElements() {
        searchEditText = findViewById(R.id.et_search);

        studentsListView = findViewById(R.id.lv_students);
        studentsListView.setOnItemClickListener(this);

        ImageButton searchImageButton = findViewById(R.id.ib_search);
        searchImageButton.setOnClickListener(this);

        ImageButton clearImageButton = findViewById(R.id.ib_clear);
        clearImageButton.setOnClickListener(this);

        new ServerConnection(this,this,ServerConnection.HOST_STUDENTS).execute("");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ib_search) {
            new ServerConnection(this, this, ServerConnection.HOST_STUDENTS).execute("");
        }else if (v.getId() == R.id.ib_clear){
            searchEditText.setText("");
            new ServerConnection(this,this,ServerConnection.HOST_STUDENTS).execute("");
        }
    }

    @Override
    public void onConnectionCompleted(Object response) {
        filter(response.toString());
    }

    private void filter(String response) {
        students = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length() ; i++) {
                String search = searchEditText.getText().toString().toUpperCase();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("nome_aluno").startsWith(search)){
                    studentNames.add(jsonObject.getString("nome_aluno"));
                    students.add(jsonObject);
                }
            }
            Collections.sort(studentNames);
            Collections.sort(students, new Comparator<JSONObject>() {
                private static final String KEY_NAME = "nome_aluno";

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA = "";
                    String valB = "";

                    try {
                        valA = (String) a.get(KEY_NAME);
                        valB = (String) b.get(KEY_NAME);
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return valA.compareTo(valB);
                }
            });
            studentsListView.setAdapter(new CustomListAdapter(this,studentNames));
            getSupportActionBar().setTitle("Lista de Alunos ("+students.size()+")");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(this,PhotoActivity.class);
            intent.putExtra("photo",students.get(position).getString("nome_foto_aluno"));
            intent.putExtra("name",students.get(position).getString("nome_aluno"));
            startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
