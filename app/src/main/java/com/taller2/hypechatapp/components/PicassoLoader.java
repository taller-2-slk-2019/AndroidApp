package com.taller2.hypechatapp.components;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.taller2.hypechatapp.R;

public class PicassoLoader {
    public static void load(Context context, String url, final ImageView image) {
        int default_image = R.drawable.loading_image;
        int error_image = R.drawable.default_image;
        Picasso.with(context).load(url).fit().placeholder(default_image)
                .error(error_image).into(image);
    }

    public static void load(Context context, String url, int defaultImageResource, final ImageView image) {
        int error_image = R.drawable.default_image;
        Picasso.with(context).load(url).fit().placeholder(defaultImageResource)
                .error(error_image).into(image);
    }
}
