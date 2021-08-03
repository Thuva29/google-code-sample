package com.google;
import java.util.List;
import java.util.ArrayList;

/** A class used to represent a Playlist */
class VideoPlaylist {
    private String name;
    private List<Video> videos;

    public VideoPlaylist(String name){
        this.name = name;
        this.videos = new ArrayList<>();
    }
    
    public String getName(){
        return name;
    }

    public void addVideoToPlaylist(Video v, String playlistName){
        int sameVideos = 0;
        for(int i = 0; i<videos.size() ; i++){
            if(videos.get(i).equals(v)){
                System.out.println("Cannot add video to " + playlistName + ": Video already added"); 
                sameVideos++;
            }
        }
        if(sameVideos == 0){
            videos.add(v);
            System.out.println("Added video to " + playlistName + ": " + v.getTitle());
        }
    }

    public void removeVideoFromPlaylist(Video v, String playlistName){
        int videosRemoved = 0;
        for(int i = 0; i<videos.size(); i++){
            if(videos.get(i).equals(v)){
                videos.remove(i);
                System.out.println("Removed video from " + playlistName + ": " + v.getTitle());
                videosRemoved++;
            }
        }
        if(videosRemoved == 0){
            System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
        }
    }

    public void clearPlaylist(String playlistName){
        videos.clear();
        System.out.println("Successfully removed all videos from " + playlistName);
    }

    public List<Video> getVideos(){
        return videos;
    }
}
