package talent.jump.data

data class ListPostCommentData(
    val content: String,
    val create_at: Long,
    val delete_at: Int,
    val id: String,
    val metion_users: String,
    val post_id: String,
    val update_at: Long,
    val user: ListPostCommentUser,
    val user_id: String
)