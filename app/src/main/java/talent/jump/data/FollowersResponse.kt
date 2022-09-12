package talent.jump.data

data class FollowersResponse(
    val data: List<FollowerData>,
    val pagination: FollowerPagination,
    val status: Boolean
)