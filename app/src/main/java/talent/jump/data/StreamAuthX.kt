package talent.jump.data

data class StreamAuthX(
    val allow_at: Long,
    val create_at: Long,
    val remark: String,
    val revoke_at: Int,
    val status: Int,
    val user_id: String
)