package com.example.musicplayer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AudioList extends AppCompatActivity {
    private ArrayList<AudioModel> AudioList;
    private ListView list_display;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Boolean permission_flag=false;
    private AudioModel will_play_song;
    private AudioAdapter adapter;
    private TextView search_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
        if(permission_flag){
        this.AudioList=getPlayList(Environment.getExternalStorageDirectory().getAbsolutePath());

        }else{
            this.AudioList=new ArrayList<>();
        }
        list_display=findViewById(R.id.listMode);
        fillitems();
        search_bar=findViewById(R.id.search_bar);
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Filter fil=adapter.getFilter();
                fil.filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    void fillitems(){
        adapter = new AudioAdapter(this,AudioList);
        list_display.setAdapter(adapter);
    }
    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(AudioList.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(AudioList.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(AudioList.this, "Permission already granted", Toast.LENGTH_SHORT).show();
            this.permission_flag=true;
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.permission_flag=true;
                Toast.makeText(AudioList.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(AudioList.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ArrayList<AudioModel> getPlayList(String rootPath) {
        ArrayList<AudioModel> fileList = new ArrayList<>();


        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles(); //here you will get NPE if directory doesn't contains  any file,handle it like this.
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getPlayList(file.getAbsolutePath()) != null) {
                        fileList.addAll(getPlayList(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".mpeg")) {
                    AudioModel song = new AudioModel();
                    /*MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
                    Uri uri = (Uri) Uri.fromFile(file);
                    mediaMetadataRetriever.setDataSource(AudioList.this, uri);
                    String artist = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);*/
                    song.setaPath(file.getAbsolutePath());
                    song.setaName(file.getName());
                    Log.e("path",song.getaPath());
                    fileList.add(song);
                }
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    public void playmusic(View view) {
        TextView v=(TextView) view;
        String path=(String) v.getTag();
        String name=(String) v.getText();
        will_play_song=new AudioModel();
        will_play_song.setaName(name);
        will_play_song.setaPath(path);
        openSomeActivityForResult();
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Toast.makeText(AudioList.this,"came back from activity",Toast.LENGTH_SHORT).show();
                }
            });
    public void openSomeActivityForResult() {
        Intent intent = new Intent(this, AudioPlayer.class);
        intent.putExtra("song",will_play_song);
        someActivityResultLauncher.launch(intent);
    }
}