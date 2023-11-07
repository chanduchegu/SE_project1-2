package com.sismics.music.core.service.spotify;

import com.sismics.music.core.service.RecommendAPI;
import com.sismics.music.core.constant.ConfigType;
import com.sismics.music.core.util.ConfigUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.net.URLEncoder;

public class SpotifyService implements RecommendAPI {
    public String searchTracks1(String query) {
        System.out.println("inside search tracks");
        System.out.println(query);
        query = URLEncoder.encode(query);
        String key = ConfigUtil.getConfigStringValue(ConfigType.LAST_FM_API_KEY);
        String endpointUrl = "http://ws.audioscrobbler.com/2.0/?method=track.search&track=" + query + "&api_key=" + key
                + "&format=json";
        try {
            URL url = new URL(endpointUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // String resp=response.toString();
                System.out.println("sfd");
                // System.out.println(response["results"]["trackmatches"]["track"]["name"]);
                return (response.toString());
            } else {
                throw new IOException("GET request has failed with a response code of " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("exception raised");
        }
        return "";
    }

    public String getAccessToken() throws IOException {
        String clientId = "3fce170b11cf4e7097aad42f8de20572";
        String clientSecret = "716b77bf8ec94eaba258e1de39e1edca";
        // String token = getAccessToken(clientId, clientSecret);
        // System.out.println(token);

        String authString = clientId + ":" + clientSecret;
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

        URL url = new URL("https://accounts.spotify.com/api/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Basic " + encodedAuthString);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);

        String postData = "grant_type=client_credentials";
        conn.getOutputStream().write(postData.getBytes(StandardCharsets.UTF_8));

        int statusCode = conn.getResponseCode();
        String responseBody = "";
        if (statusCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseBody += inputLine;
            }
            in.close();
            String accessToken = responseBody.split(":")[1]
                    .replaceAll("[\"}]", "")
                    .trim();
            return accessToken;
        } else {
            throw new RuntimeException("Failed to get access token from Spotify API. " +
                    "Status code: " + statusCode +
                    " Response message: " + conn.getResponseMessage());
        }

    }

    public String searchTracks(String query) {

        try {
            String token = getAccessToken();
            String authHeader = "Bearer " + token;
            String encodedQuery = URLEncoder.encode(query);
            String ENDPOINT = "https://api.spotify.com/v1/search?type=track";

            String apiUrl = ENDPOINT + "&q=" + encodedQuery;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", authHeader);

            int statusCode = conn.getResponseCode();
            String responseBody = "";
            if (statusCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    responseBody += inputLine;
                }
                in.close();
                return responseBody;
            } else {
                throw new RuntimeException("Failed to search tracks in Spotify API. " +
                        "Status code: " + statusCode +
                        " Response message: " + conn.getResponseMessage());
            }
            // return "";
        } catch (Exception e) {
            System.out.println("exception raised");
            return "";
        }

    }

    public String playListRecommend(String song, String artist) {
        // try {
        // System.out.println("inside search playlist recommend");
        // // song="faded";
        // // song=song.substring(0,5);
        // String token = getAccessToken();
        // String authHeader = "Bearer " + token;

        // System.out.println(song);
        // song = URLEncoder.encode(song);
        // // artist="alan walker";
        // artist = URLEncoder.encode(artist);
        // String key = ConfigUtil.getConfigStringValue(ConfigType.LAST_FM_API_KEY);
        // String acctoken = getAccessToken();
        // String res = searchTracks1(acctoken, song);
        // // return res;
        // // System.out.println("Response is "+res);
        // JsonReader reader1 = Json.createReader(new StringReader(res));

        // // // parse the JSON string and convert it into a JsonObject
        // JsonObject obj1 = reader1.readObject();
        // String seed_artists =
        // obj1.getJsonObject("tracks").getJsonArray("items").getJsonObject(0)
        // .getJsonObject("album").getJsonArray("artists").getJsonObject(
        // 0)
        // .getString("id");
        // System.out.println("ad " + obj1);
        // String seed_tracks =
        // obj1.getJsonObject("tracks").getJsonArray("items").getJsonObject(0)
        // .getJsonObject("album").getString("id");
        // String seed_genres = URLEncoder.encode("classical,pop,rock,chill,groove");
        // System.out.println("artist id " + seed_artists);
        // System.out.println("album id " + seed_tracks);
        // // return "";
        // URL url = new URL(
        // "https://api.spotify.com/v1/recommendations?seed_artists=" + seed_artists
        // + "&seed_tracks=" + seed_tracks + "&seed_genres=" + seed_genres +
        // "&limit=5");

        // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // connection.setRequestMethod("GET");
        // connection.setRequestProperty("Content-Type", "application/json");
        // connection.setRequestProperty("Authorization", authHeader);
        // System.out.println("check1");
        // int responseCode = connection.getResponseCode();
        // System.out.println(responseCode);
        // if (responseCode == HttpURLConnection.HTTP_OK) { // success
        // BufferedReader in = new BufferedReader(new
        // InputStreamReader(connection.getInputStream()));
        // String inputLine;
        // StringBuilder response = new StringBuilder();

        // while ((inputLine = in.readLine()) != null) {
        // response.append(inputLine);
        // }
        // in.close();
        // String resp = response.toString();
        // System.out.println(resp);
        // return (response.toString());
        // } else {
        // throw new IOException("GET request has failed with a response code of " +
        // responseCode);
        // }
        // } catch (Exception e) {
        // System.out.println("exception raised");
        // }
        // return "";

        try {
            System.out.println("inside playlist recommend");
            String token = getAccessToken();
            String authHeader = "Bearer " + token;
            // String acctoken = getAccessToken();
            String res = searchTracks(song);
            JsonReader reader1 = Json.createReader(new StringReader(res));
            JsonObject obj1 = reader1.readObject();
            String seed_artists = obj1.getJsonObject("tracks").getJsonArray("items").getJsonObject(0)
                    .getJsonObject("album").getJsonArray("artists").getJsonObject(
                            0)
                    .getString("id");
            System.out.println("ad " + obj1);
            String seed_tracks = obj1.getJsonObject("tracks").getJsonArray("items").getJsonObject(0)
                    .getJsonObject("album").getString("id");
            // String seed_genres = URLEncoder.encode("classical,pop,rock,chill,groove");
            System.out.println("artist id " + seed_artists);
            System.out.println("album id " + seed_tracks);
            // String seed_artists = "7vk5e3vY1uw9plTHJAMwjN";
            String seed_genres = "3nzuGtN3nXARvvecier4K0";
            int limit = 5;
            // URL url = new URL(
            String urlStr = "https://api.spotify.com/v1/recommendations?seed_artists=" + seed_artists
                    + "&seed_tracks=" + seed_tracks + "&seed_genres=" + seed_genres +
                    "&limit=5";

            // String urlStr = "https://api.spotify.com/v1/recommendations?seed_artists=" +
            // seed_artists
            // + "&seed_genres=" + seed_genres + "&limit=" + limit;
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Accept", "application/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            // System.out.println(content.toString());
            return content.toString();
            // Do something with the content

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

}
