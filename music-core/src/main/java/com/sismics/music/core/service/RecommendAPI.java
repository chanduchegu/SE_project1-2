package com.sismics.music.core.service;

public interface RecommendAPI{
    public String searchTracks(String query);
    public String playListRecommend(String song,String artist); 
}