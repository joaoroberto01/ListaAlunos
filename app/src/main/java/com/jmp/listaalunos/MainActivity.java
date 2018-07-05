package com.jmp.listaalunos;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.jmp.listaalunos.adapters.CustomListAdapter;
import com.jmp.listaalunos.utils.DialogUtils;
import com.jmp.listaalunos.utils.OnConnectionCompletedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, DialogInterface.OnClickListener {
    private static final int POSITIVE_BUTTON = -1;

    private EditText searchEditText;
    private ListView studentsListView;
    private SharedPreferences sharedPreferences;

    private Switch swPussy,swBird, swAdm, swCont,swInfo,swSeg, swFirst,swSecond, swThird,swFourth;

    private List<String> genderFilter = new ArrayList<>();
    private List<String> yearFilter = new ArrayList<>(Arrays.asList("1*","2*","3*","4*"));
    private List<String> courseFilter = new ArrayList<>(Arrays.asList("ADM","CONT","INFO","SEG"));

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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        searchEditText = findViewById(R.id.et_search);

        studentsListView = findViewById(R.id.lv_students);
        studentsListView.setOnItemClickListener(this);

        ImageButton searchImageButton = findViewById(R.id.ib_search);
        searchImageButton.setOnClickListener(this);

        ImageButton clearImageButton = findViewById(R.id.ib_clear);
        clearImageButton.setOnClickListener(this);

        filter();
    }

    private void filter() {
        setFilterConfiguration();
        List<JSONObject> students = new ArrayList<>();
        List<String> studentNames = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(getJSON());
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

                if(!genderFilter.contains(jsonObject.getString("sexo_aluno")) ||
                        !yearFilter.contains(jsonObject.getString("ano_aluno")) ||
                        !courseFilter.contains(jsonObject.getString("curso_aluno"))){
                    studentNames.remove(name);
                    students.remove(jsonObject);
                }
            }

            LocalPersistence.saveStudents(this, students);

            studentsListView.setAdapter(new CustomListAdapter(this,studentNames));
            getSupportActionBar().setTitle("Lista de Alunos ("+ students.size()+")");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void random(){
        Intent intent = new Intent(this,PhotoActivity.class);

        int position = new Random().nextInt(studentsListView.getAdapter().getCount());

        intent.putExtra("position",position);
        startActivity(intent);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,PhotoActivity.class);

        intent.putExtra("position",position);
        startActivity(intent);
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
        else if(item.getItemId() == R.id.filter)
            showFilterDialog();
        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Filtrar Por");
        LayoutInflater inflater = LayoutInflater.from(this);
        View filterView = inflater.inflate(R.layout.filter_dialog, null, false);
        alertDialog.setView(filterView);
        alertDialog.setPositiveButton("OK",this);
        alertDialog.setNegativeButton("Cancelar",this);
        alertDialog.show();

        swPussy = filterView.findViewById(R.id.sw_pussy);
        swPussy.setChecked(getBoolean("pussy"));

        swBird = filterView.findViewById(R.id.sw_bird);
        swBird.setChecked(getBoolean("bird"));

        swAdm = filterView.findViewById(R.id.sw_adm);
        swAdm.setChecked(getBoolean("adm"));

        swCont = filterView.findViewById(R.id.sw_cont);
        swCont.setChecked(getBoolean("cont"));

        swInfo = filterView.findViewById(R.id.sw_info);
        swInfo.setChecked(getBoolean("info"));

        swSeg = filterView.findViewById(R.id.sw_seg);
        swSeg.setChecked(getBoolean("seg"));

        swFirst = filterView.findViewById(R.id.sw_1);
        swFirst.setChecked(getBoolean("first"));

        swSecond = filterView.findViewById(R.id.sw_2);
        swSecond.setChecked(getBoolean("second"));

        swThird = filterView.findViewById(R.id.sw_3);
        swThird.setChecked(getBoolean("third"));

        swFourth = filterView.findViewById(R.id.sw_4);
        swFourth.setChecked(getBoolean("fourth"));
    }

    private boolean getBoolean(String key){
        return sharedPreferences.getBoolean(key,true);
    }

    private void setFilterConfiguration(){
        if(getBoolean("pussy") && !genderFilter.contains("F"))
            genderFilter.add("F");
        else if(!getBoolean("pussy"))
            genderFilter.remove("F");

        if(getBoolean("bird") && !genderFilter.contains("M"))
            genderFilter.add("M");
        else if(!getBoolean("bird"))
            genderFilter.remove("M");

        if (getBoolean("first") && !yearFilter.contains("1*"))
            yearFilter.add("1*");
        else if(!getBoolean("first"))
            yearFilter.remove("1*");

        if (getBoolean("second") && !yearFilter.contains("2*"))
            yearFilter.add("2*");
        else if(!getBoolean("second"))
            yearFilter.remove("2*");

        if (getBoolean("third") && !yearFilter.contains("3*"))
            yearFilter.add("3*");
        else if(!getBoolean("third"))
            yearFilter.remove("3*");

        if (getBoolean("fourth") && !yearFilter.contains("4*"))
            yearFilter.add("4*");
        else if(!getBoolean("fourth"))
            yearFilter.remove("4*");

        if (getBoolean("adm") && !courseFilter.contains("ADM"))
            courseFilter.add("ADM");
        else if(!getBoolean("adm"))
            courseFilter.remove("ADM");

        if (getBoolean("cont") && !courseFilter.contains("CONT"))
            courseFilter.add("CONT");
        else if(!getBoolean("cont"))
            courseFilter.remove("CONT");

        if (getBoolean("info") && !courseFilter.contains("INFO"))
            courseFilter.add("INFO");
        else if(!getBoolean("info"))
            courseFilter.remove("INFO");

        if (getBoolean("seg") && !courseFilter.contains("SEG"))
            courseFilter.add("SEG");
        else if(!getBoolean("seg"))
            courseFilter.remove("SEG");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ib_search)
            filter();
        else if (v.getId() == R.id.ib_clear){
            searchEditText.setText("");
            filter();
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == POSITIVE_BUTTON) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("pussy", swPussy.isChecked());
            editor.putBoolean("bird", swBird.isChecked());
            editor.putBoolean("first", swFirst.isChecked()).putBoolean("second", swSecond.isChecked());
            editor.putBoolean("third", swThird.isChecked()).putBoolean("fourth", swFourth.isChecked());
            editor.putBoolean("adm", swAdm.isChecked()).putBoolean("cont", swCont.isChecked());
            editor.putBoolean("info", swInfo.isChecked()).putBoolean("seg", swSeg.isChecked()).apply();

            filter();
        }
    }
}
