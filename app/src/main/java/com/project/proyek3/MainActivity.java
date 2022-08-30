package com.project.proyek3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> myList;
    String filepath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Halaman Utama");

        listView = findViewById(R.id.listView);
        myList = new ArrayList<>();
        filepath = "DataPraktik4";

        tampilListCatatan();

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(MainActivity.this, InsertViewActivity.class);
            String nama = adapterView.getItemAtPosition(i).toString();
            intent.putExtra("namafile", nama);
            startActivity(intent);
            finish();
        });

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            String nama = adapterView.getItemAtPosition(i).toString();
            dialogHapusCatatan(nama);
            return true;
        });
    }

    private void tampilListCatatan(){
        if(isStoragePermissionGranted()){
            File dir = new File(String.valueOf(getExternalFilesDir(filepath)));
            if(dir.exists()){
                myList.clear();
                File list[] = dir.listFiles();
                for(int i=0;i<list.length;i++){
                    myList.add(list[i].getName());
                }
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, myList);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void dialogHapusCatatan(String namafile){
        new AlertDialog.Builder(this)
                .setTitle("Hapus Catatan")
                .setMessage("Apakah Anda yakin ingin menghapus catatan "+namafile+" ini?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    String state = Environment.getExternalStorageState();
                    if(!Environment.MEDIA_MOUNTED.equals(state)){
                        return;
                    }
                    File file = new File(String.valueOf(getExternalFilesDir(filepath)), namafile);
                    if (file.exists()) {
                        file.delete();
                    }
                    Toast.makeText(MainActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                    tampilListCatatan();
                    listView.invalidateViews();
                })
                .setNegativeButton(android.R.string.no, null).show();
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

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuAdd){
            Intent intent = new Intent(MainActivity.this, InsertViewActivity.class);
            startActivity(intent);
            finish();
        }else if(item.getItemId() == R.id.menu_keluar){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}