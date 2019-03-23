package cl.littlephoenix.itunessearch.models.response

class SongsResponse(val wrapperType: String,
                    val collectionName: String,
                    val trackName: String,
                    val previewUrl: String,
                    val artworkUrl60: String,
                    val trackTimeMillis: Long,
                    val isStreamable: Boolean,
                    var isPlaying: Boolean)
