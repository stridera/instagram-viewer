package com.stridera.instagramphotoviewer.instagram;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mjones on 5/5/15.
 */
public class User {
    public String username;
    public String full_name;
    public String profile_picture;

    public static User parseJSON(JSONObject userJSON) throws JSONException {
        User user = new User();
        user.full_name = userJSON.getString("full_name");
        user.username = userJSON.getString("username");
        user.profile_picture = userJSON.getString("profile_picture");
        return user;
    }
}

/*
        "user": {
            "username": "kevin",
            "full_name": "Kevin S",
            "profile_picture": "...",
            "id": "3"
        },
*/