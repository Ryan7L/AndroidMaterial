package io.material.catalog.musicplayer

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import io.material.catalog.R
import java.util.Locale

class MusicData {
  companion object{
    @JvmStatic
    private val TRACKS = listOf<Track>(
      Track(1, "First", "3:25", true),
      Track(2, "Second", "4:51", false),
      Track(3, "Third", "4:12", false),
      Track(4, "Fourth", "2:34", false),
      Track(5, "Fifth", "439", false),
      Track(6, "Sixth", "2:31", false),
      Track(7, "Seventh", "5:25", false),
      Track(8, "Eighth", "3:46", false),
      Track(9, "Ninth", "4:28", false),
      Track(10, "Tenth", "4:47", false),
      Track(11, "Eleventh", "5:14", false),
      Track(12, "Twelfth", "4:46", false),
      Track(13, "Thirteenth", "7:13", false),
      Track(14, "Fourteenth", "2:43", false)
    )
    @JvmField
     val ALBUMS = listOf<Album>(
      Album(
        0L,
        "Metamorphosis",
        "Sandra Adams",
        R.drawable.album_efe_kurnaz_unsplash,
        TRACKS,
        "52 mins"
      ), Album(
        1L, "Continuity", "Ali Connors", R.drawable.album_pawel_czerwinski_unsplash,
        TRACKS, "92 mins"
      ), Album(
        2L, "Break Point", "David Park", R.drawable.album_jean_philippe_delberghe_unsplash,
        TRACKS, "45 mins"
      ), Album(
        3L, "Headspace", "Charlie z.", R.drawable.album_karina_vorozheeva_unsplash,
        TRACKS, "65 mins"
      ), Album(
        4L, "New Neighbors", "Trevor Hansen", R.drawable.album_amy_shamblen_unsplash,
        TRACKS, "73 mins"
      ), Album(
        5L, "Spaced Out", "Jonas Eckhart", R.drawable.album_pawel_czerwinski_unsplash_2,
        TRACKS, "4 mins"
      ), Album(
        6L, "Holding on", "Elizabeth Park", R.drawable.album_kristopher_roller_unsplash,
        TRACKS, "40 mins"
      ), Album(
        7L, "Persistence", "Britta Holt", R.drawable.album_emile_seguin_unsplash,
        TRACKS, "39 mins"
      ), Album(
        8L, "At the Top", "Annie Chiu", R.drawable.album_ellen_qin_unsplash,
        TRACKS, "46 mins"
      ), Album(
        9L, "On Dry Land", "Alfonso Gonzalez", R.drawable.album_david_clode_unsplash,
        TRACKS, "55 mins"
      )
    )
    @JvmStatic
    fun getAlbumById(albumId: Long): Album {
      return ALBUMS.find { it.id == albumId } ?: throw IllegalArgumentException(
        String.format(Locale.US, "Album %d does not exist.", albumId)
      )
    }
  }
}

/**
 * 保存有关音乐专辑信息的数据类。
 * @property id Long
 * @property title String
 * @property artist String
 * @property cover Int
 * @property tracks List<Track>
 * @property duration String
 * @constructor
 */
data class Album(
  val id: Long,
  val title: String,
  val artist: String,
  @DrawableRes val cover: Int,
  val tracks: List<Track>,
  val duration: String
) {
  companion object {
    @JvmField
    val DIFF_CALLBACK: ItemCallback<Album> = object : ItemCallback<Album>() {
      override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
        return newItem.title == oldItem.title
      }

      override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
        return false
      }
    }

  }
}

/**
 * 用于保存专辑中曲目信息的数据类。
 * @property track Int
 * @property title String
 * @property duration String
 * @property playing Boolean
 * @constructor
 */
data class Track(val track: Int, val title: String, val duration: String, val playing: Boolean) {
  companion object {
    @JvmField
    val DIFF_CALLBACK: ItemCallback<Track> = object : ItemCallback<Track>() {
      override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return newItem.title == oldItem.title
      }

      override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return false
      }

    }
  }

}
