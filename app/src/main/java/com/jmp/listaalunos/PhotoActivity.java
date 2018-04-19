package com.jmp.listaalunos;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jmp.listaalunos.utils.DialogUtils;

import static com.jmp.listaalunos.utils.ServerConnection.HOST_IMG;

public class PhotoActivity extends AppCompatActivity implements RequestListener<Drawable> {
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        bindElements();
    }

    private void bindElements() {
        ImageView studentImageView = findViewById(R.id.iv_student);

        loadingProgressBar = findViewById(R.id.pb_loading);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String photo = getIntent().getStringExtra("photo");
        if (photo != null){
            getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
            Glide.with(this).load(HOST_IMG + photo).listener(this).into(studentImageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        DialogUtils.showAlert(this,e.getMessage());
        loadingProgressBar.setVisibility(View.GONE);
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        loadingProgressBar.setVisibility(View.GONE);
        return false;
    }
}
