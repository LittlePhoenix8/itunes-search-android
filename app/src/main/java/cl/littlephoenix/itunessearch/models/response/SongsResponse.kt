package cl.littlephoenix.itunessearch.models.response

class SongsResponse(val wrapperType: String,
                    val kind: String,
                    val collectionId: Long,
                    val trackId: Long,
                    val artistName: String,
                    val collectionName: String,
                    val trackName: String,
                    val previewUrl: String,
                    val artworkUrl60: String,
                    val releaseDate: String,
                    val trackTimeMillis: Long,
                    val country: String,
                    val primaryGenreName: String,
                    val isStreamable: Boolean,
                    var isPlaying: Boolean)
