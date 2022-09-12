package talent.jump.data

data class ListPostData(
    val content: String,
    val create_at: Long,
    val delete_at: Int,
    val head_media: String,
    val id: String,
    val is_like: Boolean,
    val likes: Int,
    val media_ids: List<String>,
    val medias: List<String>,
    val update_at: Long,
    val user: UserLisPost,
    val user_id: String
)