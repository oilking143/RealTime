package talent.jump.data

data class FansListData(
    val create_at: Long,
    val delete_at: Int,
    val email: String,
    val fans_count: Int,
    val follows_count: Int,
    val id: String,
    val introduction: String,
    val nickname: String,
    val profile_photo: String,
    val reserve_at: Int,
    val type: Int,
    val update_at: Long,
    val username: String
)