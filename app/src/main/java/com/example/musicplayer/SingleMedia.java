package com.example.musicplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

public class SingleMedia {
    private MediaPlayer player;
    private Boolean ispaused=false;
    private static final SingleMedia ourInstance = new SingleMedia();
    public static SingleMedia getInstance() {
        ourInstance.ispaused=false;
        return ourInstance;
    }
    private SingleMedia() {
    }
    public void play(AudioModel song, Context ctx) {
        if(player != null && !ispaused){
            player.release();
            player=null;
        }
        if(!ispaused){
        this.player=MediaPlayer.create(ctx,Uri.fromFile(new File(song.getaPath())));}
        player.setOnCompletionListener(mediaPlayer -> {
            if(player != null){
                player.release();
                player=null;
            }
        });
        this.player.start();
        ispaused=false;
    }
    public void stop(AudioModel song,Context ctx) {
        this.player.stop();
        this.player=MediaPlayer.create(ctx, Uri.fromFile(new File(song.getaPath())));
    }
    public void pause(){
        this.player.pause();
        this.ispaused=true;
    }
}
