package talent.jump.data

data class LoginResponse(
    val code: Int,
    val external: Any,
    val message: String,
    val loginResult: LoginResult
)