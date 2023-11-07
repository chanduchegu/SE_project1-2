package com.sismics.music.rest.resource;

import com.sismics.music.core.service.RecommendAPI;
import com.sismics.music.core.service.lastfm.LastFmService;
import com.sismics.music.core.service.spotify.SpotifyService;

public class ContextAPI {
    public String method;
    public RecommendAPI recommendAPI;

    ContextAPI(String st) {
        //System.out.println("asd");
        this.method = st;
    }

    public void setObject() {
        //System.out.println("inside set object");
        if (method.equals("lastfm")) {
            //System.out.println("before assigning");
            this.recommendAPI = new LastFmService();
            //System.out.println("after assigning");
        } else if (method.equals( "spotify"))
            this.recommendAPI=new SpotifyService();
    }
}
