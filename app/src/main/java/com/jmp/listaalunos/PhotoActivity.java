package com.jmp.listaalunos;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jmp.listaalunos.utils.DialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import static com.jmp.listaalunos.utils.ServerConnection.HOST_IMG;

public class PhotoActivity extends AppCompatActivity {
    private ImageView studentImageView;
    private int position;
    private List<JSONObject> students;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        bindElements();
    }

    private void bindElements() {
        studentImageView = findViewById(R.id.iv_student);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_quit);

        position = getIntent().getIntExtra("position",0);
        students = LocalPersistence.getStudents(this);

        findViewById(R.id.fab_random).setOnClickListener(v -> setStudent(new Random().nextInt(students.size())));

        setStudent(position);
    }

    private void setStudent(int position){ 
        if(position >= students.size()){
            this.position = 0;
            position = 0;
        }else if(position < 0) {
            this.position = students.size() -1;
            position = students.size() -1;
        }
        if (snackbar != null && snackbar.isShown())
            snackbar.dismiss();
        JSONObject student = students.get(position);
        try {
            String name = student.getString("nome_aluno");
            getSupportActionBar().setTitle(name);

            String course = student.getString("ano_aluno").replace("*","º");
            course += " "+student.getString("curso_aluno");

            ((TextView)findViewById(R.id.tv_name)).setText(name);
            ((TextView)findViewById(R.id.tv_course)).setText(course);

            Context context = studentImageView.getContext();
            int id = context.getResources().getIdentifier(name.replace(" ", "_").toLowerCase(), "drawable", context.getPackageName());
            if (id == 0) {
                View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                snackbar = Snackbar.make(rootView, "Foto Indisponível", Snackbar.LENGTH_INDEFINITE).setAction("OK", v->{});
                snackbar.show();
            }

            studentImageView.setImageResource(id);
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        else if(item.getItemId() == R.id.next)
            setStudent(++position);
        else if(item.getItemId() == R.id.previous)
            setStudent(--position);
        return super.onOptionsItemSelected(item);
    }
}
