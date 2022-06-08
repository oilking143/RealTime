package talent.jump.data

data class Pagination(
    val page: Int,
    val per_page: Int,
    val stream_total: Int,
    val streamer_total: Int
)