package com.example.chihiong.bedtimestories;

import java.util.ArrayList;

/**
 * Created by chihi.ong on 21/10/15.
 */
public class StoryData {

    public static String[] storyNameArray = { "The Little Prince", "The Little Mermaid", "The Ugly Duckling", "Hansel And Gretel"};

    public static ArrayList<Story> storyList() {
        ArrayList<Story> list = new ArrayList<>();
        for (int i = 0; i < storyNameArray.length; i++) {
            Story story = new Story();
            story.title = storyNameArray[i];
            story.imageName = storyNameArray[i].replaceAll("\\s+", "").toLowerCase();
            if (i == 2 || i == 5) {
                story.isFav = true;
            }
            list.add(story);
        }
        return (list);
    }
}
