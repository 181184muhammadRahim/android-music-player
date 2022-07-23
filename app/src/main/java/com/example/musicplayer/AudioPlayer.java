package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;


import org.jmusixmatch.MusixMatch;
import org.jmusixmatch.entity.lyrics.Lyrics;
import org.jmusixmatch.entity.track.Track;
import org.jmusixmatch.entity.track.TrackData;

public class AudioPlayer extends AppCompatActivity {
    private AudioModel song;
    SingleMedia player;
    private TextView lyrics_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_player);
        Intent intent=getIntent();
        song=(AudioModel) intent.getSerializableExtra("song");
        player=SingleMedia.getInstance();
        lyrics_view=findViewById(R.id.lyrics_display);
        lyrics_view.setMovementMethod(new ScrollingMovementMethod());
        new RetrieveFeedTask().execute();
    }
    private class RetrieveFeedTask extends AsyncTask<Void, Void,Void> {
        private String lyrics;
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                MusixMatch musixMatch=new MusixMatch("07347c3967b469e734c1cd52ad4e6ed7");
                String trackName = "Cheap Thrills";
                String artistName = "Sia";
                Track track = musixMatch.getMatchingTrack(trackName,artistName);
                TrackData data = track.getTrack();
                int trackID=data.getTrackId();
                Lyrics ly = musixMatch.getLyrics(trackID);
                lyrics=ly.getLyricsBody();
            } catch (Exception e) {
                this.lyrics=e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            lyrics_view.setText(this.lyrics);
            super.onPostExecute(unused);
        }
    }


    public void musicstop(View view) {
        player.stop(this.song,this);
    }

    public void musicplay(View view) {
        player.play(this.song,this);
    }

    public void musicpause(View view) {
        this.player.pause();
    }
}
