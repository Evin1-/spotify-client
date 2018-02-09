package foo.bar.musicplayer.model

import com.google.gson.annotations.SerializedName

data class Track(
    val name: String? = null,
    val uri: String? = null,
    val id: String? = null,
    val href: String? = null,
    var index: Int = 0,
    @SerializedName("preview_url") val previewUrl: String? = null,
    val album: Album? = null
)
