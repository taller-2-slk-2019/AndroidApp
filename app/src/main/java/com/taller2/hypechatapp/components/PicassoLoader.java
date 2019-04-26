package com.taller2.hypechatapp.components;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.taller2.hypechatapp.R;

public class PicassoLoader {
    private static int default_image = R.drawable.loading_image;
    private static int error_image = R.drawable.default_image;

    public static void load(Context context, String url, final ImageView image) {
        load(context, url, default_image, image);
    }

    public static void load(Context context, String url, int defaultImageResource, final ImageView image) {
        Picasso.with(context)
                .load(url)
                .fit()
                .placeholder(defaultImageResource)
                .error(error_image)
                .into(image);
    }

    public static void loadWithoutFit(Context context, String url, final ImageView image) {
        Picasso.with(context)
                .load(url)
                .resize(500, 1000)
                .onlyScaleDown()
                .centerInside().placeholder(default_image)
                .error(error_image)
                .into(image);
    }
}
