package talent.jump.data

data class LiveStreamListResponse(
    val data: DataLiveStreamList,
    val pagination: PaginationLiveStream,
    val status: Boolean
)