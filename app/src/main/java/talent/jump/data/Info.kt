package talent.jump.data

data class Info(
    val create_at: Long,
    val delete_at: Int,
    val display_name: String,
    val id: String,
    val living_at: Int,
    val owner: OwnerLiveList,
    val owner_id: String,
    val private: Boolean,
    val pull: String,
    val pull_flv: String,
    val push: String,
    val status: Int,
    val type_id: Int,
    val update_at: Long,
    val viewer_count: Int
)