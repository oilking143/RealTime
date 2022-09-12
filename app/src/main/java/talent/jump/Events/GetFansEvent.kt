package talent.jump.Events

import talent.jump.data.FansListResponse

class GetFansEvent internal constructor(streamResponse: FansListResponse){
    private var fansResponse: FansListResponse

    init {
        this.fansResponse = streamResponse
    }

    fun GetFansList(): FansListResponse
    {
        return fansResponse
    }
}

