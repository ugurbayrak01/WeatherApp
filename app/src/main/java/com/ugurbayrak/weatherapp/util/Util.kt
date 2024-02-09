package com.ugurbayrak.weatherapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

fun ImageView.downloadFromUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .into(this)
}
@BindingAdapter("android:download_icon")
fun downloadIcon(view: ImageView, url: String?) {
    view.downloadFromUrl(url)
}