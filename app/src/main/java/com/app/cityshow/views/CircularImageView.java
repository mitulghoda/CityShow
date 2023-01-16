package com.app.cityshow.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.app.cityshow.R;
import com.bumptech.glide.Glide;
import com.filepickersample.model.Media;
import com.makeramen.roundedimageview.RoundedImageView;

public class CircularImageView extends RoundedImageView {
    public CircularImageView(Context context) {
        super(context);
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @BindingAdapter("loadImage")
    public static void loadImage(CircularImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).placeholder(R.drawable.ic_logo).into(view);
    }

    public void setUrl(String url) {
        Glide.with(getContext()).load(url).into(this);
    }

    public void loadSVG(String url) {
        setUrl(url);
    }

    public void setUrl(String url, int res_id) {
        Glide.with(getContext()).load(url).into(this);
    }


    public void set(int res_id) {
        Glide.with(this).load(res_id).into(this);
    }
}
