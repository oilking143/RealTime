package talent.jump.Events

import talent.jump.data.GetUserResponse

class GetUserEvent internal constructor(UserResponse: GetUserResponse){
    private var userResponse: GetUserResponse = UserResponse

    init {
        this.userResponse = UserResponse
    }

    fun GetRegisterData(): GetUserResponse
    {
        return userResponse
    }
}

