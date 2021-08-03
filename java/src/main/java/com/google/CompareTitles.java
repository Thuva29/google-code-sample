package com.google;
import java.util.Comparator;
class CompareTitles implements Comparator<Video>{
    public int compare(Video one, Video two){
        return one.getTitle().compareTo(two.getTitle());
    }
}