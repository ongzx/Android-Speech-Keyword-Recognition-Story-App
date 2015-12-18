package com.example.chihiong.bedtimestories;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class KeySoundAdapter extends RecyclerView.Adapter <KeySoundAdapter.KeySoundHolder> {
    private final ArrayList mData;
    private Map<String, String> mMap;

    public KeySoundAdapter(Map<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
        mMap = map;
    }

    public void clear() {
        mMap.clear();
        mData.clear();
        mData.addAll(mMap.entrySet());
    }

    public void put(String key, String value) {
        mMap.put(key, value);
        mData.clear();
        mData.addAll(mMap.entrySet());
    }

    public boolean containsKey(Object key) {
        return mMap.containsKey(key);
    }

    public String get(Object key) {
        return mMap.get(key);
    }

    public int size() {
        return mMap.size();
    }

    public Set<Map.Entry<String, String>> entrySet() {
        return mMap.entrySet();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder (final KeySoundHolder keySoundHolder, int position) {

        Map.Entry<String, String> item = getItem(position);

        int index = item.getKey().indexOf('/');
        String sound_title = item.getKey().substring(0, index);
        Log.v("test", "bind view");
        keySoundHolder.text.setText(toTitleCase(sound_title));

    }


    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public KeySoundHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_key_sound, viewGroup, false);
        return new KeySoundHolder(itemView);
    }

    public class KeySoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout textHolder;
        public TextView text;


        public KeySoundHolder (View v) {
            super(v);
            v.setOnClickListener(this);

            text = (TextView) itemView.findViewById(R.id.text);
            textHolder = (LinearLayout) itemView.findViewById(R.id.textHolder);
        }

        @Override
        public void onClick(View view) {


        }
    }
}
