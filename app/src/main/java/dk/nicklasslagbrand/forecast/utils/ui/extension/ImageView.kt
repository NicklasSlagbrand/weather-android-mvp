package dk.nicklasslagbrand.forecast.utils.ui.extension

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


fun ImageView.loadImage(
        url: String
) =
        Glide.with(context)
                .load(url)
                .into(this)


fun ImageView.loadGif(
        url: String
) =
        Glide.with(context)
                .asGif()
                .load(url)
                .centerCrop()
                .into(this)

fun ImageView.loadImageWithFitCenter(
    url: String
) =
    Glide.with(context)
        .load(url)
        .fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)


fun ImageView.loadImageWithFitCenter(
    url: String,
    onLoadingFinishedListener: () -> Unit
) =
    Glide.with(context)
        .load(url)
        .fitCenter()
        .transition(DrawableTransitionOptions.withCrossFade())
        .listener(object : RequestListener<Drawable> {
            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onLoadingFinishedListener.invoke()
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        })
        .into(this)

fun ImageView.setBase64Image(image: String) {
    val decodedString = Base64.decode(image, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    Glide.with(this)
        .load(bitmap)
        .into(this)
}
