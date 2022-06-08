package talent.jump.data

data class StreamListResponse(
    val data: StreamListData,
    val pagination: Pagination,
    val status: Boolean
)