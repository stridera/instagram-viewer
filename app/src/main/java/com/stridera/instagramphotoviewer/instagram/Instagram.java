package com.stridera.instagramphotoviewer.instagram;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mjones on 5/4/15.
 */
public class Instagram  {

    public static final String BASE_URL = "https://api.instagram.com/v1/";
    public static final String POPULAR_PATH = "media/popular";
    public static final String CLIENT_ID = "CLIENT_ID";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_VIDEO = "video";

    public String type;
    public ArrayList<Comment> comments;
    public int commentCount;
    public Caption caption;
    public int likes;
    public String link;
    public User user;
    public Media thumbnail;
    public Media image;
    public Media video;
    public String created;

    public static String getPopularUrl() {
        return BASE_URL + POPULAR_PATH + "?client_id=" + CLIENT_ID;
    }

    public static ArrayList<Instagram> parseJSON(JSONObject response) throws JSONException {
        JSONArray instagramsJSON = response.getJSONArray("data");
        ArrayList<Instagram> intagrams = new ArrayList<>();
        for (int i = 0; i < instagramsJSON.length(); i++) {
            JSONObject mediaJSON = instagramsJSON.getJSONObject(i);

            String type = mediaJSON.getString("type");
            Instagram instagram = new Instagram();
            instagram.type = type;
            if (mediaJSON.optJSONObject("comments") != JSONObject.NULL) {
                JSONObject commentsJSON = mediaJSON.getJSONObject("comments");
                instagram.commentCount = commentsJSON.getInt("count");
                instagram.comments = Comment.parseJSON(commentsJSON);
            } else {
                instagram.commentCount = 0;
            }
            if (mediaJSON.optJSONObject("caption") != null) {
                instagram.caption = Caption.parseJSON(mediaJSON.getJSONObject("caption"));
            }
            instagram.likes = mediaJSON.getJSONObject("likes").getInt("count");
            instagram.link = mediaJSON.getString("link");
            instagram.user = User.parseJSON(mediaJSON.getJSONObject("user"));
            instagram.thumbnail = Media.parseJSON(mediaJSON.getJSONObject("images").getJSONObject("thumbnail"));
            instagram.image = Media.parseJSON(mediaJSON.getJSONObject("images").getJSONObject("standard_resolution"));
            if (type.equals(TYPE_VIDEO)) {
                instagram.video = Media.parseJSON(mediaJSON.getJSONObject("videos").getJSONObject("standard_resolution"));
            }
            instagram.created = mediaJSON.getString("created_time");
            intagrams.add(instagram);
        }
        return intagrams;
    }

    @Override
    public String toString() {
        return String.format("%s:  Caption: %s.  URL: %s", type, caption.caption, link);
    }
}

/*
{
    "data": [{
        "type": "image",
        "users_in_photo": [],
        "filter": "Gotham",
        "tags": [],
        "comments": { ... },
        "caption": {
            "created_time": "1296656006",
            "text": "ãã¼ãâ¥ã¢ããªå§ãã¦ä½¿ã£ã¦ã¿ãã(^^)",
            "from": {
                "username": "cocomiin",
                "full_name": "",
                "type": "User",
                "id": "1127272"
            },
            "id": "26329105"
        },
        "likes": {
            "count": 35,
            "data": [{
                "username": "mikeyk",
                "full_name": "Kevin S",
                "id": "4",
                "profile_picture": "..."
            }, {...subset of likers...}]
        },
        "link": "http://instagr.am/p/BV5v_/",
        "User": {
            "username": "cocomiin",
            "full_name": "Cocomiin",
            "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_1127272_75sq_1296145633.jpg",
            "id": "1127272"
        },
        "created_time": "1296655883",
        "images": {
            "low_resolution": {
                "url": "http://distillery.s3.amazonaws.com/media/2011/02/01/34d027f155204a1f98dde38649a752ad_6.jpg",
                "width": 306,
                "height": 306
            },
            "thumbnail": {
                "url": "http://distillery.s3.amazonaws.com/media/2011/02/01/34d027f155204a1f98dde38649a752ad_5.jpg",
                "width": 150,
                "height": 150
            },
            "standard_resolution": {
                "url": "http://distillery.s3.amazonaws.com/media/2011/02/01/34d027f155204a1f98dde38649a752ad_7.jpg",
                "width": 612,
                "height": 612
            }
        },
        "id": "22518783",
        "location": null
    },
    {
        "type": "video",
        "videos": {
            "low_resolution": {
                "url": "http://distilleryvesper9-13.ak.instagram.com/090d06dad9cd11e2aa0912313817975d_102.mp4",
                "width": 480,
                "height": 480
            },
            "standard_resolution": {
                "url": "http://distilleryvesper9-13.ak.instagram.com/090d06dad9cd11e2aa0912313817975d_101.mp4",
                "width": 640,
                "height": 640
            },
        "users_in_photo": null,
        "filter": "Vesper",
        "tags": [],
        "comments": {
            "data": [{
                "created_time": "1279332030",
                "text": "Love the sign here",
                "from": {
                    "username": "mikeyk",
                    "full_name": "Mikey Krieger",
                    "id": "4",
                    "profile_picture": "http://distillery.s3.amazonaws.com/profiles/profile_1242695_75sq_1293915800.jpg"
                },
                "id": "8"
            },
            {
                "created_time": "1279341004",
                "text": "Chilako taco",
                "from": {
                    "username": "kevin",
                    "full_name": "Kevin S",
                    "id": "3",
                    "profile_picture": "..."
                },
                "id": "3"
            }],
            "count": 2
        },
        "caption": null,
        "likes": {
            "count": 1,
            "data": [{
                "username": "mikeyk",
                "full_name": "Mikeyk",
                "id": "4",
                "profile_picture": "..."
            }]
        },
        "link": "http://instagr.am/p/D/",
        "User": {
            "username": "kevin",
            "full_name": "Kevin S",
            "profile_picture": "...",
            "id": "3"
        },
        "created_time": "1279340983",
        "images": {
            "low_resolution": {
                "url": "http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_6.jpg",
                "width": 306,
                "height": 306
            },
            "thumbnail": {
                "url": "http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_5.jpg",
                "width": 150,
                "height": 150
            },
            "standard_resolution": {
                "url": "http://distilleryimage2.ak.instagram.com/11f75f1cd9cc11e2a0fd22000aa8039a_7.jpg",
                "width": 612,
                "height": 612
            }
        },
        "id": "3",
        "location": null
    },
    ...]
}
 */