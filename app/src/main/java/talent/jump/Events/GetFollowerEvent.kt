package talent.jump.Events

import talent.jump.data.FollowersResponse

class GetFollowerEvent internal constructor(followResponse: FollowersResponse){
    private var followResponse: FollowersResponse

    init {
        this.followResponse = followResponse
    }

    fun GetFollowList(): FollowersResponse
    {
        return followResponse
    }
}

