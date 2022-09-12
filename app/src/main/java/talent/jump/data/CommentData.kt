package talent.jump.data

data class CommentData(
    val content: String,
    val create_at: Long,
    val delete_at: Int,
    val id: String,
    val metion_users: Any,
    val post_id: String,
    val update_at: Long,
    val user: Any,
    val user_id: String
)