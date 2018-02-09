package foo.bar.musicplayer.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import foo.bar.musicplayer.model.Image

/**
 * Created by evin on 2/9/18.
 */
object ImageUtils {
  fun setImage(context: Context?, r_result_img: ImageView?, images: List<Image>?) {
    val image = getLargestImage(images)
    if (context != null && image != null) {
      Glide.with(context).load(image).into(r_result_img)
    }
  }

  private fun getLargestImage(images: List<Image>?): String? {
    return images?.firstOrNull()?.url
  }
}