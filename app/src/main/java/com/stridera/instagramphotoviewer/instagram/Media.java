package com.stridera.instagramphotoviewer.instagram;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mjones on 5/5/15.
 */
public class Media {
    public String url;
    public int width;
    public int height;

    public static Media parseJSON() {
        return null;
    }

    public static Media parseJSON(JSONObject mediaJSON) throws JSONException {
        Media media = new Media();
        media.url = mediaJSON.getString("url");
        media.height = mediaJSON.getInt("height");
        media.width = mediaJSON.getInt("width");
        return media;
    }
}


/*
            "low_resolution": {
                "url": "http://distillery.s3.amazonaws.com/media/2011/02/01/34d027f155204a1f98dde38649a752ad_6.jpg",
                "width": 306,
                "height": 306
            },
 */