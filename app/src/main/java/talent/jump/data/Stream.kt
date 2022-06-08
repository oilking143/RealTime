package talent.jump.data

data class Stream(
    val create_at: Long,
    val delete_at: Int,
    val description: String,
    val display_name: String,
    val id: String,
    val living_at: Long,
    val owner: Owner,
    val owner_id: String,
    val private: Boolean,
    val pull: String,
    val push: String,
    val status: Int,
    val type_id: Int,
    val update_at: Long,
    var viewer_count: Int
)