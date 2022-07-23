package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AudioAdapter extends ArrayAdapter<AudioModel> implements Filterable {
    private final ArrayList<AudioModel> audio_list;
    private ArrayList<AudioModel> filtered_list;
    private Filter filter;
    public AudioAdapter(Context ctx,ArrayList<AudioModel> songs){
        super(ctx,0,songs);
        audio_list=songs;
        filtered_list=songs;
    }
    public AudioModel getItem(int position) {
        return filtered_list.get(position);
    }

    @Override
    public int getCount() {
        return filtered_list.size();
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AudioModel song=getItem(position);
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView=inflater.inflate(R.layout.audio_list_display,parent,false);
        }
        TextView SongTypeView=convertView.findViewById(R.id.song_info);
        assert song != null;
        SongTypeView.setText(song.getaName());
        SongTypeView.setTag(song.getaPath());
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter=new SongFilter();
        }
        return filter;
    }

    private class SongFilter extends Filter {


        @Override
        protected FilterResults performFiltering(CharSequence cs) {
            FilterResults results=new FilterResults();
            if(cs !=null && cs.length()>0 ){
                ArrayList<AudioModel> FilteredValues=new ArrayList<>();
                for (int i = 0; i < filtered_list.size(); i++) {

                    if(audio_list.get(i).getaName().contains(cs)){
                        FilteredValues.add(audio_list.get(i));
                    }
                }
                results.values=FilteredValues;
                results.count=FilteredValues.size();
            }else{
                results.values= audio_list;
                results.count=audio_list.size();
            }
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            filtered_list=(ArrayList<AudioModel>) filterResults.values;
            notifyDataSetChanged();
        }
    }
}
