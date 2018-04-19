package com.jmp.listaalunos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.jmp.listaalunos.adapters.CustomListAdapter;
import com.jmp.listaalunos.utils.OnConnectionCompletedListener;
import com.jmp.listaalunos.utils.ServerConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnConnectionCompletedListener<Object>, AdapterView.OnItemClickListener {
    private EditText searchEditText;
    private ListView studentsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindElements();

    }

    private void bindElements() {
        searchEditText = findViewById(R.id.et_search);

        studentsListView = findViewById(R.id.lv_students);
        studentsListView.setOnItemClickListener(this);

        ImageButton imageButton = findViewById(R.id.ib_search);
        imageButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new ServerConnection(this,this,ServerConnection.HOST_STUDENTS).execute("");
    }

    @Override
    public void onConnectionCompleted(Object response) {
        filter(response.toString());
    }

    private void filter(String response) {
        List<JSONObject> students = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();
        List<String> studentPhotos = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length() ; i++) {
                String search = searchEditText.getText().toString();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.getString("nome_aluno").contains(search)){
                    studentNames.add(jsonObject.getString("nome_aluno"));
                    studentPhotos.add(jsonObject.getString("nome_foto_aluno"));
                    students.add(jsonObject);
                }
            }
            studentsListView.setAdapter(new CustomListAdapter(this,studentNames));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
