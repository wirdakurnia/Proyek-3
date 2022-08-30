package com.project.proyek3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private static final String TOKEN_KEY = "key_token";
    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;
    String filepath = "";
    String pass = "";
    ArrayList<String> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        filepath = "UserPraktik4";
        myList = new ArrayList<>();

        btnLogin.setOnClickListener(view -> {
            String username, password;
            username = etUsername.getText().toString();
            password = etPassword.getText().toString();
            if(username.isEmpty() || password.isEmpty()){
                Toast.makeText(LoginActivity.this, "input tidak boleh kosong", Toast.LENGTH_LONG).show();
            }else{
                cekDataLogin(username, password);
                //Toast.makeText(this, "Data Username : " + myList.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void cekDataLogin(String username, String password){
        if(isStoragePermissionGranted()) {
            File file = new File(String.valueOf(getExternalFilesDir(filepath)), username);
            if(file.exists()){
                StringBuilder text = new StringBuilder();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line = br.readLine();

                    while (line != null){
                        text.append(line);
                        line = br.readLine();
                    }
                    br.close();
                } catch (IOException e) {
                    System.out.print("Error : " + e.getMessage());
                }
                pass = text.toString();
            }

            File dir = new File(String.valueOf(getExternalFilesDir(filepath)));
            if(dir.exists()){
                File list[] = dir.listFiles();
                myList.clear();
                for(int i=0;i<list.length;i++){
                    myList.add(list[i].getName());
                }
                if(myList.contains(username) && pass.equals(password)){
                    Toast.makeText(LoginActivity.this, "login berhasil", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "username/password salah", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Permission is granted
                return true;
            } else {
                //Permission is revoked
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            //Permission is granted
            return true;
        }
    }
}