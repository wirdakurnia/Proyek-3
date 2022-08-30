package com.project.proyek3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class InsertViewActivity extends AppCompatActivity {
    private final String TAG = InsertViewActivity.class.getName();
    EditText etNama, etIsi;
    Button simpan;
    String namafile, isicatatan;
    String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_view);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etNama = findViewById(R.id.etNamaFile);
        etIsi = findViewById(R.id.etCatatan);
        simpan = findViewById(R.id.btnSimpan);
        filepath = "DataPraktik4";

        simpan.setOnClickListener(view -> {

            if(etNama.getText().toString().equals("") || etIsi.getText().toString().equals("")){
                Toast.makeText(InsertViewActivity.this, "tidak boleh kososng",Toast.LENGTH_LONG).show();
            }else if(isicatatan.equals(etIsi.getText().toString())){
                new AlertDialog.Builder(InsertViewActivity.this)
                        .setTitle("Tidak Ada Perubahan")
                        .setMessage("Apakah Anda yakin ingin menyimpan catatan ini?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                buatDanUbah();
                                Toast.makeText(InsertViewActivity.this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(InsertViewActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }else{
                tampilkanDialogKonfirmasiPenyimpanan();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            namafile = extras.getString("namafile");
            etNama.setText(namafile);
            getSupportActionBar().setTitle("Ubah Catatan");
        }else {
            getSupportActionBar().setTitle("Tambah Catatan");
        }
        bacaCatatan();
    }

    public void bacaCatatan(){
        File file = new File(String.valueOf(getExternalFilesDir(filepath)), etNama.getText().toString());
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
            isicatatan = text.toString();
            etIsi.setText(text.toString());
        }
    }

    public void buatDanUbah(){
        File parent = new File(String.valueOf(getExternalFilesDir(filepath)));
        if(parent.exists()){
            if(!isicatatan.equals(etIsi.getText().toString())){
                File file = new File(String.valueOf(getExternalFilesDir(filepath)), etNama.getText().toString());
                FileOutputStream outputStream = null;
                try{
                    file.createNewFile();
                    outputStream = new FileOutputStream(file);
                    OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                    streamWriter.append(etIsi.getText());
                    streamWriter.flush();
                    streamWriter.close();
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            parent.mkdir();
            File file = new File(String.valueOf(getExternalFilesDir(filepath)), etNama.getText().toString());
            Log.d(TAG, "create file: "+getFilesDir().getAbsolutePath());
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file, false);

                outputStream.write(etIsi.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void tampilkanDialogKonfirmasiPenyimpanan(){
        new AlertDialog.Builder(this)
                .setTitle("Simpan Catatan")
                .setMessage("Apakah Anda yakin ingin menyimpan Catatan ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buatDanUbah();
                        Toast.makeText(InsertViewActivity.this, "Berhasil disimpan", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(InsertViewActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(InsertViewActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}