package foo.bar.musicplayer.util

import java.io.UnsupportedEncodingException
import java.net.URLEncoder

object StringUtilities {
  private val timerPrefix = "0:"
  private val timerSufix = "0"

  fun formatTimer(time: Int): String {
    var time = time
    time = time / 1000 % 60
    return timerPrefix + if (time < 10) timerSufix + time.toString() else time.toString()
  }

  @Throws(UnsupportedEncodingException::class)
  fun makeUrlEncoded(input: String): String {
    return URLEncoder.encode(input, "UTF-8")
  }
}
