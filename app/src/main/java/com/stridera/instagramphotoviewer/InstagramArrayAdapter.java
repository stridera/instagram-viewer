package com.stridera.instagramphotoviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.text.Html;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.stridera.instagramphotoviewer.instagram.Instagram;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InstagramArrayAdapter extends ArrayAdapter<Instagram> {

    private static class ViewHolder {
        ImageView ivUser;
        TextView tvUsername;
        ImageView ivImage;
        TextView tvCaption;
        TextView tvCreated;
        TextView tvLikesCommentCount;
        TextView tvComments;
    }

    public InstagramArrayAdapter(Context context, List<Instagram> objects) {
        super(context, R.layout.popular_photos_item, objects);
    }

    public class VideoOverlayTransformation implements Transformation  {
        @Override
        public Bitmap transform(Bitmap source) {
            Bitmap bmOverlay = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
            Bitmap bmVideoOverlay = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_video);

            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();
            int videoOverlayWidth = bmVideoOverlay.getWidth();
            int videoOverlayHeight = bmVideoOverlay.getHeight();

            Canvas canvas = new Canvas(bmOverlay);
            canvas.drawBitmap(source, new Matrix(), null);

            Matrix matrix = new Matrix();
            RectF rectf = new RectF(0, 0, videoOverlayWidth, videoOverlayHeight);
            matrix.mapRect(rectf);
            matrix.postTranslate((sourceWidth - videoOverlayWidth) / 2, (sourceHeight - videoOverlayHeight) / 2);

            canvas.drawBitmap(bmVideoOverlay, matrix, null);

            source.recycle();
            bmVideoOverlay.recycle();

            return bmOverlay;
        }

        @Override public String key() { return "videoOverlay()"; }
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Instagram instagram = getItem(position);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(R.layout.popular_photos_item, parent, false);

            viewHolder.ivUser = (ImageView) view.findViewById(R.id.ivUser);
            viewHolder.tvUsername = (TextView) view.findViewById(R.id.tvUsername);
            viewHolder.ivImage = (ImageView) view.findViewById(R.id.ivImage);
            viewHolder.tvCaption = (TextView) view.findViewById(R.id.tvCaption);
            viewHolder.tvCreated = (TextView) view.findViewById(R.id.tvTimestamp);
            viewHolder.tvLikesCommentCount = (TextView) view.findViewById(R.id.tvLikesAndCommentCount);
            viewHolder.tvComments = (TextView) view.findViewById(R.id.tvLatestComments);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //llHeader.setLayoutParams(new LinearLayout.LayoutParams(instagram.image.width, llHeader.getHeight()));
        viewHolder.ivUser.setImageResource(0);
        Picasso.with(getContext()).load(instagram.user.profile_picture).into(viewHolder.ivUser);
        viewHolder.tvUsername.setText(instagram.user.username);
        viewHolder.ivImage.setImageResource(0);
        if (instagram.type.equals(Instagram.TYPE_IMAGE)) {
            Picasso.with(getContext()).load(instagram.image.url)
                    .placeholder(R.drawable.loading1).into(viewHolder.ivImage);
        } else {
            Picasso.with(getContext()).load(instagram.image.url)
                    .transform(new VideoOverlayTransformation())
                    .placeholder(R.drawable.loading1)
                    .into(viewHolder.ivImage);
        }
        viewHolder.ivImage.setAdjustViewBounds(true);
        viewHolder.ivImage.setTag(R.string.type, instagram.type);
        if (instagram.type.equals(Instagram.TYPE_IMAGE)) {
            if (instagram.image != null && instagram.image.url != null) {
                viewHolder.ivImage.setTag(R.string.url, instagram.image.url);
            } else {
                Log.d("Blah", "Invalid image url!");
            }
        } else {
            if (instagram.video != null && instagram.video.url != null) {
                viewHolder.ivImage.setTag(R.string.url, instagram.video.url);
            } else {
                viewHolder.ivImage.setTag(R.string.url, "INVALID");
                Log.d("Blah", "Invalid video url!");
            }
        }
        viewHolder.ivImage.setOnClickListener(ivImageSelected);

        if (instagram.caption != null) {
            viewHolder.tvCaption.setText(Html.fromHtml(instagram.caption.toString()));
        }

        viewHolder.tvCreated.setText(DateUtils.getRelativeTimeSpanString(Long.parseLong(instagram.created) * 1000));

        String commentsAndLikes;
        if (instagram.likes == 0) {
            commentsAndLikes = "Be the first to like! &nbsp&nbsp ";
        } else {
            commentsAndLikes = String.format("%d %s this. &nbsp;&nbsp; ",
                    instagram.likes,
                    instagram.likes == 1 ? "person likes" : "people like");
        }

        String comments = "";
        if (instagram.comments != null && instagram.comments.size() > 0)
        {
            commentsAndLikes += String.format("%d comment%s.",
                    instagram.commentCount,
                    instagram.commentCount > 1 ? "s" : "");
            comments = instagram.comments.get(instagram.comments.size() - 1).toString();
            if (instagram.comments.size() > 1) {
                comments += "<br />";
                comments += instagram.comments.get(instagram.comments.size() - 2).toString();
            }
        } else {
            comments += "0 Comments.";
        }

        viewHolder.tvLikesCommentCount.setText(Html.fromHtml(commentsAndLikes));
        viewHolder.tvComments.setText(Html.fromHtml(comments));

        return view;
    }

    private View.OnClickListener ivImageSelected = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        String type = (String) v.getTag(R.string.type);
        String url = (String) v.getTag(R.string.url);
        Log.d("BLAH", String.format("Handle onclick. type: %s  URL: %s", type, url));

        if (type.equals(Instagram.TYPE_VIDEO)) {
            Context ctx = getContext();
            Intent i = new Intent(ctx, WatchVideoActivity.class);

            i.putExtra("url", url);
            ctx.startActivity(i);
        }
        //TODO: Figure out how to get the item so I can trigger the full screen video
        }
    };
}
