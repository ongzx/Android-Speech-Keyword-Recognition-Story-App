package com.example.chihiong.bedtimestories;

import android.content.Context;

/**
 * Created by chihi.ong on 18/10/15.
 */
public class Story {

    public String title;
    public String imageName;
    public boolean isFav;

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
    }

}
