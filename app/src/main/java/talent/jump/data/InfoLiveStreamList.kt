package talent.jump.data

data class InfoLiveStreamList(
    val create_at: Long,
    val delete_at: Long,
    val display_name: String,
    val email: String,
    val fans_count: Int,
    val follows_count: Int,
    val id: String,
    val introduction: String,
    val living_at: Int,
    val nickname: String,
    val owner: OwnerX,
    val owner_id: String,
    val `private`: Boolean,
    val profile_photo: String,
    val pull: String,
    val pull_flv: String,
    val push: String,
    val reserve_at: Int,
    val status: Int,
    val type: Int,
    val type_id: Int,
    val update_at: Long,
    val username: String,
    val viewer_count: Int
)