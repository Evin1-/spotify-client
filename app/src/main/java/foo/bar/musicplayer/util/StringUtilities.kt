package foo.bar.musicplayer.util

import io.reactivex.Single
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object StringUtilities {
  private val timerPrefix = "0:"
  private val timerSufix = "0"

  fun formatTimer(time: Int): String {
    val dividedTime = time / 1000 % 60
    return timerPrefix + if (dividedTime < 10) timerSufix + dividedTime.toString() else dividedTime.toString()
  }

  @Throws(UnsupportedEncodingException::class)
  fun makeUrlEncoded(input: String): Single<String> {
    return Single.fromCallable { URLEncoder.encode(input, "UTF-8") }
  }
}
