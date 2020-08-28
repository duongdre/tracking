package com.example.dss.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dss.R;
import com.example.dss.project.Models.Dispatch;

public class HomeActivity extends AppCompatActivity {

    int checkIfPressBack = 0;
    Button dispatching, map, setting;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        dispatching = findViewById(R.id.btn_dispatching);
        map = findViewById(R.id.btn_map);
        final LoadingDialog loadingDialog = new LoadingDialog(HomeActivity.this);

        dispatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dissmissDialog();
                    }
                }, 7000);
                startActivity(new Intent(HomeActivity.this, DispatchingActivity.class));
                /*Intent intent = new Intent(HomeActivity.this, DispatchingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dissmissDialog();
                    }
                }, 7000);
                startActivity(new Intent(HomeActivity.this, MapsActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        checkIfPressBack++;
        if (checkIfPressBack == 1) {
            Toast.makeText(HomeActivity.this, "Bấm trở lại một lần nữa để thoát",
                    Toast.LENGTH_SHORT).show();
        } else if (checkIfPressBack > 1) {
            finish();
            super.onBackPressed();
        }
    }
}
