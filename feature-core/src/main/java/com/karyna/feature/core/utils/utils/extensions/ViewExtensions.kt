package com.karyna.feature.core.utils.utils.extensions

import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

fun ImageView.showImage(image: String) {
    Glide.with(context).load(image).into(this)
}

fun ImageView.showImage(image: String?, options: RequestOptions) {
    Glide.with(context)
        .load(image)
        .apply(options)
        .into(this@showImage)
}

fun ViewBinding.string(resInt: Int, vararg params: Any) = root.context.getString(resInt, *params)