package com.stridera.instagramphotoviewer.instagram;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mjones on 5/5/15.
 */
public class Caption {
    public String created;
    public String caption;
    public User from;

    @Override
    public String toString() {
        return String.format("<strong>%s</strong>: %s", from.username, caption);
    }

    public static Caption parseJSON(JSONObject captionJSON) throws JSONException {
        Caption caption = new Caption();
        caption.created = captionJSON.getString("created_time");
        caption.caption = captionJSON.getString("text");
        caption.from = User.parseJSON(captionJSON.getJSONObject("from"));
        return caption;
    }
}

/*
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
*/