package com.jmp.listaalunos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;

import com.jmp.listaalunos.adapters.CustomListAdapter;
import com.jmp.listaalunos.utils.DialogUtils;
import com.jmp.listaalunos.utils.OnConnectionCompletedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {
    private EditText searchEditText;
    private ListView studentsListView;
    private boolean pussyMode;

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

        Switch swPussy = findViewById(R.id.sw_pussy);
        swPussy.setOnCheckedChangeListener(this);
        swPussy.setChecked(true);

        filter(getJSON());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ib_search) {
            filter(getJSON());
        }else if (v.getId() == R.id.ib_clear){
            searchEditText.setText("");
            filter(getJSON());
        }
    }

    private void filter(String response) {
        List<JSONObject> students = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length() ; i++) {
                String[] search = searchEditText.getText().toString().toUpperCase().split(" ");
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("nome_aluno");
                if(search.length > 1) {
                    if (name.contains(search[0]) && name.contains(search[1])) {
                        studentNames.add(name);
                        students.add(jsonObject);
                    }
                }else {
                    if (name.contains(search[0])) {
                        studentNames.add(name);
                        students.add(jsonObject);
                    }
                }

                if(pussyMode){
                    if(!isPussy(jsonObject.getString("sexo_aluno"))){
                        studentNames.remove(name);
                        students.remove(jsonObject);
                    }
                }
            }

            LocalPersistence.saveStudents(this, students);

            studentsListView.setAdapter(new CustomListAdapter(this,studentNames));
            getSupportActionBar().setTitle("Lista de Alunos ("+ students.size()+")");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isPussy(String gender) {
        return gender.equals("F");
    }

    private void random(){
        Intent intent = new Intent(this,PhotoActivity.class);

        int position = new Random().nextInt(studentsListView.getAdapter().getCount());

        intent.putExtra("position",position);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,PhotoActivity.class);

        intent.putExtra("position",position);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        pussyMode = isChecked;
        filter(getJSON());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.random)
            random();
        return super.onOptionsItemSelected(item);
    }

    private String getJSON() {
        String json;
        try {
            InputStream is = getAssets().open("alunos.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return json;

    }

}
