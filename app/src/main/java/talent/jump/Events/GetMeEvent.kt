package talent.jump.Events

import talent.jump.data.UserResponse

class GetMeEvent internal constructor(UserResponse: UserResponse){
    private var userResponse: UserResponse = UserResponse

    init {
        this.userResponse = UserResponse
    }

    fun GetUserResponse(): UserResponse
    {
        return userResponse
    }
}

