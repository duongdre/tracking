package com.example.dss.project.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dss.R;

public class StartActivity extends AppCompatActivity {
    Button btn_login;
    TextView edtxt_register;
    EditText userid, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        btn_login = findViewById(R.id.loginButton);
        edtxt_register = findViewById(R.id.registerButton);
        userid = findViewById(R.id.txtuserlogin);
        password = findViewById(R.id.txtpasswordlogin);

        //SetText for Testing
        userid.setText("DSS");
        password.setText("123456");

        // Login
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String txt_userid = userid.getText().toString();
                String txt_password = password.getText().toString();
                if (TextUtils.isEmpty(txt_userid) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(StartActivity.this, "Vui lòng điền đầy đủ Tên đăng nhập và Mật khẩu", Toast.LENGTH_SHORT).show();
                }else{
                    if(txt_userid.equals("DSS")&& txt_password.equals("123456")){
                        //startActivity(new Intent(StartActivity.this, HomeActivity.class));
                        Intent intent = new Intent(StartActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }else{
                        Toast.makeText(StartActivity.this, "Lỗi xác thực", Toast.LENGTH_SHORT).show();
                    }
                }
                }
                //startActivity(new Intent(StartActivity.this, MapsActivity.class));
        });

        //Register
        edtxt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, HomeActivity.class));
            }
        });
    }
}
