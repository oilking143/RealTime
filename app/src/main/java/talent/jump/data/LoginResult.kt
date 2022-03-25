package talent.jump.data

data class LoginResult(
    val account: String,
    val agentId: Int,
    val avatar: String,
    val createDate: String,
    val email: String,
    val id: Int,
    val mobile: String,
    val nickname: String,
    val status: Boolean,
    val token: String
)