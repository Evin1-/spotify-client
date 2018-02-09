package foo.bar.musicplayer.model

import java.io.Serializable

data class Artist(
    val id: String? = null,
    val name: String? = null,
    val popularity: String? = null,
    val images: List<Image>? = null
)
