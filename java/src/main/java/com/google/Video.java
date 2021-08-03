package com.google;

import java.util.Collections;
import java.util.List;

/** A class used to represent a video. */
class Video {

  private final String title;
  private final String videoId;
  private final List<String> tags;
  private boolean playing = false;
  private boolean paused = false;
  private boolean flagged = false;
  private String reasonForFlag = "";

  Video(String title, String videoId, List<String> tags) {
    this.title = title;
    this.videoId = videoId;
    this.tags = Collections.unmodifiableList(tags);
  }

  /** Returns the title of the video. */
  String getTitle() {
    return title;
  }

  /** Returns the video id of the video. */
  String getVideoId() {
    return videoId;
  }

  /** Returns a readonly collection of the tags of the video. */
  List<String> getTags() {
    return tags;
  }

  public boolean isItPlaying(){
    return playing;
  }

  public void startPlaying(){
    playing = true;
    System.out.println("Playing video: " + this.getTitle());
  }

  public void stopPlaying(){
    System.out.println("Stopping video: " + this.getTitle());
    playing = false;
    paused = false;
  } 

  public void pauseVid(){
    if(this.isItPaused() == false){
      System.out.println("Pausing video: " + this.getTitle());
      paused = true;
    }
    else{
      System.out.println("Video already paused: " + this.getTitle());
    }
  }

  public void continueVid(){
    System.out.println("Continuing video: " + this.getTitle());
    paused = false;
  }

  public boolean isItPaused(){
    return paused;
  }

  public void flagVideo(String reason){
    if(this.playing == true){
      stopPlaying();
    }
    flagged = true;
    reasonForFlag = reason;
  }

  public void unflagVideo(){
    flagged = false;
  }

  public boolean isItFlagged(){
    return flagged;
  }
  
  public String returnFlagReason(){
    return reasonForFlag;
  }
}
