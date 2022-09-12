package talent.jump.data

data class FansListResponse(
    val data: List<FansListData>,
    val pagination: FansListPagination,
    val status: Boolean
)