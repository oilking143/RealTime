package talent.jump.Events

import talent.jump.data.UserResponse

class GetUserEvent internal constructor(UserResponse: UserResponse){
    private var userResponse: UserResponse = UserResponse

    init {
        this.userResponse = UserResponse
    }

    fun GetUserResponse(): UserResponse
    {
        return userResponse
    }
}

