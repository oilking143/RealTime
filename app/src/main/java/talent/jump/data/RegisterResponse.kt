package talent.jump.data

data class RegisterResponse(
    val code: Int,
    val external: Any,
    val message: String,
    val result: Any
)