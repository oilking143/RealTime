package talent.jump.data

data class StreamAuth(
    val allow_at: Int,
    val create_at: Long,
    val remark: String,
    val revoke_at: Int,
    val status: Int,
    val user_id: String
)