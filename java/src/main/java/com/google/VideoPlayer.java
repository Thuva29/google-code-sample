package com.google;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private List<Video> allVideos;
  private List<VideoPlaylist> allPlaylists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    allVideos = videoLibrary.getVideos();
    allPlaylists = new ArrayList<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", allVideos.size());
  }

  public void showAllVideos() {
    System.out.println("Here's a list of all available videos:");
    Collections.sort(allVideos, new CompareTitles());
    for(int i = 0; i<allVideos.size(); i++){
      String tagString = getTagsAsString(allVideos.get(i).getTags());
      System.out.println(allVideos.get(i).getTitle() + " (" + allVideos.get(i).getVideoId() + ") [" + tagString + "]" + generateFlagStatusMarker(allVideos.get(i)));
    }
  }

  public void playVideo(String videoId) {
      Video toBePlayed = videoLibrary.getVideo(videoId);
      
      if(toBePlayed != null){
        if(toBePlayed.isItFlagged() == false){
          for(int i = 0; i<allVideos.size(); i++){
            if(allVideos.get(i).isItPlaying() == true){
              allVideos.get(i).stopPlaying();
            }
          }
        toBePlayed.startPlaying();
        }
        else{
          System.out.println("Cannot play video: Video is currently flagged (reason: " + toBePlayed.returnFlagReason() + ")");
        }
      }
      else{
        System.out.println("Cannot play video: Video does not exist");
      }
  }

  public void stopVideo() {
    int videosPlaying = 0;
    for(int i = 0; i<allVideos.size(); i++){
      if(allVideos.get(i).isItPlaying() == true){
        allVideos.get(i).stopPlaying();
        videosPlaying++;
      }
    }
    if(videosPlaying == 0){
      System.out.println("Cannot stop video: No video is currently playing");
    }
  }

  public void playRandomVideo() {
     Random random = new Random();
     try{
      Video randomVideo = allVideos.get(random.nextInt(allVideos.size()));
      
      if(!areAllVideosFlagged()){
        while(randomVideo.isItFlagged() == true){
          randomVideo = allVideos.get(random.nextInt(allVideos.size()));
        }

        for(int i = 0; i<allVideos.size(); i++){
          if(allVideos.get(i).isItPlaying() == true){
            allVideos.get(i).stopPlaying();
          }
        }
        randomVideo.startPlaying();
      }
      else{
        System.out.println("No videos available");
      }

     }
     catch(NullPointerException e){
       System.out.println("No videos available");
     }  
  }

  public void pauseVideo() {
    int videosPlaying = 0;
    for(int i = 0; i<allVideos.size(); i++){
      if(allVideos.get(i).isItPlaying() == true){
        allVideos.get(i).pauseVid();
        videosPlaying++;
      }
    }
    if(videosPlaying == 0){
      System.out.println("Cannot pause video: No video is currently playing");
    }

  }

  public void continueVideo() {
    int videosPlaying = 0;
    for(int i = 0; i<allVideos.size(); i++){
      if(allVideos.get(i).isItPaused() == true){
        allVideos.get(i).continueVid();
        videosPlaying++;
      }
      else if(allVideos.get(i).isItPlaying() == true & allVideos.get(i).isItPaused() == false){
        System.out.println("Cannot continue video: Video is not paused");
        videosPlaying++;
      }  
    }
    if(videosPlaying == 0){
        System.out.println("Cannot continue video: No video is currently playing");
    }
  }

  public void showPlaying() {
    int videosPlaying = 0;
    for(int i = 0; i<allVideos.size(); i++){
      if(allVideos.get(i).isItPlaying() == true){
        String tagString = getTagsAsString(allVideos.get(i).getTags());
        System.out.print("Currently playing: " + allVideos.get(i).getTitle() + " (" + allVideos.get(i).getVideoId() + ") [" + tagString +"]");
        if(allVideos.get(i).isItPaused() == true){
          System.out.print(" - PAUSED");
        }
        System.out.print("\n");
        videosPlaying++;
      }
    }
    if(videosPlaying == 0){
      System.out.println("No video is currently playing");
    }
  }

  public void createPlaylist(String playlistName) {
    int sameNamedPlaylists = 0;
    for(int i = 0; i<allPlaylists.size(); i++){
        if(allPlaylists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())){
           System.out.println("Cannot create playlist: A playlist with the same name already exists");
           sameNamedPlaylists++;
        }
    }
    if(sameNamedPlaylists == 0){
      allPlaylists.add(new VideoPlaylist(playlistName));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
    
  } 

  public void addVideoToPlaylist(String playlistName, String videoId) {
    VideoPlaylist selectedPlaylist = null;
    for(int i = 0; i<allPlaylists.size(); i++){
      if(allPlaylists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())){
        selectedPlaylist = allPlaylists.get(i);
        break;
      } 
    }
    
    if(selectedPlaylist != null){
      Video v = videoLibrary.getVideo(videoId);
      if(v != null){
        if(v.isItFlagged() == false){
          selectedPlaylist.addVideoToPlaylist(v, playlistName);
        }
        else{
          System.out.println("Cannot add video to my_playlist: Video is currently flagged (reason: " + v.returnFlagReason() + ")");
        }
      }
      else{
        System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
      }
    }
    else{
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    }
  }

  public void showAllPlaylists() {
    ArrayList<String> allPlaylistNames = new ArrayList<>();
    if(allPlaylists.size() != 0){
      for(int i = 0; i<allPlaylists.size(); i++){
        allPlaylistNames.add(allPlaylists.get(i).getName());
      }
    
      Collections.sort(allPlaylistNames);
      System.out.println("Showing all playlists: ");
    
      for(int i = 0; i<allPlaylistNames.size(); i++){
        System.out.println(allPlaylistNames.get(i));
      }
    }
    else{
      System.out.println("No playlists exist yet");
    }
  }

  public void showPlaylist(String playlistName) {
    VideoPlaylist selectedPlaylist = null;
    for(int i = 0; i<allPlaylists.size(); i++){
      if(allPlaylists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())){
        selectedPlaylist = allPlaylists.get(i);
        break;
      } 
    }
    
    if(selectedPlaylist != null){
      List<Video> videosOfPlaylist = selectedPlaylist.getVideos();
      if(videosOfPlaylist.size() != 0){
        System.out.println("Showing playlist: " + playlistName);
        for(int i = 0; i<videosOfPlaylist.size(); i++){
          String tagString = getTagsAsString(videosOfPlaylist.get(i).getTags());
          System.out.println(videosOfPlaylist.get(i).getTitle() + " (" + videosOfPlaylist.get(i).getVideoId() + ") [" + tagString + "]" + generateFlagStatusMarker(videosOfPlaylist.get(i)));
        }
      }
      else{
        System.out.println("Showing playlist: " + playlistName);
        System.out.println("No videos here yet");
      }
    }
    else{
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    }
    
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    VideoPlaylist selectedPlaylist = null;
    for(int i = 0; i<allPlaylists.size(); i++){
      if(allPlaylists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())){
        selectedPlaylist = allPlaylists.get(i);
        break;
      } 
    }

    if(selectedPlaylist != null){
      Video v = videoLibrary.getVideo(videoId);
      if(v != null){
      selectedPlaylist.removeVideoFromPlaylist(v,playlistName);
      }
      else{
        System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
      }
    }
    else{
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    }
  }

  public void clearPlaylist(String playlistName) {
    VideoPlaylist selectedPlaylist = null;
    for(int i = 0; i<allPlaylists.size(); i++){
      if(allPlaylists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())){
        selectedPlaylist = allPlaylists.get(i);
        break;
      } 
    }

    if(selectedPlaylist != null){
      selectedPlaylist.clearPlaylist(playlistName);
    }
    else{
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void deletePlaylist(String playlistName) {
    VideoPlaylist selectedPlaylist = null;
    for(int i = 0; i<allPlaylists.size(); i++){
      if(allPlaylists.get(i).getName().toUpperCase().equals(playlistName.toUpperCase())){
        selectedPlaylist = allPlaylists.get(i);
        break;
      } 
    }
    
    if(selectedPlaylist != null){
      allPlaylists.remove(selectedPlaylist);
      System.out.println("Deleted playlist: " + playlistName);
    }
    else{
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    }
  }

  public void searchVideos(String searchTerm) {
    ArrayList<Video> foundVideos = new ArrayList<>();

    for(int i = 0; i<allVideos.size(); i++){
      if(allVideos.get(i).getTitle().toUpperCase().contains(searchTerm.toUpperCase()) & allVideos.get(i).isItFlagged() == false){
          foundVideos.add(allVideos.get(i));
      }
    }

    if(foundVideos.size() == 0){
      System.out.println("No search results for " + searchTerm);
    }
    else if(foundVideos.size() > 0){
      System.out.println("Here are the results for " + searchTerm + ":");
      Collections.sort(foundVideos, new CompareTitles());
      for(int i = 0; i<foundVideos.size(); i++){
        String tagString = getTagsAsString(foundVideos.get(i).getTags());
        System.out.println((i+1) + ") " + foundVideos.get(i).getTitle() + " (" + foundVideos.get(i).getVideoId() + ") [" + tagString + "]");
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video." + 
      "\nIf your answer is not a valid number, we will assume it's a no.");

      Scanner scanner = new Scanner(System.in);
      int inputNumber;

      try{
        inputNumber = Integer.parseInt(scanner.nextLine());
        if(inputNumber <= foundVideos.size() & inputNumber > 0){
          playVideo(foundVideos.get(inputNumber-1).getVideoId());
        }
      }
      catch(NumberFormatException e){}
    }
  }

  public void searchVideosWithTag(String videoTag) {
    ArrayList<Video> foundVideos = new ArrayList<>();

    for(int i = 0; i<allVideos.size(); i++){
      List<String> tags = allVideos.get(i).getTags();
      for(int j = 0; j<tags.size(); j++){ 
        if(tags.get(j).toUpperCase().equals(videoTag.toUpperCase()) & allVideos.get(i).isItFlagged() == false){
          foundVideos.add(allVideos.get(i));
        }
      }
    }
    
    if(foundVideos.size() == 0){
      System.out.println("No search results for " + videoTag);
    }
    else if(foundVideos.size() > 0){
      System.out.println("Here are the results for " + videoTag + ":");
      Collections.sort(foundVideos, new CompareTitles());
      for(int i = 0; i<foundVideos.size(); i++){
        String tagString = getTagsAsString(foundVideos.get(i).getTags());
        System.out.println((i+1) + ") " + foundVideos.get(i).getTitle() + " (" + foundVideos.get(i).getVideoId() + ") [" + tagString + "]");
      }
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video." + 
      "\nIf your answer is not a valid number, we will assume it's a no.");

      Scanner scanner = new Scanner(System.in);
      int inputNumber;

      try{
        inputNumber = Integer.parseInt(scanner.nextLine());
        if(inputNumber <= foundVideos.size() & inputNumber > 0){
          playVideo(foundVideos.get(inputNumber-1).getVideoId());
        }
      }
      catch(NumberFormatException e){}
    }
  }

  public void flagVideo(String videoId) {
    Video v = videoLibrary.getVideo(videoId);
    if(v != null){
      if(v.isItFlagged() == false){
        v.flagVideo("Not supplied");
        System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: Not supplied)");
      }
      else{
        System.out.println("Cannot flag video: Video is already flagged");
      }
    }
    else{
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void flagVideo(String videoId, String reason) {
    Video v = videoLibrary.getVideo(videoId);
    if(v != null){
      if(v.isItFlagged() == false){
        v.flagVideo(reason);
        System.out.println("Successfully flagged video: " + v.getTitle() + " (reason: " + reason +")");
      }
      else{
        System.out.println("Cannot flag video: Video is already flagged");
      }
    }
    else{
      System.out.println("Cannot flag video: Video does not exist");
    }
  }

  public void allowVideo(String videoId) {
    Video toBeUnFlagged = videoLibrary.getVideo(videoId);
    if(toBeUnFlagged != null){
      if(toBeUnFlagged.isItFlagged() == true){
        toBeUnFlagged.unflagVideo();
        System.out.println("Successfully removed flag from video: " + toBeUnFlagged.getTitle());
      }
      else{
        System.out.println("Cannot remove flag from video: Video is not flagged");
      }
    }
    else{
      System.out.println("Cannot remove flag from video: Video does not exist");
    }
  }

  public boolean areAllVideosFlagged(){
    int numberFlagged = 0;
    boolean areTheyAllFlagged = false;
    for(int i = 0; i<allVideos.size(); i++){
      if(allVideos.get(i).isItFlagged() == true){
        numberFlagged++;
      }
    }
    if(numberFlagged == allVideos.size()){
      areTheyAllFlagged = true;
    }
    return areTheyAllFlagged;
  }

  public String generateFlagStatusMarker(Video v){
    String marker = "";
    if(v.isItFlagged()){
      marker = " - FLAGGED (reason: " + v.returnFlagReason() + ")";
    }
    return marker;
  }

  public String getTagsAsString(List<String> tags){
    String tagString = "";
      for(int j = 0; j<tags.size(); j++){
        if(j != tags.size()-1){
          tagString += tags.get(j) + " ";
        }
        else{
          tagString += tags.get(j);
        }
      }
      return tagString;
  }
}