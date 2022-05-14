package talent.jump.data

data class GetUserStreamAuth(
    val allow_at: Int,
    val create_at: Int,
    val remark: String,
    val revoke_at: Int,
    val status: Int,
    val user_id: String
)