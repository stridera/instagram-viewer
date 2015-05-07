package com.stridera.instagramphotoviewer.instagram;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mjones on 5/5/15.
 */
public class Comment {
    public String created;
    public String comment;
    public User user;

    public static ArrayList<Comment> parseJSON() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("<strong>%s</strong> (%s): %s",
                user.username,
                DateUtils.getRelativeTimeSpanString(Long.parseLong(created) * 1000),
                comment);
    }

    public static ArrayList<Comment> parseJSON(JSONObject commentsJSONObject) throws JSONException {
        ArrayList<Comment> comments = new ArrayList<>();
        JSONArray commentsJSON = commentsJSONObject.getJSONArray("data");
        for (int i = 0; i < commentsJSON.length(); i++) {
            JSONObject commentJSON = commentsJSON.getJSONObject(i);
            Comment comment = new Comment();
            comment.comment = commentJSON.getString("text");
            comment.user = User.parseJSON(commentJSON.getJSONObject("from"));
            comment.created = commentJSON.getString("created_time");
            comments.add(comment);
        }
        return comments;
    }
}

/*
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
 */
