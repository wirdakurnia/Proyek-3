package com.project.proyek3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = RegisterActivity.class.getName();
    EditText etUsername, etPassword, etUlang;
    Button btnRegister;
    ImageView back;
    String filepath = "";
    ArrayList<String> myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword =findViewById(R.id.et_password);
        etUlang = findViewById(R.id.et_passwordulang);
        back = findViewById(R.id.btn_back);
        btnRegister = findViewById(R.id.btn_register);
        filepath = "UserPraktik4";
        myList = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, password, ulang;
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                ulang = etUlang.getText().toString();
                if(isStoragePermissionGranted()){
                    if(username.isEmpty() || password.isEmpty() || ulang.isEmpty()){
                        Toast.makeText(RegisterActivity.this, "input tidak boleh kosong", Toast.LENGTH_LONG).show();
                    }else if(!password.equals(ulang)){
                        Toast.makeText(RegisterActivity.this, "password tidak sama", Toast.LENGTH_LONG).show();
                    }else {
                        tampilkanDialogKonfirmasiPenyimpanan();
                    }
                }
            }
        });
    }

    public void tambahAkun(){
        File parent = new File(String.valueOf(getExternalFilesDir(filepath)));
        if(parent.exists()){
            File list[] = parent.listFiles();
            myList.clear();
            for(int i=0;i<list.length;i++){
                myList.add(list[i].getName());
            }
            if(myList.contains(etUsername.getText().toString())){
                Toast.makeText(this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show();
            }else{
                File file = new File(String.valueOf(getExternalFilesDir(filepath)), etUsername.getText().toString());
                FileOutputStream outputStream = null;
                try{
                    file.createNewFile();
                    outputStream = new FileOutputStream(file);
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    streamWriter.append(etPassword.getText());
                    streamWriter.flush();
                    streamWriter.close();
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegisterActivity.this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }else {
            parent.mkdir();
            File list[] = parent.listFiles();
            myList.clear();
            for(int i=0;i<list.length;i++){
                myList.add(list[i].getName());
            }
            if(myList.contains(etUsername.getText().toString())){
                Toast.makeText(this, "Username sudah terdaftar", Toast.LENGTH_SHORT).show();
            }else{
                File file = new File(String.valueOf(getExternalFilesDir(filepath)), etUsername.getText().toString());
                FileOutputStream outputStream = null;
                try{
                    file.createNewFile();
                    outputStream = new FileOutputStream(file);
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    streamWriter.append(etPassword.getText());
                    streamWriter.flush();
                    streamWriter.close();
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(RegisterActivity.this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void tampilkanDialogKonfirmasiPenyimpanan(){
        new AlertDialog.Builder(this)
                .setTitle("Register Akun")
                .setMessage("Apakah Anda yakin ingin menyimpan ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tambahAkun();
                        clearAll();

                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void clearAll(){
        etUsername.setText("");
        etPassword.setText("");
        etUlang.setText("");
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